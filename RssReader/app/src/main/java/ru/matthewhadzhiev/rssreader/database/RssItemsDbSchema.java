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

    /*
        Таблица для хранения всех итемов. Она возникла из-за ошибки проектирования.
         Можно было по другому спроектировать скорее всего, но до дедлайна 2 недели
     */
    public static final class RssAllItemsTable {
        public static final String NAME = "all_items";

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
