package ru.matthewhadzhiev.rssreader.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.database.RssBaseHelper;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema.RssItemsTable;
import ru.matthewhadzhiev.rssreader.rssworks.Parser;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;
import ru.matthewhadzhiev.rssreader.ui.AddChannelActivity;


public final class FetchRssItemsService extends IntentService{

    private final String TAG = "FetchRssItemsService";
    public static final String ACTION_FETCH_ITEMS = "ru.matthewhadzhiev.rssreader.network.RESPONSE";
    public static final String EXTRA_RESULT_OUT = "ru.matthewhadzhiev.rssreader.network.result_out";
    private SQLiteDatabase database;

    private static ContentValues getContentValues(RssItem rssItem) {
        ContentValues values = new ContentValues();
        values.put(RssItemsTable.Cols.ADDRESS, rssItem.getUrl());
        values.put(RssItemsTable.Cols.TITLE, rssItem.getTitle());
        values.put(RssItemsTable.Cols.LINK, rssItem.getLink());
        values.put(RssItemsTable.Cols.DESCRIPTION, rssItem.getDescription());

        return values;
    }


    public FetchRssItemsService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //TODO К нам еще должна приходить метка о том, что новый это канал или мы просто смотрим
        //TODO Сам канал в базу добавлять нужно
        //TODO Чистим ведь сначала всё
        //TODO Попроверять как тосты вылетают
        String urlLink = intent.getStringExtra(AddChannelActivity.URL_ADDRESS);


        if (TextUtils.isEmpty(urlLink)) {
            /*
                Этот тост вылетает ещё до взаимодействия с сетью.
                Вряд-ли пользователь сумеет уйти с activity до вылета этого тоста
                (Если он конечно вылетел)
             */
            Toast.makeText(this, R.string.empty_address_input, Toast.LENGTH_LONG).show();
        } else {
            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://")) {
                    urlLink = "https://" + urlLink;
                }

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                List<RssItem> feedList = Parser.parseFeed(inputStream);

                database = new RssBaseHelper(getApplicationContext()).getWritableDatabase();

                for (RssItem item : feedList) {
                    item.setUrl(urlLink);
                    ContentValues values = getContentValues(item);

                    database.insert(RssItemsTable.NAME, null, values);
                }

            } catch (IOException | XmlPullParserException e) {
                //TODO Обработка неудачи случившейся
                Log.e("AddChannelActivity", e.getMessage());
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO ПОдчистить всё возможное за собой
    }
}
