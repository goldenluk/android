package ru.golden.flickrviewer.items;

import android.net.Uri;

import java.io.Serializable;

public final class PhotoItem implements Serializable {
    private String caption;
    private String id;
    private String url;
    private String owner;

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public Uri getPhotoPageUri() {
        return Uri.parse("http://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(owner)
                .appendPath(id)
                .build();
    }


    public void setCaption(final String caption) {
        this.caption = caption;
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
