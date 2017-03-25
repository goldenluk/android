package ru.matthewhadzhiev.rssreader.database;


public final class RssItemsDbSchema {
    public static final class RssItemsTable {
        public static final String NAME = "items";

        public static final class Cols {
            public static final String ADDRESS = "address";
            static final String TITLE = "title";
            static final String LINK = "link";
            static final String DESCRIPTION = "description";
        }
    }

    public static final class RssChannelsTable {
        public static final String NAME = "channels";

        public static final class Cols {
            public static final String ADDRESS = "address";
            static final String ACTIVE = "active";
        }
    }

    private RssItemsDbSchema () {
        throw new UnsupportedOperationException();
    }
}
