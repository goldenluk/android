package ru.matthewhadzhiev.rssreader.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.matthewhadzhiev.rssreader.AndroidLoggingHandler;
import ru.matthewhadzhiev.rssreader.database.RssBaseHelper;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema.RssItemsTable;
import ru.matthewhadzhiev.rssreader.feed.FeedNewsActivity;
import ru.matthewhadzhiev.rssreader.rssworks.Parser;
import ru.matthewhadzhiev.rssreader.rssworks.RssChannel;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;
import ru.matthewhadzhiev.rssreader.channels.AddChannelActivity;


public final class FetchRssItemsService extends IntentService{

    public static final String ACTION_FETCH_ITEMS = "ru.matthewhadzhiev.rssreader.network.RESPONSE";
    public static final String ANSWER_SUCCESS_OR_NOT ="ru.matthewhadzhiev.rssreader.network.success_or_not";
    public static final String IS_UPDATE = "ru.matthewhadzhiev.rssreader.network.is_update";
    public static final String IS_LAST_IN_UPDATE = "ru.matthewhadzhiev.rssreader.network";

    //В манифесте была ошибка, пока я не сделал дефолтный конструктор
    public FetchRssItemsService() {
        super("");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        String urlLink = intent.getStringExtra(AddChannelActivity.URL_ADDRESS);
        InputStream inputStream = null;

        AndroidLoggingHandler.reset(new AndroidLoggingHandler());
        final Logger logger = Logger.getLogger("FeedNewsActivity");

        final Intent responseIntent = new Intent();
        responseIntent.putExtra(IS_LAST_IN_UPDATE, intent.getBooleanExtra(IS_LAST_IN_UPDATE, false));
        responseIntent.setAction(ACTION_FETCH_ITEMS);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);

        if (TextUtils.isEmpty(urlLink)) {
            responseIntent.putExtra(ANSWER_SUCCESS_OR_NOT, false);
        } else {
            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://")) {
                    urlLink = "https://" + urlLink;
                }

                final ArrayList<RssChannel> channelList = new RssBaseHelper(getApplicationContext()).getChannels();

                if (!intent.getBooleanExtra(IS_UPDATE, false)) {
                    for (final RssChannel channel: channelList) {
                        if (channel.getAddress().equals(urlLink)) {
                            logger.log(Level.INFO, "Такой канал уже есть");
                            responseIntent.putExtra(ANSWER_SUCCESS_OR_NOT, false);
                            sendBroadcast(responseIntent);
                            return;
                        }
                    }
                }


                final URL url = new URL(urlLink);
                inputStream = url.openConnection().getInputStream();
                final ArrayList<RssItem> feedList = Parser.parseFeed(inputStream);

                //К этому моменту, если бы мы не законнектились, или не смогли распарсить, мы бы уже пробросили исключение

                final SQLiteDatabase database = new RssBaseHelper(getApplicationContext()).getWritableDatabase();

                if (!intent.getBooleanExtra(IS_UPDATE, false)) {
                    final ContentValues valuesChannel = RssBaseHelper.getContentValuesChannel(new RssChannel(urlLink, true));
                    database.insert(RssItemsDbSchema.RssChannelsTable.NAME, null, valuesChannel);
                }

                //Проверяе, свежие ли записи смотрим или все
                if (!intent.getBooleanExtra(FeedNewsActivity.IS_ALL_ITEMS,false)) {
                    //Эти строки очищают все итемы данного канала
                    final int count = database.delete(RssItemsTable.NAME, RssItemsTable.Cols.ADDRESS + "= ?", new String[] { urlLink});
                    logger.log(Level.INFO, "Удалено итемов " + Integer.toString(count));
                }


                final ArrayList<RssItem> fromDbList = new RssBaseHelper(getApplicationContext()).getItems(true);

                for (final RssItem item : feedList) {
                    item.setUrl(urlLink);
                    boolean addOrNot = true;
                    if (intent.getBooleanExtra(FeedNewsActivity.IS_ALL_ITEMS, false)) {
                        final ContentValues values = RssBaseHelper.getContentValuesForAll(item);
                        for (final RssItem itemFromDb: fromDbList) {
                            if (itemFromDb.getTitle().equals(item.getTitle())) {
                                addOrNot = false;
                                break;
                            }
                        }
                        if (addOrNot) {
                            database.insert(RssItemsDbSchema.RssAllItemsTable.NAME, null, values);
                        }
                    } else {
                        final ContentValues values = RssBaseHelper.getContentValues(item);
                        database.insert(RssItemsTable.NAME, null, values);
                    }
                }

                database.close();

                responseIntent.putExtra(ANSWER_SUCCESS_OR_NOT, true);
            } catch (final Throwable e) {
                responseIntent.putExtra(ANSWER_SUCCESS_OR_NOT, false);
                logger.log(Level.WARNING, "Не сформировали ответ");
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        sendBroadcast(responseIntent);
    }
}
