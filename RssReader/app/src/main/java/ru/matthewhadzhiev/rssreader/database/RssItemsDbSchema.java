package ru.matthewhadzhiev.rssreader.database;


public final class RssItemsDbSchema {
    public static final class RssItemsTable {
        public static final String NAME = "items";

        public static final class Cols {
            public static final String ADDRESS = "address";
            public static final String TITLE = "title";
            public static final String LINK = "link";
            public static final String DESCRIPTION = "description";
        }
    }

    public static final class RssChannelsTable {
        public static final String NAME = "channels";

        public static final class Cols {
            public static final String ADDRESS = "address";
            public static final String ACTIVE = "active";
        }
    }
}
