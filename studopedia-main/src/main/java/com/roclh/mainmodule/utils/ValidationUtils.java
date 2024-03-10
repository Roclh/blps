package com.roclh.mainmodule.utils;

import org.roclh.common.exceptions.DataValidationException;

import java.util.List;
import java.util.function.Predicate;

public class ValidationUtils {
    private static final List<String> specialCharacters = List.of(new String[]{"\\", "[", "]", "^", "$", "|", "*", "+", "(", ")"});

    public static <T> void validate(T value, Predicate<T> func, String message) throws DataValidationException {
        if (func.test(value)) {
            throw new DataValidationException(message);
        }
    }

    //special symbols: [ ] \ ^ $ | * + ( )

    public static boolean containsSpecialCharacters(String text) {
        return specialCharacters.stream().anyMatch(text::contains);
    }
}
