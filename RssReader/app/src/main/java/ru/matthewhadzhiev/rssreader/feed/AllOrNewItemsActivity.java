package ru.matthewhadzhiev.rssreader.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.matthewhadzhiev.rssreader.R;


public final class AllOrNewItemsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_or_new_items_activity);
    }
}
