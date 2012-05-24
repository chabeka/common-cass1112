package fr.urssaf.image.sae.anais.portail.validator.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import fr.urssaf.image.sae.anais.portail.validator.NotEmpty;

/**
 * Classe de validation pour la contrainte {@link @NotEmpty}<br>
 * <br>
 * La validation s'appuie sur la méthode {@link StringUtils#hasText(String)}<br>
 *
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {

	@Override
	public final boolean isValid(String value, ConstraintValidatorContext context) {
		
		return StringUtils.hasText(value);
	}

	@Override
	public void initialize(NotEmpty parametres) {
		//rien à initialiser
	}

}
