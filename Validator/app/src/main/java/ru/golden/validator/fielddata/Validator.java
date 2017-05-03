package ru.golden.validator.fielddata;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validator {
    private final Pattern emailRegex =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final Pattern phoneRegex =
            Pattern.compile("^\\+7\\d{10}$");
    private final Pattern numberRegex =
            Pattern.compile("^\\d{1,5}$");
    private final Pattern urlRegex =
            Pattern.compile("^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?");

    public boolean validate(final String type, final String value) {
        switch (type.toLowerCase()) {
            case "text":
                return validateText(value);
            case "email":
                return validateEmail(value);
            case "phone":
                return validatePhone(value);
            case "number":
                return validateNumber(value);
            case "url":
                return validateUrl(value);
            default:
                return false;
        }
    }

    private boolean validateText(final String value) {
        return value.length() >= 10 && value.length() <= 30;
    }

    private boolean validateEmail(final String value) {
        final Matcher matcher = emailRegex.matcher(value);
        return matcher.find();
    }

    private boolean validatePhone(final String value) {
        final Matcher matcher = phoneRegex.matcher(value);
        return matcher.find();
    }

    private boolean validateNumber(final String value) {
        final Matcher matcher = numberRegex.matcher(value);
        return matcher.find();
    }

    private boolean validateUrl(final String value) {
        final Matcher matcher = urlRegex.matcher(value);
        return matcher.find();
    }
}
