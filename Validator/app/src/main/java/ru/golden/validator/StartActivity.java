package ru.golden.validator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public final class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private MyBroadcastReceiver myBroadcastReceiver;
    private static final String EXTRA_STRING_URL = "ru.golden.validator.URL";
    private static final String ACTION_GET_DATA_FROM_URL = "ru.golden.validator.GET_DATA";
    private static final String EXTRA_RESPONSE_STRING = "ru.golden.validator.RESPONSE_STRING";
    private Button getDataButton;
    private TextInputEditText inputUrl;
    private TextView exampleTextView;
    private TextView statusTextView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getDataButton = (Button) findViewById(R.id.url_button);
        getDataButton.setOnClickListener(this);

        inputUrl = (TextInputEditText) findViewById(R.id.input_url);



        myBroadcastReceiver = new MyBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter(ACTION_GET_DATA_FROM_URL);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);
    }


    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {

        }
    }

    @Override
    public void onClick(final View view) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(myBroadcastReceiver);
        } catch (final Throwable e) {
            Log.i("MainActivity", "Не отписался Reciever");
        }

    }
}
