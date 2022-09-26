package com.example.spring_boot_rest_api_project.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {
    @Override
    public boolean isValid(String inn,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (inn.length() == 8 || inn.length() == 19) {
            return inn.matches("^[a-zA-Z][0-9]{8,20}$");
        } else {
            return false;
        }
    }
}