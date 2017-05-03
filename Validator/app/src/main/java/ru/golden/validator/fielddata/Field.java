package ru.golden.validator.fielddata;

public final class Field {
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

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(final String placeholder) {
        this.placeholder = placeholder;
    }

    public String getDefaultValue() {
        return default_value;
    }

    public void setDefaultValue(final String defaultValue) {
        this.default_value = defaultValue;
    }
}
