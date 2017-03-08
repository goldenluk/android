package ru.matthewhadzhiev.rssreader.rssworks;


public final class RssItem {
    private final String title;
    private final String link;
    private final String description;
    private String url;

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl () {
        return url;
    }

    RssItem(final String title, final String link, final String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }
}
