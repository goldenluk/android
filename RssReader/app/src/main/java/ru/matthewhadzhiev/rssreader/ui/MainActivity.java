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
    //TODO Регистрироваться как обработчик
    //TODO ActionBar
    //TODO Порядок слов в уведомлении и слово итем поменять
    //TODO новых итемов 0 не писать
    //TODO в новости дата ссылка и заголовок
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView channelsButton = (ImageView) findViewById(R.id.channels_button);
        channelsButton.setOnClickListener(this);

        final ImageView feedButton = (ImageView) findViewById(R.id.feed_button);
        feedButton.setOnClickListener(this);

        final ImageView settingsButton = (ImageView) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);

    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.channels_button:
                startActivity(new Intent(this, ChannelsMenuActivity.class));
                break;
            case R.id.feed_button:
                startActivity(new Intent(this, AllOrNewItemsActivity.class));
                break;
            case R.id.settings_button:
                startActivity(new Intent(this, SettingsActivity.class));
        }
    }
}
