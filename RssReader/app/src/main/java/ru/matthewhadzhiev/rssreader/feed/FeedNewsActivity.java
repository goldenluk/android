package ru.matthewhadzhiev.rssreader.feed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.matthewhadzhiev.rssreader.AndroidLoggingHandler;
import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.database.RssBaseHelper;
import ru.matthewhadzhiev.rssreader.network.FetchRssItemsService;
import ru.matthewhadzhiev.rssreader.rssworks.RssChannel;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;


final public class FeedNewsActivity extends AppCompatActivity{
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyBroadcastReceiver myBroadcastReceiver;
    private Logger logger;
    private boolean isAll;

    private static final String ACTION_FETCH_ITEMS = "ru.matthewhadzhiev.rssreader.network.RESPONSE";
    private static final String ANSWER_SUCCESS_OR_NOT ="ru.matthewhadzhiev.rssreader.network.success_or_not";
    private static final String IS_UPDATE = "ru.matthewhadzhiev.rssreader.network.is_update";
    private static final String IS_LAST_IN_UPDATE = "ru.matthewhadzhiev.rssreader.network";
    private static final String URL_ADDRESS = "ru.matthewhadzhiev.rssreader.ui.url_address";
    private static final String IS_ALL_ITEMS = "ru.matthewhadzhiev.rssreader.feed.IS_ALL_ITEMS";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_news_activity);


        isAll = getIntent().getBooleanExtra(IS_ALL_ITEMS, false);

        AndroidLoggingHandler.reset(new AndroidLoggingHandler());
        logger = Logger.getLogger("FeedNewsActivity");


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final List<RssChannel> channelList = new RssBaseHelper(FeedNewsActivity.this).getChannels();
                for (int i = 0; i < channelList.size(); ++i) {
                    if (channelList.get(i).isActive()) {
                        swipeRefreshLayout.setRefreshing(true);
                        startFetchRssIntent(channelList, i);
                    }
                }
            }
        });

        final ArrayList<RssItem> feedList = new RssBaseHelper(FeedNewsActivity.this).getItems(isAll);
        if (isAll) {
            if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.key_show_readed),false)) {
                final ListIterator<RssItem> iterator = feedList.listIterator();
                while (iterator.hasNext()) {
                    if (iterator.next().isReaded()) {
                        iterator.remove();
                    }
                }
            }
        }

        if (feedList.size() != 0) {
            findViewById(R.id.text_view_no_items).setVisibility(View.GONE);
        }

        recyclerView.setAdapter(new RssFeedListAdapter(feedList, getApplicationContext()));

        myBroadcastReceiver = new MyBroadcastReceiver();

        // регистрируем BroadcastReceiver
        final IntentFilter intentFilter = new IntentFilter(
                ACTION_FETCH_ITEMS);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    private void startFetchRssIntent(final List<RssChannel> channelList, final int i) {
        final Intent fetchRss = new Intent(FeedNewsActivity.this, FetchRssItemsService.class);
        fetchRss.putExtra(URL_ADDRESS, channelList.get(i).getAddress());
        fetchRss.putExtra(IS_UPDATE, true);
        fetchRss.putExtra(IS_ALL_ITEMS, isAll);
        if (i == channelList.size() - 1) {
            fetchRss.putExtra(IS_LAST_IN_UPDATE, true);
        }
        startService(fetchRss);
    }





    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ArrayList<RssItem> feedList = new RssBaseHelper(FeedNewsActivity.this).getItems(isAll);
            if (feedList.size() != 0) {
                findViewById(R.id.text_view_no_items).setVisibility(View.GONE);
            }
            if (intent.getBooleanExtra(IS_LAST_IN_UPDATE, false)) {
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.setAdapter(new RssFeedListAdapter(feedList, getApplicationContext()));
                if (!isAnswerSuccess(intent)) {
                    Toast.makeText(FeedNewsActivity.this, R.string.show_bad_result_for_update_feed, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean isAnswerSuccess (final Intent intent) {
         return intent.getBooleanExtra(ANSWER_SUCCESS_OR_NOT, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(myBroadcastReceiver);
        } catch (final Throwable e) {
            logger.log(Level.WARNING,"Не отписался receiver");
        }

    }
}
