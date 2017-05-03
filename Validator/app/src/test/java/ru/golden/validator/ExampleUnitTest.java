package ru.golden.validator;

import org.junit.Test;

import ru.golden.validator.fielddata.Validator;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void textValidationIsCorrect() {
        assertEquals(true, new Validator().validate("text", "1232hjh3fhfjr3rh4f**"));
        assertEquals(false, new Validator().validate("text", "1232h"));
        assertEquals(false, new Validator().validate("text", "1232hwweeorjrjrjvevjrejoroewjjeoijevjojrorejofewjoioejid90||!!```//fggd"));
    }

}