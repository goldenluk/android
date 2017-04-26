package ru.golden.flickrviewer.network;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import ru.golden.flickrviewer.PhotoItem;

public final class GetImagesService extends IntentService {

    private static final String TAG = "FlickrFetch";
    private static final String API_KEY = "ec63bffd299e044a8aaee555bc3a45c2";
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    private static final Uri ENDPOINT = Uri.parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .build();


    public GetImagesService() {
        super("");
    }

    private ArrayList<PhotoItem> fetchRecentPhotos() {
        final String url = buildUrl(FETCH_RECENTS_METHOD, null);
        return downloadGalleryItems(url);
    }

    private ArrayList<PhotoItem> searchPhotos(final String query) {
        final String url = buildUrl(SEARCH_METHOD, query);
        return downloadGalleryItems(url);
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


    private ArrayList<PhotoItem> downloadGalleryItems(final String url) {
        final ArrayList<PhotoItem> items = new ArrayList<>();

        try {
            final String jsonString = getUrlString(url);
            Log.i(TAG, "Recieved json: " + jsonString);
            final JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (final JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (final IOException e) {
            Log.e(TAG, "Failed to fetch items", e);
        }

        return items;
    }

    private String buildUrl(final String method, final String query) {
        final Uri.Builder uriBuilder = ENDPOINT.buildUpon()
                .appendQueryParameter("method", method);
        if (method.equals(SEARCH_METHOD)) {
            uriBuilder.appendQueryParameter("text", query);
        }
        return uriBuilder.build().toString();
    }

    private void parseItems(final ArrayList<PhotoItem> items, final JSONObject jsonBody)
            throws IOException, JSONException {
        final JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        final JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        for (int i = 0; i < photoJsonArray.length(); ++i) {
            final JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            final PhotoItem item = new PhotoItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));

            if (!photoJsonObject.has("url_s")) {
                continue;
            }

            item.setUrl(photoJsonObject.getString("url_s"));
            items.add(item);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        fetchRecentPhotos();
    }
}
