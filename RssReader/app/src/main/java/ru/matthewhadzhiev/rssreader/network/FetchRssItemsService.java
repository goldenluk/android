package ru.matthewhadzhiev.rssreader.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.database.RssBaseHelper;
import ru.matthewhadzhiev.rssreader.database.RssCursorWrapper;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema.RssItemsTable;
import ru.matthewhadzhiev.rssreader.rssworks.Parser;
import ru.matthewhadzhiev.rssreader.rssworks.RssChannel;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;
import ru.matthewhadzhiev.rssreader.ui.AddChannelActivity;


public final class FetchRssItemsService extends IntentService{

    private final String TAG = "FetchRssItemsService";
    public static final String ACTION_FETCH_ITEMS = "ru.matthewhadzhiev.rssreader.network.RESPONSE";
    public static final String ANSWER_SUCCESS_OR_NOT ="ru.matthewhadzhiev.rssreader.network.success_or_not";
    public static final String IS_UPDATE = "ru.matthewhadzhiev.rssreader.network.is_update";

    private static ContentValues getContentValues(final RssItem rssItem) {
        ContentValues values = new ContentValues();
        values.put(RssItemsTable.Cols.ADDRESS, rssItem.getUrl());
        values.put(RssItemsTable.Cols.TITLE, rssItem.getTitle());
        values.put(RssItemsTable.Cols.LINK, rssItem.getLink());
        values.put(RssItemsTable.Cols.DESCRIPTION, rssItem.getDescription());

        return values;
    }

    private static ContentValues getContentValuesChannel(final RssChannel rssChannel) {
        ContentValues values = new ContentValues();
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
    protected void onHandleIntent(Intent intent) {
        //TODO Сам канал в базу добавлять нужно
        //TODO Чистим ведь сначала всё
        String urlLink = intent.getStringExtra(AddChannelActivity.URL_ADDRESS);
        InputStream inputStream = null;
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_FETCH_ITEMS);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);

        if (TextUtils.isEmpty(urlLink)) {
            responseIntent.putExtra(ANSWER_SUCCESS_OR_NOT, false);
        } else {
            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://")) {
                    urlLink = "https://" + urlLink;
                }

                final List<RssChannel> channelList = getChannels();

                for (RssChannel channel: channelList) {
                    if (channel.getAddress().equals(urlLink)) {
                        Log.d(TAG, "Такой канал уже есть");
                        responseIntent.putExtra(ANSWER_SUCCESS_OR_NOT, false);
                        sendBroadcast(responseIntent);
                        return;
                    }
                }

                final URL url = new URL(urlLink);
                inputStream = url.openConnection().getInputStream();
                final List<RssItem> feedList = Parser.parseFeed(inputStream);

                //К этому моменту, если бы мы не законнектились, или не смогли распарсить, мы бы уже пробросили исключение


                final SQLiteDatabase database = new RssBaseHelper(getApplicationContext()).getWritableDatabase();

                ContentValues valuesChannel = getContentValuesChannel(new RssChannel(urlLink, true));
                database.insert(RssItemsDbSchema.RssChannelsTable.NAME, null, valuesChannel);

                int count = database.delete(RssItemsTable.NAME, RssItemsTable.Cols.ADDRESS + "= ?", new String[] { urlLink});
                Log.d(TAG,"Удалено итемов " + Integer.toString(count));

                for (final RssItem item : feedList) {
                    item.setUrl(urlLink);
                    ContentValues values = getContentValues(item);

                    long countInsert = database.insert(RssItemsTable.NAME, null, values);
                    Log.d(TAG, "Добавлено итемов " + countInsert);
                }

                responseIntent.putExtra(ANSWER_SUCCESS_OR_NOT, true);
            } catch (IOException | XmlPullParserException e) {
                //TODO Обработка неудачи случившейся
                Log.e("TAG", e.getMessage());
            } finally {
                //TODO ЗАкрывать поток как-то по другому. Нельзя обворачивать close в catch тупо
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        sendBroadcast(responseIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO ПОдчистить всё возможное за собой
    }

    private List<RssChannel> getChannels() {
        final List<RssChannel> channelList = new ArrayList<>();
        final RssCursorWrapper cursor = queryItems(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                channelList.add(cursor.getRssChannel());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return channelList;
    }

    private RssCursorWrapper queryItems(final String whereClause,final String[] whereArgs) {
        final SQLiteDatabase database = new RssBaseHelper(getApplicationContext()).getWritableDatabase();

        final Cursor cursor = database.query(
                RssItemsDbSchema.RssChannelsTable.NAME,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new RssCursorWrapper(cursor);
    }
}
