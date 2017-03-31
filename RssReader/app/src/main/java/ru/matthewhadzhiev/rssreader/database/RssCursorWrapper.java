package ru.matthewhadzhiev.rssreader.database;


import android.database.Cursor;
import android.database.CursorWrapper;

import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema.RssItemsTable;
import ru.matthewhadzhiev.rssreader.rssworks.RssChannel;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;

final class RssCursorWrapper extends CursorWrapper{

    RssCursorWrapper(final Cursor cursor) {
        super(cursor);
    }

    RssItem getRssItem() {
        final String address = getString(getColumnIndex(RssItemsTable.Cols.ADDRESS));
        final String title = getString(getColumnIndex(RssItemsTable.Cols.TITLE));
        final String link = getString(getColumnIndex(RssItemsTable.Cols.LINK));
        final String description = getString(getColumnIndex(RssItemsTable.Cols.DESCRIPTION));

        final RssItem rssItem = new RssItem(title, link, description);
        rssItem.setUrl(address);

        return rssItem;
    }

    RssItem getRssItemFromAll() {
        final String address = getString(getColumnIndex(RssItemsDbSchema.RssAllItemsTable.Cols.ADDRESS));
        final String title = getString(getColumnIndex(RssItemsDbSchema.RssAllItemsTable.Cols.TITLE));
        final String link = getString(getColumnIndex(RssItemsDbSchema.RssAllItemsTable.Cols.LINK));
        final String description = getString(getColumnIndex(RssItemsDbSchema.RssAllItemsTable.Cols.DESCRIPTION));

        final RssItem rssItem = new RssItem(title, link, description);
        rssItem.setUrl(address);

        return rssItem;
    }

    RssChannel getRssChannel() {
        final String address = getString(getColumnIndex(RssItemsDbSchema.RssChannelsTable.Cols.ADDRESS));
        final int isActiveInt = getInt(getColumnIndex(RssItemsDbSchema.RssChannelsTable.Cols.ACTIVE));
        final boolean isActive;
        isActive = isActiveInt == 1;

        return new RssChannel(address, isActive);
    }
}
