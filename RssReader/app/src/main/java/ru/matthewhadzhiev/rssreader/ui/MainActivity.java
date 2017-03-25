package ru.matthewhadzhiev.rssreader.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.channels.ChannelsMenuActivity;
import ru.matthewhadzhiev.rssreader.feed.FeedNewsActivity;

final public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView channelsButton = (ImageView) findViewById(R.id.channels_button);
        channelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startMyActivity(ChannelsMenuActivity.class);
            }
        });

        final ImageView feedButton = (ImageView) findViewById(R.id.feed_button);
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startMyActivity(FeedNewsActivity.class);
            }
        });
    }
    //TODO Пройтись по строкам и добавить button, textview и т.д.
    //TODO кнопкам назначить листенером саму активность и реализовать метод onClick , в нем свитч
    private void startMyActivity (final Class myClass) {
        final Intent intent = new Intent(MainActivity.this, myClass);
        startActivity(intent);
    }
}
