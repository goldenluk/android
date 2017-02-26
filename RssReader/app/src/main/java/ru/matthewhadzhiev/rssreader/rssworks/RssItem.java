package ru.matthewhadzhiev.rssreader.rssworks;


final public class RssItem {
    private String title;
    private String link;
    private String description;

    String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }


    public RssItem(final String title, final String link, final String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }
}
