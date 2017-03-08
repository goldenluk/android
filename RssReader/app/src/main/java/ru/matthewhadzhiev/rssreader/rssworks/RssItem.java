package ru.matthewhadzhiev.rssreader.rssworks;


public final class RssItem {
    private final String title;
    private final String link;
    private final String description;

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    RssItem(final String title, final String link, final String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }
}
