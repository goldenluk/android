package ru.golden.validator.fielddata;

import java.io.Serializable;

public final class Field implements Serializable{
    private int id;
    private String type;
    private String placeholder;
    private String default_value;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(final String placeholder) {
        this.placeholder = placeholder;
    }

    String getDefaultValue() {
        return default_value;
    }

    public void setDefaultValue(final String defaultValue) {
        this.default_value = defaultValue;
    }
}
