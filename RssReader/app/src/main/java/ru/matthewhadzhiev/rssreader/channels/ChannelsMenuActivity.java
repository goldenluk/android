package ru.matthewhadzhiev.rssreader.channels;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.matthewhadzhiev.rssreader.R;

final public class ChannelsMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_menu_activity);

        final Button newChannelButton = (Button) findViewById(R.id.add_channel_button);
        newChannelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(ChannelsMenuActivity.this, AddChannelActivity.class);
                startActivity(intent);
            }
        });

        final Button viewedChannelButton = (Button) findViewById(R.id.viewed_channels_button);
        viewedChannelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(ChannelsMenuActivity.this, ChannelsControlActivity.class);
                startActivity(intent);
            }
        });
    }
}
