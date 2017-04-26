package ru.golden.flickrviewer.feed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import ru.golden.flickrviewer.PhotoItem;
import ru.golden.flickrviewer.R;
import ru.golden.flickrviewer.network.GetImagesService;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyBroadcastReceiver myBroadcastReceiver;
    private static final String ACTION_FETCH_ITEMS = "ru.golden.flickrviewer.RESPONSE";
    private static final String GETTED_IMAGES = "ru.golden.flickrviewer.images";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new FeedListAdapter(new ArrayList<PhotoItem>()));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startService(new Intent(MainActivity.this, GetImagesService.class));
            }
        });

        Toast.makeText(this, getString(R.string.toast_update_main_activity), Toast.LENGTH_LONG).show();

        myBroadcastReceiver = new MyBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter(ACTION_FETCH_ITEMS);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);

    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            swipeRefreshLayout.setRefreshing(false);
            //Ненадежный момент, но я в своей задаче уверен, что мне придет именно arraylist
            final ArrayList<PhotoItem> photoItems = (ArrayList<PhotoItem>) intent.getSerializableExtra(GETTED_IMAGES);
            recyclerView.setAdapter(new FeedListAdapter(photoItems));
        }
    }
}
