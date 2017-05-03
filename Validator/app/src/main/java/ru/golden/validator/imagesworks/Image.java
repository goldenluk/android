package ru.golden.validator.imagesworks;

import java.io.Serializable;

final class Image implements Serializable {
    private int albumId;
    private int id;
    private String title;
    private String url;
    private String thumbnailUrl;

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(final int albumId) {
        this.albumId = albumId;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(final String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
