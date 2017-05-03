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
}
