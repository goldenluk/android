package ru.matthewhadzhiev.rssreader.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import ru.matthewhadzhiev.rssreader.R;

final public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView channelsButton = (ImageView) findViewById(R.id.channels_button);
        channelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChannelsMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
