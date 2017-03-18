package ru.matthewhadzhiev.rssreader.feed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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
import ru.matthewhadzhiev.rssreader.channels.AddChannelActivity;

final public class FeedNewsActivity extends AppCompatActivity{
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_news_activity);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);


        //TODO Разобраться как эта штука работает
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final List<RssChannel> channelList = getChannels();
                for (int i = 0; i < channelList.size(); ++i) {
                    if (channelList.get(i).isActive()) {
                        swipeRefreshLayout.setRefreshing(true);
                        startFetchRssIntent(channelList, i);
                    }
                }
            }
        });

        final List<RssItem> feedList = getItems();
        recyclerView.setAdapter(new RssFeedListAdapter(feedList));

        myBroadcastReceiver = new MyBroadcastReceiver();

        // регистрируем BroadcastReceiver
        final IntentFilter intentFilter = new IntentFilter(
                FetchRssItemsService.ACTION_FETCH_ITEMS);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    private void startFetchRssIntent(final List<RssChannel> channelList, final int i) {
        final Intent fetchRss = new Intent(FeedNewsActivity.this, FetchRssItemsService.class);
        fetchRss.putExtra(AddChannelActivity.URL_ADDRESS, channelList.get(i).getAddress());
        fetchRss.putExtra(FetchRssItemsService.IS_UPDATE, true);
        if (i == channelList.size() - 1) {
            fetchRss.putExtra(FetchRssItemsService.IS_LAST_IN_UPDATE, true);
        }
        startService(fetchRss);
    }


    private List<RssItem> getItems() {
        List<RssItem> feedList = null;
        final SQLiteDatabase database;
        try {
            database = new RssBaseHelper(getApplicationContext()).getWritableDatabase();
            final Cursor cursorTemp = database.query(
                    RssItemsTable.NAME,
                    null, // Columns - null выбирает все столбцы
                    null,
                    null,
                    null, // groupBy
                    null, // having
                    null // orderBy
            );

            feedList = new ArrayList<>();
            final RssCursorWrapper cursor = new RssCursorWrapper(cursorTemp);

            //noinspection TryFinallyCanBeTryWithResources
            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    feedList.add(cursor.getRssItem());
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            cursorTemp.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        return feedList;
    }

    private List<RssChannel> getChannels() {
        final SQLiteDatabase database;
        List<RssChannel> channelList = null;
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
            }
            cursorTemp.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }



        return channelList;
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final List<RssItem> feedList = getItems();
            if (intent.getBooleanExtra(FetchRssItemsService.IS_LAST_IN_UPDATE, false)) {
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.setAdapter(new RssFeedListAdapter(feedList));
                if (!intent.getBooleanExtra(FetchRssItemsService.ANSWER_SUCCESS_OR_NOT, false)) {
                    Toast.makeText(FeedNewsActivity.this, R.string.show_bad_result_for_update_feed, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(myBroadcastReceiver);
        } catch (final Throwable e) {
            e.printStackTrace();
        }

    }
}
