package ru.matthewhadzhiev.rssreader.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.network.FetchRssItemsService;

final public class AddChannelActivity extends AppCompatActivity {
    private EditText editText;
    private MyBroadcastReceiver myBroadcastReceiver;
    private TextView addingProccessTextView;

    public static final String URL_ADDRESS = "ru.matthewhadzhiev.rssreader.ui.url_address";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_channel_activity);

        addingProccessTextView = (TextView) findViewById(R.id.result_text_view);
        editText = (EditText) findViewById(R.id.rss_address);
        final Button fetchFeedButton = (Button) findViewById(R.id.read_button);



        fetchFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingProccessTextView.setText(R.string.show_progress_adding_channel);
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
            addingProccessTextView.setText(R.string.show_adding_channel_end);
            boolean isResultSuccess = intent.getBooleanExtra(FetchRssItemsService.ANSWER_SUCCESS_OR_NOT, false);
            if (isResultSuccess) {
                Toast.makeText(AddChannelActivity.this, R.string.show_success_result_for_add_channel, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddChannelActivity.this, R.string.show_bad_result_for_add_channel, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
}
