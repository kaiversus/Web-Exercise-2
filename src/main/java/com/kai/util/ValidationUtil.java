package com.kai.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidationUtil {

	private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
	private static final Validator VALIDATOR = FACTORY.getValidator();

	public static <T> Map<String, String> validate(T target) {
		Map<String, String> errors = new LinkedHashMap<>();
		Set<ConstraintViolation<T>> violations = VALIDATOR.validate(target);
		for (ConstraintViolation<T> v : violations) {
			String field = v.getPropertyPath().toString();
			errors.putIfAbsent(field, v.getMessage());
		}
		return errors;
	}
}
