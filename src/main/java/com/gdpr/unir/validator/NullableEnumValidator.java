package com.gdpr.unir.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Arrays.stream;

public class NullableEnumValidator implements ConstraintValidator<ValidNullableEnumValue, String> {

    private Class<? extends Enum> enumClass;

    @Override
    public void initialize(final ValidNullableEnumValue constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return stream(enumClass.getEnumConstants())
                .map(Enum::toString)
                .anyMatch(enumValue -> enumValue.equals(value));
    }
}
