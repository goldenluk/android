package ru.matthewhadzhiev.rssreader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.matthewhadzhiev.rssreader.AndroidLoggingHandler;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema.RssItemsTable;
import ru.matthewhadzhiev.rssreader.rssworks.RssChannel;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;

public final class RssBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "rssItemsBase.db";
    private Logger logger;

    public RssBaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        AndroidLoggingHandler.reset(new AndroidLoggingHandler());
        logger = Logger.getLogger("RssBaseHelper");

        try {
            sqLiteDatabase.execSQL("create table " + RssItemsTable.NAME + "(" +
                    " _id integer primary key autoincrement, " +
                    RssItemsTable.Cols.ADDRESS + ", " +
                    RssItemsTable.Cols.TITLE + ", " +
                    RssItemsTable.Cols.LINK + ", " +
                    RssItemsTable.Cols.DESCRIPTION +
                    ")"
            );

            sqLiteDatabase.execSQL("create table " + RssItemsDbSchema.RssChannelsTable.NAME + "(" +
                    " _id integer primary key autoincrement, " +
                    RssItemsDbSchema.RssChannelsTable.Cols.ADDRESS + ", " +
                    RssItemsDbSchema.RssChannelsTable.Cols.ACTIVE +
                    ")"
            );
        } catch (final SQLException e) {
            logger.log(Level.WARNING, "Не создалась база");
        }

    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {

    }


    public static ContentValues getContentValues(final RssItem rssItem) {
        final ContentValues values = new ContentValues();
        values.put(RssItemsTable.Cols.ADDRESS, rssItem.getUrl());
        values.put(RssItemsTable.Cols.TITLE, rssItem.getTitle());
        values.put(RssItemsTable.Cols.LINK, rssItem.getLink());
        values.put(RssItemsTable.Cols.DESCRIPTION, rssItem.getDescription());

        return values;
    }

    public static ContentValues getContentValuesChannel(final RssChannel rssChannel) {
        final ContentValues values = new ContentValues();
        values.put(RssItemsDbSchema.RssChannelsTable.Cols.ADDRESS, rssChannel.getAddress());
        final int isActive;
        if (rssChannel.isActive()) {
            isActive = 1;
        } else {
            isActive = 0;
        }
        values.put(RssItemsDbSchema.RssChannelsTable.Cols.ACTIVE, isActive);
        return values;
    }

    //TODO вот это в фон
    public ArrayList<RssItem> getItems() {
        ArrayList<RssItem> feedList = null;
        final SQLiteDatabase database;
        try {
            database = this.getWritableDatabase();
            final Cursor cursorTemp = database.query(
                    RssItemsTable.NAME,
                    null, // Columns - null выбирает все столбцы
                    null,
                    null,
                    null, // groupBy
                    null, // having
                    null // orderBy
            );

            feedList = new ArrayList<>();
            final RssCursorWrapper cursor = new RssCursorWrapper(cursorTemp);
            final ArrayList<RssChannel> rssChannels = getChannels();
            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    final String address = cursor.getRssItem().getUrl();
                    for (int i = 0; i < rssChannels.size(); ++i) {
                        if (rssChannels.get(i).getAddress().equals(address) && rssChannels.get(i).isActive()) {
                            feedList.add(cursor.getRssItem());
                            break;
                        }
                    }
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            cursorTemp.close();
        } catch (final Throwable e) {
            logger.log(Level.WARNING, "Не смогли получить items");
        }
        return feedList;
    }

    //TODO вот это тоже в фон
    public ArrayList<RssChannel> getChannels() {
        final SQLiteDatabase database;
        ArrayList<RssChannel> channelList = null;
        try {
            database = this.getWritableDatabase();
            final Cursor cursorTemp = database.query(
                    RssItemsDbSchema.RssChannelsTable.NAME,
                    null, // Columns - null выбирает все столбцы
                    null,
                    null,
                    null, // groupBy
                    null, // having
                    null // orderBy
            );

            channelList = new ArrayList<>();
            final RssCursorWrapper cursor = new RssCursorWrapper(cursorTemp);

            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    channelList.add(cursor.getRssChannel());
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            cursorTemp.close();
        } catch (final Throwable e) {
            logger.log(Level.WARNING, "Не смогли получить channels");
        }

        return channelList;
    }


}
