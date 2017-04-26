package ru.golden.flickrviewer.items;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.golden.flickrviewer.R;

public final class FullItemActivity extends AppCompatActivity {

    private static final String ITEM_LINK_STRING = "ru.matthewhadzhiev.rssreader.feed.ITEM_LINK";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_item);

        final WebView fullItemWebView = (WebView) findViewById(R.id.web_view_item);
        fullItemWebView.getSettings().setJavaScriptEnabled(true);
        //noinspection deprecation
        fullItemWebView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                return false;
            }
        });
        fullItemWebView.loadUrl(getIntent().getParcelableExtra(ITEM_LINK_STRING).toString());
    }
}
