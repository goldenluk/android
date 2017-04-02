package ru.matthewhadzhiev.rssreader.feed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.database.RssBaseHelper;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema;


public final class AllOrNewItemsActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_or_new_items_activity);

        final Button allNewsButton = (Button) findViewById(R.id.button_all_items_feed);
        allNewsButton.setOnClickListener(this);

        final Button newNewsButton = (Button) findViewById(R.id.button_new_items_feed);
        newNewsButton.setOnClickListener(this);

        final Button clearNews = (Button) findViewById(R.id.button_delete_all_items);
        clearNews.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.button_new_items_feed:
                startActivity(new Intent(this, FeedNewsActivity.class).putExtra(FeedNewsActivity.IS_ALL_ITEMS, false));
                break;
            case R.id.button_all_items_feed:
                startActivity(new Intent(this, FeedNewsActivity.class).putExtra(FeedNewsActivity.IS_ALL_ITEMS, true));
                break;
            case R.id.button_delete_all_items:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(R.string.title_delete_all_items_dialog);
                alertDialog.setMessage(R.string.message_delete_all_items_dialog);
                alertDialog.setPositiveButton(R.string.positive_button_delete_all_items_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        try {
                            final SQLiteDatabase database = new RssBaseHelper(getApplicationContext()).getWritableDatabase();
                            database.execSQL("delete from "+ RssItemsDbSchema.RssItemsTable.NAME);
                            database.execSQL("delete from "+ RssItemsDbSchema.RssAllItemsTable.NAME);
                            Toast.makeText(AllOrNewItemsActivity.this, R.string.toast_success_delete_all_items, Toast.LENGTH_LONG).show();
                        } catch (final Throwable throwable) {
                            Toast.makeText(AllOrNewItemsActivity.this, R.string.toast_cant_delete_all_items, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialog.setNegativeButton(R.string.negative_button_delete_all_items_dialog, null);
                alertDialog.show();
        }
    }
}
