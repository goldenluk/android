package ru.matthewhadzhiev.rssreader.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.network.FetchRssItemsService;

final public class AddChannelActivity extends AppCompatActivity {
    private EditText editText;
    private SwipeRefreshLayout swipeLayout;
    private MyBroadcastReceiver myBroadcastReceiver;

    public static final String URL_ADDRESS = "ru.matthewhadzhiev.rssreader.ui.url_address";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_channel_activity);

        editText = (EditText) findViewById(R.id.rss_address);
        final Button fetchFeedButton = (Button) findViewById(R.id.read_button);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeLayout.setEnabled(false);

        final String LAST_CHANNEL_ADD_INPUT = "last_channel_add_input";
        editText.setText(PreferenceManager.getDefaultSharedPreferences(AddChannelActivity.this).getString(LAST_CHANNEL_ADD_INPUT, ""));

        fetchFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.setRefreshing(true);
                startService(newFetchRssIntent());
            }
        });

        myBroadcastReceiver = new MyBroadcastReceiver();

        // регистрируем BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(
                FetchRssItemsService.ACTION_FETCH_ITEMS);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    private Intent newFetchRssIntent() {
        Intent fetchRss = new Intent(AddChannelActivity.this, FetchRssItemsService.class);
        fetchRss.putExtra(URL_ADDRESS, editText.getText().toString());

        return fetchRss;
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            swipeLayout.setRefreshing(false);
            TextView resultTextView = (TextView) findViewById(R.id.result_text_view);
            boolean isResultSuccess = intent.getBooleanExtra(FetchRssItemsService.ANSWER_SUCCESS_OR_NOT, false);
            if (isResultSuccess) {
                resultTextView.setText(R.string.show_success_result_for_add_channel);
            } else {
                resultTextView.setText(R.string.show_bad_result_for_add_channel);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
}
