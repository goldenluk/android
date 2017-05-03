package ru.golden.validator.fielddata;

import java.io.Serializable;

public final class Field implements Serializable{
    private int id;
    //Эти поля нужны. Они заполняются, когда парсим из json
    private String type;
    private String placeholder;
    private String default_value;
    private Boolean isValid = null;
    private String value;

    String getValue() {
        return value;
    }

    void setValue(final String value) {
        this.value = value;
    }

    void initValue() {
        if (default_value == null) {
            value = "";
        } else {
            value = default_value;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    String getType() {
        return type;
    }

    String getPlaceholder() {
        return placeholder;
    }

    String getDefaultValue() {
        return default_value;
    }

    Boolean getValid() {
        return isValid;
    }

    Boolean setValid() {
        isValid = new Validator().validate(type, value);
        return isValid;
    }
}
