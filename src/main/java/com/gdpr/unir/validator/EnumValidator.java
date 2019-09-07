package com.gdpr.unir.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Arrays.stream;

public class EnumValidator implements ConstraintValidator<ValidEnumValue, String> {

    private Class<? extends Enum> enumClass;

    @Override
    public void initialize(final ValidEnumValue constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return stream(enumClass.getEnumConstants())
                .map(Enum::toString)
                .anyMatch(enumValue -> enumValue.equals(value));
    }
}
