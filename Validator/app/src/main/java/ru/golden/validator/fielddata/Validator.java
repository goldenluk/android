package ru.golden.validator.fielddata;

public final class Validator {
    public  boolean validate (final String type, final String value) {
        switch (type.toLowerCase()) {
            case "text":
                return validateText(value);
            default:
                return false;
        }
    }

    private boolean validateText (final String value) {
        return value.length() >= 10 && value.length() <= 30;
    }
}
