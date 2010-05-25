package fr.urssaf.image.commons.controller.spring.formulaire.type;

import java.util.ArrayList;

import fr.urssaf.image.commons.controller.spring.formulaire.support.exception.FormulaireException;
import fr.urssaf.image.commons.controller.spring.formulaire.support.exception.TypeFormulaireException;

public class IntType extends AbstractObjectType<Integer> {

	@Override
	public Integer getNotEmptyObject(String valeur) throws TypeFormulaireException {
		try {
			return Integer.parseInt(valeur);
		} catch (NumberFormatException e) {
			ArrayList<Object> valeurs = new ArrayList<Object>();
			valeurs.add(valeur);
			throw new TypeFormulaireException(valeur, Integer.class,
					new FormulaireException(valeurs, "exception.integer"));
		}
	}

	public String getValue(Integer object) {
		return Integer.toString(object);
	}

}
