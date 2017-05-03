package ru.golden.validator;

import org.junit.Test;

import ru.golden.validator.fielddata.Validator;

import static org.junit.Assert.assertEquals;

public class ValidatorTest {
    @Test
    public void textValidationIsCorrect() {
        assertEquals(true, new Validator().validate("text", "1232hjh3fhfjr3rh4f**"));
        assertEquals(false, new Validator().validate("text", "1232h"));
        assertEquals(false, new Validator().validate("text", "1232hwweeorjrjrjvevjrejoroewjjeoijevjojrorejofewjoioejid90||!!```//fggd"));
        assertEquals(true, new Validator().validate("TEXT", "raztwotrifourpyat11199**()"));
    }

    @Test
    public void emailValidationCorrect() {
        assertEquals(true, new Validator().validate("EMAIL", "khadmat96@gmail.com"));
        assertEquals(false, new Validator().validate("EMAIL", ".."));
        assertEquals(true, new Validator().validate("email", "111ggggeeee@yandex.ru"));
        assertEquals(false, new Validator().validate("email", "899988@.."));
    }

    @Test
    public void phoneValidationCorrect() {
        assertEquals(false, new Validator().validate("PHONE", "fe"));
        assertEquals(true, new Validator().validate("PHONE", "+79139009700"));
        assertEquals(false, new Validator().validate("PHONE", "+7913900700"));
        assertEquals(false, new Validator().validate("phone", "89267438941"));
        assertEquals(true, new Validator().validate("PHONE", "+79840008000"));
        assertEquals(false, new Validator().validate("PHONE", "+7913a009700"));
        assertEquals(false, new Validator().validate("PHONE", "+791390097099"));
        assertEquals(false, new Validator().validate("PHONE", "+79139700"));
        assertEquals(false, new Validator().validate("PHONE", "+89139009700"));
        assertEquals(false, new Validator().validate("PHONE", "+794398799791"));
    }

    @Test
    public void numberValidationCorrect() {
        assertEquals(true, new Validator().validate("NUMBER", "123"));
        assertEquals(true, new Validator().validate("NUMBER", "1234"));
        assertEquals(false, new Validator().validate("NUMBER", ""));
        assertEquals(false, new Validator().validate("NUMBER", "123pe"));
        assertEquals(true, new Validator().validate("NUMBER", "11111"));
        assertEquals(false, new Validator().validate("NUMBER", "123456"));
        assertEquals(false, new Validator().validate("NUMBER", "q"));
        assertEquals(true, new Validator().validate("NUMBER", "00000"));
    }

    @Test
    public void urlValidationCorrect() {
        assertEquals(true, new Validator().validate("url", "http://www.mocky.io/v2/58fa10ce110000b81ad2106c"));
        assertEquals(true, new Validator().validate("url", "https://github.com/goldenluk"));
        assertEquals(true, new Validator().validate("url", "http://docs.oracle.com/javase/8/"));
        assertEquals(true, new Validator().validate("url", "https://developer.android.com/reference/packages.html"));
        assertEquals(true, new Validator().validate("url", "http://www.drom.ru"));
        assertEquals(true, new Validator().validate("url", "https://www.yandex.ru"));
        assertEquals(false, new Validator().validate("url", "1111"));
        assertEquals(false, new Validator().validate("url", "1111@frgerg/re'g./fr"));
        assertEquals(false, new Validator().validate("url", "ps://www.yandex.ru"));
        assertEquals(false, new Validator().validate("url", "https:/www.yandex.ru"));
    }
}
