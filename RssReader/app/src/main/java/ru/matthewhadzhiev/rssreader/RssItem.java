package ru.matthewhadzhiev.rssreader;


public class RssItem {
    private String mTitle;
    private String mLink;
    private String mDescription;

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    public RssItem(String title, String link, String description) {
        mTitle = title;
        mLink = link;
        mDescription = description;
    }
}
