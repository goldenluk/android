package ru.matthewhadzhiev.rssreader.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.matthewhadzhiev.rssreader.database.RssBaseHelper;
import ru.matthewhadzhiev.rssreader.database.RssCursorWrapper;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema.RssItemsTable;
import ru.matthewhadzhiev.rssreader.rssworks.Parser;
import ru.matthewhadzhiev.rssreader.rssworks.RssChannel;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;
import ru.matthewhadzhiev.rssreader.channels.AddChannelActivity;


public final class FetchRssItemsService extends IntentService{

    public static final String ACTION_FETCH_ITEMS = "ru.matthewhadzhiev.rssreader.network.RESPONSE";
    public static final String ANSWER_SUCCESS_OR_NOT ="ru.matthewhadzhiev.rssreader.network.success_or_not";
    public static final String IS_UPDATE = "ru.matthewhadzhiev.rssreader.network.is_update";
    public static final String IS_LAST_IN_UPDATE = "ru.matthewhadzhiev.rssreader.network";

    private static ContentValues getContentValues(final RssItem rssItem) {
        final ContentValues values = new ContentValues();
        values.put(RssItemsTable.Cols.ADDRESS, rssItem.getUrl());
        values.put(RssItemsTable.Cols.TITLE, rssItem.getTitle());
        values.put(RssItemsTable.Cols.LINK, rssItem.getLink());
        values.put(RssItemsTable.Cols.DESCRIPTION, rssItem.getDescription());

        return values;
    }

    private static ContentValues getContentValuesChannel(final RssChannel rssChannel) {
        final ContentValues values = new ContentValues();
        values.put(RssItemsDbSchema.RssChannelsTable.Cols.ADDRESS, rssChannel.getAddress());
        final int isActive;
        if (rssChannel.isActive()) {
            isActive = 1;
        } else {
            isActive = 0;
        }
        values.put(RssItemsDbSchema.RssChannelsTable.Cols.ACTIVE, isActive);
        return values;
    }

    public FetchRssItemsService(final String name) {
        super(name);
    }

    //В манифесте была ошибка, пока я не сделал дефолтный конструктор
    public FetchRssItemsService() {
        super("");

    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        String urlLink = intent.getStringExtra(AddChannelActivity.URL_ADDRESS);
        InputStream inputStream = null;

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

                final ArrayList<RssChannel> channelList = getChannels();

                final String TAG = "FetchRssItemsService";
                if (!intent.getBooleanExtra(IS_UPDATE, false)) {
                    for (final RssChannel channel: channelList) {
                        if (channel.getAddress().equals(urlLink)) {
                            Log.d(TAG, "Такой канал уже есть");
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
                    final ContentValues valuesChannel = getContentValuesChannel(new RssChannel(urlLink, true));

                    database.insert(RssItemsDbSchema.RssChannelsTable.NAME, null, valuesChannel);
                }


                final int count = database.delete(RssItemsTable.NAME, RssItemsTable.Cols.ADDRESS + "= ?", new String[] { urlLink});
                Log.d(TAG,"Удалено итемов " + Integer.toString(count));

                for (final RssItem item : feedList) {
                    item.setUrl(urlLink);
                    final ContentValues values = getContentValues(item);

                    final long countInsert = database.insert(RssItemsTable.NAME, null, values);
                    Log.d(TAG, "Добавлено итемов " + countInsert);
                }

                database.close();

                responseIntent.putExtra(ANSWER_SUCCESS_OR_NOT, true);
            } catch (final Throwable e) {
                responseIntent.putExtra(ANSWER_SUCCESS_OR_NOT, false);
                Log.e("TAG", e.getMessage());
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


    private ArrayList<RssChannel> getChannels() {

        final SQLiteDatabase database;
        ArrayList<RssChannel> channelList = null;
        try {
            database = new RssBaseHelper(getApplicationContext()).getWritableDatabase();
            final Cursor cursorTemp = database.query(
                    RssItemsDbSchema.RssChannelsTable.NAME,
                    null, // Columns - null выбирает все столбцы
                    null,
                    null,
                    null, // groupBy
                    null, // having
                    null // orderBy
            );

            channelList = new ArrayList<>();
            final RssCursorWrapper cursor = new RssCursorWrapper(cursorTemp);



            //noinspection TryFinallyCanBeTryWithResources
            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    channelList.add(cursor.getRssChannel());
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
                cursorTemp.close();
            }
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        return channelList;
    }

}
