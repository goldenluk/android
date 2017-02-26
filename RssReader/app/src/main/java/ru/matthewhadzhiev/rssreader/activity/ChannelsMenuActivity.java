package ru.matthewhadzhiev.rssreader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.matthewhadzhiev.rssreader.R;

final public class ChannelsMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_menu_activity);

        Button newChannelButton = (Button) findViewById(R.id.add_channel_button);
        newChannelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChannelsMenuActivity.this, ReadChannelActivity.class);
                startActivity(intent);
            }
        });
    }
}
