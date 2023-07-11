package com.dlsc.workbenchfx.demo;
import com.dlsc.formsfx.model.validators.CustomValidator;

public class NumbersValidator extends CustomValidator<String> {

    private NumbersValidator(String errorMessage) {
        super(input -> input.matches("^[0-9]+$"), errorMessage);
    }

    public static NumbersValidator justNumber(String errorMessage) {
        return new NumbersValidator(errorMessage);
    }

}

