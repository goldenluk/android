package ru.matthewhadzhiev.rssreader.channels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_channel_activity);

        final String lastChannelKey = "last_channel";

        addingProccessTextView = (TextView) findViewById(R.id.result_text_view);
        editText = (EditText) findViewById(R.id.rss_address);

        final String lastChannel = PreferenceManager.getDefaultSharedPreferences(AddChannelActivity.this)
                .getString(lastChannelKey, null);

        final TextView titleForExample = (TextView) findViewById(R.id.example_text_view);
        final TextView standartOrNotExample = (TextView) findViewById(R.id.last_or_example);

        if (lastChannel != null) {
            titleForExample.setText(R.string.give_last_address);
            standartOrNotExample.setText(lastChannel);
        }

        final Button fetchFeedButton = (Button) findViewById(R.id.read_button);
        fetchFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (editText.getText().toString().equals("")){
                    Toast.makeText(AddChannelActivity.this, R.string.was_empty_address_input, Toast.LENGTH_LONG).show();
                } else {
                    addingProccessTextView.setText(R.string.show_progress_adding_channel);
                    PreferenceManager.getDefaultSharedPreferences(AddChannelActivity.this)
                            .edit()
                            .putString(lastChannelKey, editText.getText().toString())
                            .apply();
                    startService(newFetchRssIntent());
                }
            }
        });

        myBroadcastReceiver = new MyBroadcastReceiver();

        // регистрируем BroadcastReceiver
        final IntentFilter intentFilter = new IntentFilter(
                FetchRssItemsService.ACTION_FETCH_ITEMS);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    private Intent newFetchRssIntent() {
        final Intent fetchRss = new Intent(AddChannelActivity.this, FetchRssItemsService.class);
        fetchRss.putExtra(URL_ADDRESS, editText.getText().toString());

        return fetchRss;
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            addingProccessTextView.setText(R.string.show_adding_channel_end);
            final boolean isResultSuccess = intent.getBooleanExtra(FetchRssItemsService.ANSWER_SUCCESS_OR_NOT, false);
            if (isResultSuccess) {
                Toast.makeText(AddChannelActivity.this, R.string.show_success_result_for_add_channel, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddChannelActivity.this, R.string.show_bad_result_for_add_channel, Toast.LENGTH_LONG).show();
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