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
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import ru.golden.flickrviewer.items.PhotoItem;
import ru.golden.flickrviewer.R;
import ru.golden.flickrviewer.network.GetImagesService;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyBroadcastReceiver myBroadcastReceiver;
    private static final String ACTION_FETCH_ITEMS = "ru.golden.flickrviewer.RESPONSE";
    private static final String GETTED_IMAGES = "ru.golden.flickrviewer.images";
    private static final String SEARCH_METHOD = "flickr.photos.search";

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
            @SuppressWarnings("unchecked")
            final ArrayList<PhotoItem> photoItems = (ArrayList<PhotoItem>) intent.getSerializableExtra(GETTED_IMAGES);
            if (photoItems.size() != 0) {
                recyclerView.setAdapter(new FeedListAdapter(photoItems));
                findViewById(R.id.no_items).setVisibility(View.GONE);
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.toast_failed_update), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                startService(new Intent(MainActivity.this, GetImagesService.class).putExtra(SEARCH_METHOD, query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                return false;
            }
        });

        return true;
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
