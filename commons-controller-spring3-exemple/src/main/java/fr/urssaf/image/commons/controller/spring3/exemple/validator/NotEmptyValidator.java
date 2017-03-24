package fr.urssaf.image.commons.controller.spring3.exemple.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		return StringUtils.hasText(value);
	}

	@Override
	public void initialize(NotEmpty parametres) {
		//rien à initialiser
	}

}
