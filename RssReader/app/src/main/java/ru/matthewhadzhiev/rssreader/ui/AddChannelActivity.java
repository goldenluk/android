package ru.matthewhadzhiev.rssreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.network.FetchRssItemsService;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;

final public class AddChannelActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText editText;
    private SwipeRefreshLayout swipeLayout;
    private final String LAST_CHANNEL_ADD_INPUT = "last_channel_add_input";

    public static final String URL_ADDRESS = "ru.matthewhadzhiev.rssreader.ui.url_address";

    private List<RssItem> feedModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_channel_activity);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        editText = (EditText) findViewById(R.id.rss_address);
        Button fetchFeedButton = (Button) findViewById(R.id.read_button);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editText.setText(PreferenceManager.getDefaultSharedPreferences(AddChannelActivity.this).getString(LAST_CHANNEL_ADD_INPUT, ""));

        fetchFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.setRefreshing(true);
                startService(newFetchRssIntent());
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                startService(newFetchRssIntent());
            }
        });
    }

    private Intent newFetchRssIntent () {
        Intent fetchRss = new Intent(AddChannelActivity.this, FetchRssItemsService.class);
        fetchRss.putExtra(URL_ADDRESS, editText.getText().toString());
        return fetchRss;
    }
}
