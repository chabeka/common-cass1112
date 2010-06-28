package fr.urssaf.image.commons.controller.spring3.exemple.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import fr.urssaf.image.commons.util.number.DoubleUtil;

public class InfValidator implements ConstraintValidator<Inf, Number> {

	private double inf;

	@Override
	public boolean isValid(Number value, ConstraintValidatorContext context) {
		return value != null ? DoubleUtil.inf(value.doubleValue(), inf) : true;
	}

	@Override
	public void initialize(Inf parametres) {
		inf = parametres.borneInf();
	}

}
