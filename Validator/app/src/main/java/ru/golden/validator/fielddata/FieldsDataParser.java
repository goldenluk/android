package ru.golden.validator.fielddata;

import com.google.gson.Gson;

public final class FieldsDataParser {

    public static Field[] parse (final String string) {
        final Gson gson = new Gson();
        try {
            return gson.fromJson(string, Field[].class);
        } catch (final Throwable throwable) {
            return null;
        }
    }

}
