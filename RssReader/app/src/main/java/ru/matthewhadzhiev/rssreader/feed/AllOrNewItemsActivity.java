package ru.matthewhadzhiev.rssreader.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.matthewhadzhiev.rssreader.R;


public final class AllOrNewItemsActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_or_new_items_activity);

        final Button allNewsButton = (Button) findViewById(R.id.button_all_items_feed);
        allNewsButton.setOnClickListener(this);

        final Button newNewsButton = (Button) findViewById(R.id.button_new_items_feed);
        newNewsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.button_new_items_feed:
                startActivity(new Intent(this, FeedNewsActivity.class).putExtra(FeedNewsActivity.IS_ALL_ITEMS, false));
                break;
            case R.id.button_all_items_feed:
                startActivity(new Intent(this, FeedNewsActivity.class).putExtra(FeedNewsActivity.IS_ALL_ITEMS, true));
        }
    }
}
