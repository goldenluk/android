package ru.matthewhadzhiev.rssreader.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.channels.ChannelsMenuActivity;
import ru.matthewhadzhiev.rssreader.feed.AllOrNewItemsActivity;

final public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //TODO Горизонтальная ориентация
    //TODO Настройки. Показывать ли прочитанные
    //TODO Настройки. Как часто обновлять новости
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView channelsButton = (ImageView) findViewById(R.id.channels_button);
        channelsButton.setOnClickListener(this);

        final ImageView feedButton = (ImageView) findViewById(R.id.feed_button);
        feedButton.setOnClickListener(this);

    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.channels_button:
                startActivity(new Intent(this, ChannelsMenuActivity.class));
                break;
            case R.id.feed_button:
                startActivity(new Intent(this, AllOrNewItemsActivity.class));
        }
    }
}
