package ru.matthewhadzhiev.rssreader.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.database.RssBaseHelper;
import ru.matthewhadzhiev.rssreader.database.RssCursorWrapper;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema;
import ru.matthewhadzhiev.rssreader.rssworks.RssChannel;

public final class ChannelsControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channels_control_activity);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final List<RssChannel> rssChannels = getChannels();

        recyclerView.setAdapter(new RssChannelListAdapter(rssChannels));

    }


    private RssCursorWrapper queryItems(final String whereClause, final String[] whereArgs, final String tableName) {
        SQLiteDatabase database = null;
        try {
            database = new RssBaseHelper(getApplicationContext()).getWritableDatabase();
        } catch (final Throwable e) {
            e.printStackTrace();
        }


        final Cursor cursor = database.query(
                tableName,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new RssCursorWrapper(cursor);
    }
    private List<RssChannel> getChannels() {
        final List<RssChannel> channelList = new ArrayList<>();
        final RssCursorWrapper cursor = queryItems(null, null, RssItemsDbSchema.RssChannelsTable.NAME);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                channelList.add(cursor.getRssChannel());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return channelList;
    }
}
