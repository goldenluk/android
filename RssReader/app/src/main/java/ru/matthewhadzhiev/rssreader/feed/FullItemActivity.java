package ru.matthewhadzhiev.rssreader.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import ru.matthewhadzhiev.rssreader.R;


public final class FullItemActivity extends AppCompatActivity {
    public static final String ITEM_DESCRIPTION = "ru.matthewhadzhiev.rssreader.feed.ITEM_DESCRIPTION";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_item_activity);

        final String description = getIntent().getStringExtra(ITEM_DESCRIPTION);

        final WebView fullItemWebView = (WebView) findViewById(R.id.web_view_item);

        if (description == null || description.equals("")) {
            fullItemWebView.loadDataWithBaseURL(null, getString(R.string.text_for_webview_no_description), "text/html", "UTF-8", null);
        } else {
            fullItemWebView.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null);
        }

    }
}
