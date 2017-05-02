package ru.golden.validator.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public final class GetDataService extends IntentService {
    private static final String EXTRA_STRING_URL = "ru.golden.validator.URL";
    private static final String ACTION_GET_DATA_FROM_URL = "ru.golden.validator.GET_DATA";
    private static final String EXTRA_RESPONSE_STRING = "ru.golden.validator.RESPONSE_STRING";

    public GetDataService() {
        super("");
    }

    private byte[] getUrlBytes(final String urlSpec) throws IOException {
        final URL url = new URL(urlSpec);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead;
            final byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    private String getUrlString(final String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        String responseString = "";
        if (intent != null && intent.getStringExtra(EXTRA_STRING_URL) != null) {
            try {
                responseString = getUrlString(intent.getStringExtra(EXTRA_STRING_URL));
            } catch (final Throwable e) {
                responseString = "";
            }
        }
        final Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_GET_DATA_FROM_URL);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(EXTRA_RESPONSE_STRING, responseString);
        sendBroadcast(responseIntent);
    }
}
