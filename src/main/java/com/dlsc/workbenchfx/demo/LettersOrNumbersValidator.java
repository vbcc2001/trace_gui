package com.dlsc.workbenchfx.demo;
import com.dlsc.formsfx.model.validators.CustomValidator;

public class LettersOrNumbersValidator extends CustomValidator<String> {

    private LettersOrNumbersValidator(int min, int max, String errorMessage) {
        super(input -> input.matches("^[0-9a-zA-Z]+$") && (input.length() >= min && input.length() <= max), errorMessage);
    }
    @Deprecated
    private LettersOrNumbersValidator(String errorMessage) {
        super(input -> input.matches("^[0-9a-zA-Z]+$"), errorMessage);
    }
    @Deprecated
    public static LettersOrNumbersValidator justLettersOrNumber(String errorMessage) {
        return new LettersOrNumbersValidator(errorMessage);
    }

    public static LettersOrNumbersValidator exactly(int num, String errorMessage) {
        return new LettersOrNumbersValidator(num,num,errorMessage);
    }

    public static LettersOrNumbersValidator between(int min, int max, String errorMessage) {
        if (min < 0) {
            throw new IllegalArgumentException("Minimum string length cannot be negative.");
        } else if (min > max) {
            throw new IllegalArgumentException("Minimum must not be larger than maximum.");
        }
        return new LettersOrNumbersValidator(min,max,errorMessage);
    }


}

