package ru.matthewhadzhiev.rssreader.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

import ru.matthewhadzhiev.rssreader.R;


public final class FullItemActivity extends AppCompatActivity {
    public static final String ITEM_DESCRIPTION_STRING= "ru.matthewhadzhiev.rssreader.feed.ITEM_DESCRIPTION";
    public static final String ITEM_TITLE_STRING = "ru.matthewhadzhiev.rssreader.feed.ITEM_TITLE";
    public static final String ITEM_LINK_STRING = "ru.matthewhadzhiev.rssreader.feed.ITEM_LINK";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_item_activity);

        final String description = getIntent().getStringExtra(ITEM_DESCRIPTION_STRING);
        final String title = getIntent().getStringExtra(ITEM_TITLE_STRING);
        final String link = getIntent().getStringExtra(ITEM_LINK_STRING);

        final TextView titleTextView = (TextView) findViewById(R.id.text_view_item_title);
        final TextView linkTextView = (TextView) findViewById(R.id.text_view_item_link);
        final WebView fullItemWebView = (WebView) findViewById(R.id.web_view_item);

        if (description == null || ("").equals(description)) {
            fullItemWebView.loadDataWithBaseURL(null, getString(R.string.text_for_webview_no_description), "text/html", "UTF-8", null);
        } else {
            fullItemWebView.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null);
        }

        if (title == null || ("").equals(title)) {
            titleTextView.setText(R.string.text_view_no_title_for_item);
        } else {
            titleTextView.setText(title);
        }

        if (link == null || ("").equals(link)) {
            linkTextView.setText(R.string.text_view_no_link_for_item);
        } else {
            linkTextView.setText(link);
        }

    }
}
