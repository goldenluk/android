package ru.matthewhadzhiev.rssreader.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.database.RssBaseHelper;
import ru.matthewhadzhiev.rssreader.database.RssCursorWrapper;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema.RssItemsTable;
import ru.matthewhadzhiev.rssreader.network.FetchRssItemsService;
import ru.matthewhadzhiev.rssreader.rssworks.RssChannel;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;

final public class FeedNewsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_news_activity);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        final List<RssItem> feedList = getItems();
        recyclerView.setAdapter(new RssFeedListAdapter(feedList));


    }

    private RssCursorWrapper queryItems(final String whereClause,final String[] whereArgs,final String tableName) {
        final SQLiteDatabase database = new RssBaseHelper(getApplicationContext()).getWritableDatabase();

        final Cursor cursor = database.query(
                tableName,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new RssCursorWrapper(cursor);
    }

    private List<RssItem> getItems() {
        final List<RssItem> feedList = new ArrayList<>();
        final RssCursorWrapper cursor = queryItems(null, null, RssItemsTable.NAME);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                feedList.add(cursor.getRssItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return feedList;
    }

    private List<RssChannel> getChannels() {
        final List<RssChannel> channelList = new ArrayList<>();
        final RssCursorWrapper cursor = queryItems(null, null, RssItemsDbSchema.RssChannelsTable.NAME);
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
}
