package ru.golden.validator.imagesworks;

import com.google.gson.Gson;

final class ImagesInfoParser {
    static Image[] parse(final String string) {
        final Gson gson = new Gson();
        try {
            return gson.fromJson(string, Image[].class);
        } catch (final Throwable throwable) {
            return null;
        }
    }
}

