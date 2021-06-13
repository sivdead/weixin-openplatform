package com.github.sivdead.wx.open.validator;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Object> {

    private ValidEnum annotation;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value != null) {
            Class<? extends IEnum> enumClass = annotation.enumClass();
            if (enumClass.isEnum()) {
                IEnum[] enums = enumClass.getEnumConstants();
                for (IEnum enumValue : enums) {
                    if (value.equals(enumValue.getIdentifier())) {
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.annotation = constraintAnnotation;
    }
}
