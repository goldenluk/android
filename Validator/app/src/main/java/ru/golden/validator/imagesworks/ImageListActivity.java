package ru.golden.validator.imagesworks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;

import ru.golden.validator.R;
import ru.golden.validator.StartActivity;
import ru.golden.validator.fielddata.Field;
import ru.golden.validator.network.GetDataService;


public final class ImageListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String EXTRA_STRING_URL = "ru.golden.validator.URL";
    private static final String ACTION_GET_IMAGES_FROM_URL = "ru.golden.validator.GET_IMAGES";
    private static final String EXTRA_FIELDS = "ru.golden.validator.FIELDS";

    private TextView statusTextView;
    private RecyclerView recyclerView;
    private MyBroadcastReceiver myBroadcastReceiver;
    private CountDownTimer timerNoAnswer;
    private ArrayList<Image> images;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        statusTextView = (TextView) findViewById(R.id.text_view_status_images);
        statusTextView.setEnabled(false);
        statusTextView.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_images);


        myBroadcastReceiver = new MyBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter(ACTION_GET_IMAGES_FROM_URL);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);

        final Intent intent = new Intent(ImageListActivity.this, GetDataService.class);
        intent.putExtra(EXTRA_STRING_URL, getString(R.string.url_for_images));
        intent.putExtra(ACTION_GET_IMAGES_FROM_URL, true);
        startService(intent);

        timerNoAnswer = new CountDownTimer(5000, 1000) {
            public void onTick(final long millisUntilFinished) {
            }

            public void onFinish() {
                stopService(intent);
                Toast.makeText(ImageListActivity.this, R.string.toast_time_end, Toast.LENGTH_LONG).show();

                statusTextView.setEnabled(true);

                myBroadcastReceiver = new MyBroadcastReceiver();
                final IntentFilter intentFilter = new IntentFilter(ACTION_GET_IMAGES_FROM_URL);
                intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
                registerReceiver(myBroadcastReceiver, intentFilter);
            }
        }.start();
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.text_view_status_images:
                statusTextView.setEnabled(false);

                final Intent intent = new Intent(ImageListActivity.this, GetDataService.class);
                intent.putExtra(EXTRA_STRING_URL, getString(R.string.url_for_images));
                intent.putExtra(ACTION_GET_IMAGES_FROM_URL, true);
                startService(intent);
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            timerNoAnswer.cancel();

            final String response = PreferenceManager.getDefaultSharedPreferences(ImageListActivity.this)
                    .getString(getString(R.string.images_response_key),"");


            if ("".equals(response)) {
                statusTextView.setText(R.string.text_view_status_failed_get_images);
                statusTextView.setEnabled(true);
            } else {
                statusTextView.setText(R.string.text_view_status_parsing_images_json);

                final Image[] imagesTemp = ImagesInfoParser.parse(response);

                if (imagesTemp == null) {
                    statusTextView.setText(R.string.text_view_status_failed_get_images);
                    statusTextView.setEnabled(true);
                } else {
                    images = new ArrayList<>(Arrays.asList(imagesTemp));
                    statusTextView.setVisibility(View.GONE);
                    recyclerView.setLayoutManager(new GridLayoutManager(ImageListActivity.this, 3));
                    recyclerView.setAdapter(new ImageListAdapter(images));
                }
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(myBroadcastReceiver);
        } catch (final Throwable e) {
            Log.i("StartActivity", "Не отписался Reciever, либо не существовал");
        }

    }
}
