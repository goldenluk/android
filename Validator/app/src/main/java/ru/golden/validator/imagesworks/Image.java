package ru.golden.validator.imagesworks;

import java.io.Serializable;

final class Image implements Serializable {
    //Эти поля нужны. Они заполняются, когда парсим из json
    private int albumId;
    private int id;
    private String title;
    private String url;
    private String thumbnailUrl;


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    String getTitle() {
        return title;
    }

    String getUrl() {
        return url;
    }

    String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
