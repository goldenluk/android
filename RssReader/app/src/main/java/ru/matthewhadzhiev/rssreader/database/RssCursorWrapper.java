package ru.matthewhadzhiev.rssreader.database;


import android.database.Cursor;
import android.database.CursorWrapper;

import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema.RssItemsTable;
import ru.matthewhadzhiev.rssreader.rssworks.RssChannel;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;

public final class RssCursorWrapper extends CursorWrapper{

    public RssCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public RssItem getRssItem() {
        final String address = getString(getColumnIndex(RssItemsTable.Cols.ADDRESS));
        final String title = getString(getColumnIndex(RssItemsTable.Cols.TITLE));
        final String link = getString(getColumnIndex(RssItemsTable.Cols.LINK));
        final String description = getString(getColumnIndex(RssItemsTable.Cols.DESCRIPTION));

        final RssItem rssItem = new RssItem(title, link, description);
        rssItem.setUrl(address);

        return rssItem;
    }

    public RssChannel getRssChannel() {
        final String address = getString(getColumnIndex(RssItemsDbSchema.RssChannelsTable.Cols.ADDRESS));
        final int isActiveInt = getInt(getColumnIndex(RssItemsDbSchema.RssChannelsTable.Cols.ACTIVE));
        final boolean isActive;
        isActive = isActiveInt == 1;

        return new RssChannel(address, isActive);
    }
}
