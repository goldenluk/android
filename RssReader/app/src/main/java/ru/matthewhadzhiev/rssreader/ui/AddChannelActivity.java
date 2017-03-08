package ru.matthewhadzhiev.rssreader.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.rssworks.Parser;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;

final public class AddChannelActivity extends AppCompatActivity {
    private String TAG = "AddChannelActivity";
    private RecyclerView recyclerView;
    private EditText editText;
    private SwipeRefreshLayout swipeLayout;
    private final String LAST_CHANNEL_ADD_INPUT = "last_channel_add_input";

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
                new FetchRssItem().execute((Void) null);
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchRssItem().execute((Void) null);
            }
        });
    }


    private class FetchRssItem extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
            swipeLayout.setRefreshing(true);
            urlLink = editText.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink)) {
                return false;
            }

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://")) {
                    urlLink = "https://" + urlLink;
                }

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                feedModelList = Parser.parseFeed(inputStream);
                return true;
            } catch (IOException | XmlPullParserException e) {
                Log.e(TAG, e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            swipeLayout.setRefreshing(false);

            if (success) {
                recyclerView.setAdapter(new RssFeedListAdapter(feedModelList));

                PreferenceManager.getDefaultSharedPreferences(AddChannelActivity.this)
                        .edit()
                        .putString(LAST_CHANNEL_ADD_INPUT, editText.getText().toString())
                        .apply();

                //TODO Прикручиваем базу и сохраняем в неё


                Toast.makeText(AddChannelActivity.this,
                        getString(R.string.success_add_channel),
                        Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(AddChannelActivity.this,
                        getString(R.string.invalid_address),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
