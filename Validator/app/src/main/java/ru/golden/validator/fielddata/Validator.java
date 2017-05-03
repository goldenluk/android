package ru.golden.validator.fielddata;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validator {
    public boolean validate(final String type, final String value) {
        switch (type.toLowerCase()) {
            case "text":
                return validateText(value);
            case "email":
                return validateEmail(value);
            case "phone":
                return validatePhone(value);
            default:
                return false;
        }
    }

    private boolean validateText(final String value) {
        return value.length() >= 10 && value.length() <= 30;
    }

    private boolean validateEmail(final String value) {
        final Pattern emailRegex =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        final Matcher matcher = emailRegex.matcher(value);
        return matcher.find();
    }

    private boolean validatePhone(final String value) {
        final Pattern phoneRegex =
                Pattern.compile("^[+][7][0-9]{10}$");
        final Matcher matcher = phoneRegex.matcher(value);
        return matcher.find();
    }
}
