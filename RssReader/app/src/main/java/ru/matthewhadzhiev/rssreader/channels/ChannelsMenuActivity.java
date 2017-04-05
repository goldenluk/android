package ru.matthewhadzhiev.rssreader.channels;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.network.UpdateAllService;

final public class ChannelsMenuActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_menu_activity);

        final Button newChannelButton = (Button) findViewById(R.id.add_channel_button);
        newChannelButton.setOnClickListener(this);

        final Button viewedChannelButton = (Button) findViewById(R.id.viewed_channels_button);
        viewedChannelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.add_channel_button:
                UpdateAllService.setServiceAlarm(getApplicationContext(), true);
                startActivity(new Intent(this, AddChannelActivity.class));
                break;
            case R.id.viewed_channels_button:
                UpdateAllService.setServiceAlarm(getApplicationContext(), false);
                startActivity(new Intent(this, ChannelsControlActivity.class));
                break;
        }
    }
}
