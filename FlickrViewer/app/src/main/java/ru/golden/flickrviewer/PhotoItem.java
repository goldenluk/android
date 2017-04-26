package ru.golden.flickrviewer;

import java.io.Serializable;

public final class PhotoItem implements Serializable {
    private String caption;
    private String id;
    private String url;

    public String getCaption() {
        return caption;
    }

    public void setCaption(final String caption) {
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return caption;
    }
}
