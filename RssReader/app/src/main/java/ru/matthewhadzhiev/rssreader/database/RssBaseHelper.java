package ru.matthewhadzhiev.rssreader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema.RssItemsTable;

public final class RssBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "rssItemsBase.db";

    public RssBaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
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
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {

    }
}
