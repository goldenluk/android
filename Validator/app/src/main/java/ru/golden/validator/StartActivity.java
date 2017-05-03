package ru.golden.validator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.golden.validator.fielddata.Field;
import ru.golden.validator.fielddata.FieldActivity;
import ru.golden.validator.fielddata.FieldsDataParser;
import ru.golden.validator.network.GetDataService;

public final class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private MyBroadcastReceiver myBroadcastReceiver;
    private static final String EXTRA_STRING_URL = "ru.golden.validator.URL";
    private static final String ACTION_GET_DATA_FROM_URL = "ru.golden.validator.GET_DATA";
    private static final String EXTRA_RESPONSE_STRING = "ru.golden.validator.RESPONSE_STRING";
    private static final String EXTRA_FIELDS = "ru.golden.validator.FIELDS";

    private Button getDataButton;
    private TextInputEditText inputUrl;
    private TextView exampleTextView;
    private TextView statusTextView;
    private CountDownTimer timerNoAnswer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getDataButton = (Button) findViewById(R.id.url_button);
        getDataButton.setOnClickListener(this);

        inputUrl = (TextInputEditText) findViewById(R.id.input_url);

        exampleTextView = (TextView) findViewById(R.id.example_text_view);
        exampleTextView.setOnClickListener(this);

        statusTextView = (TextView) findViewById(R.id.status_text_view);

        myBroadcastReceiver = new MyBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter(ACTION_GET_DATA_FROM_URL);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);
    }


    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            timerNoAnswer.cancel();

            final String response = intent.getStringExtra(EXTRA_RESPONSE_STRING);

            if ("".equals(response)) {
                Toast.makeText(StartActivity.this, R.string.toast_bad_url, Toast.LENGTH_LONG).show();
                statusTextView.setText(R.string.text_view_status_incorrect_url);
                activateWidgets();
            } else {
                statusTextView.setText(R.string.text_view_status_parsing);
                final Field[] fields = FieldsDataParser.parse(response);

                if (fields == null) {
                    Toast.makeText(StartActivity.this, R.string.toast_bad_parse, Toast.LENGTH_LONG).show();
                    statusTextView.setText(R.string.text_view_status_bad_parse);
                    activateWidgets();
                } else {
                    Toast.makeText(StartActivity.this, R.string.toast_success_parse, Toast.LENGTH_LONG).show();
                    final Intent fieldActivityIntent = new Intent(StartActivity.this, FieldActivity.class);
                    fieldActivityIntent.putExtra(EXTRA_FIELDS, fields);

                    startActivity(fieldActivityIntent);
                    activateWidgets();
                    statusTextView.setText(R.string.text_view_status_wait);
                }
            }
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.url_button:
                statusTextView.setText(R.string.text_view_status_download);
                deactivateWidgets();
                final Intent intent = new Intent(StartActivity.this, GetDataService.class);
                intent.putExtra(EXTRA_STRING_URL, inputUrl.getText().toString());
                startService(intent);

                timerNoAnswer = new CountDownTimer(5000, 1000) {
                    public void onTick(final long millisUntilFinished) {
                    }
                    public void onFinish() {
                        stopService(intent);
                        Toast.makeText(StartActivity.this, R.string.toast_time_end, Toast.LENGTH_LONG).show();
                        activateWidgets();
                        statusTextView.setText(R.string.text_view_status_wait);

                        myBroadcastReceiver = new MyBroadcastReceiver();
                        final IntentFilter intentFilter = new IntentFilter(ACTION_GET_DATA_FROM_URL);
                        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
                        registerReceiver(myBroadcastReceiver, intentFilter);
                    }
                }.start();
                break;
            case R.id.example_text_view:
                inputUrl.setText(R.string.example_url);
                break;
            default:
                break;
        }
    }

    private void deactivateWidgets() {
        exampleTextView.setEnabled(false);
        inputUrl.setEnabled(false);
        getDataButton.setEnabled(false);
    }

    private void activateWidgets() {
        exampleTextView.setEnabled(true);
        inputUrl.setEnabled(true);
        getDataButton.setEnabled(true);
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
