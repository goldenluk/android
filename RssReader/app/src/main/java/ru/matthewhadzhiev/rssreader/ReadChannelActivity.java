package ru.matthewhadzhiev.rssreader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReadChannelActivity extends AppCompatActivity {
    private String TAG = "ReadChannelActivity";
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private SwipeRefreshLayout mSwipeLayout;

    private List<RssItem> mFeedModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_activity);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mEditText = (EditText) findViewById(R.id.rss_address);
        Button fetchFeedButton = (Button) findViewById(R.id.read_button);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        fetchFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchRssItem().execute((Void) null);
            }
        });
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchRssItem().execute((Void) null);
            }
        });
    }

    public List<RssItem> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        List<RssItem> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null) {
                    continue;
                }

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }

                if (title != null && link != null && description != null) {
                    if(isItem) {
                        RssItem item = new RssItem(title, link, description);
                        items.add(item);
                    }
                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

    private class FetchRssItem extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
            mSwipeLayout.setRefreshing(true);
            urlLink = mEditText.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink)) {
                return false;
            }

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://")) {
                    urlLink = "http://" + urlLink;
                }

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = parseFeed(inputStream);
                return true;
            } catch (IOException | XmlPullParserException e) {
                Log.e(TAG, e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeLayout.setRefreshing(false);

            if (success) {
                mRecyclerView.setAdapter(new RssFeedListAdapter(mFeedModelList));
                Toast.makeText(ReadChannelActivity.this,
                        getString(R.string.success_add_channel),
                        Toast.LENGTH_LONG).show();
                //TODO Прикручиваем базу и сохраняем в неё
            } else {
                Toast.makeText(ReadChannelActivity.this,
                        getString(R.string.invalid_address),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
