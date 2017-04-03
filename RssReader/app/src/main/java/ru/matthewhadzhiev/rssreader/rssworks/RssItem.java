package ru.matthewhadzhiev.rssreader.rssworks;


public final class RssItem {
    private final String title;
    private final String link;
    private final String description;
    private boolean isReaded;

    public void setReaded() {
        isReaded = true;
    }

    public boolean isReaded() {
        return isReaded;
    }

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

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getUrl () {
        return url;
    }

    public RssItem(final String title, final String link, final String description, final boolean isReaded) {
        //Проверка на null выполняется при их создании
        this.title = title;
        this.link = link;
        this.description = description;
        this.isReaded = isReaded;
    }
}
