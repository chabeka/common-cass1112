package fr.urssaf.image.commons.controller.spring3.exemple.resource;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

import fr.urssaf.image.commons.controller.spring3.exemple.modele.Etat;


public class EtatEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {

		if (StringUtils.hasText(text)) {

			try {

				setValue(Etat.valueOf(text));
			} catch (IllegalArgumentException e) {

				throw new IllegalArgumentException("Could not parse etat: "
						+ e.getMessage(), e);

			}
		}

		setValue(null);
	}

	@Override
	public String getAsText() {
		Etat etat = (Etat) getValue();
		
		String asText = "";
		if(etat != null){
			asText = etat.name();
		}
		return asText;
	}

}
