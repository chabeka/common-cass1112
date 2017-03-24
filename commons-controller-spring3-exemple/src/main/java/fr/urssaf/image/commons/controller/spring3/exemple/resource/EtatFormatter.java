package fr.urssaf.image.commons.controller.spring3.exemple.resource;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import fr.urssaf.image.commons.controller.spring3.exemple.modele.Etat;

public class EtatFormatter implements Formatter<Etat> {

	@Override
	public String print(Etat etat, Locale locale) {

		String code = "";
		if (etat != null) {
			switch (etat) {
			case init:
				code = "I";
				break;
			case close:
				code = "C";
				break;
			case open:
				code = "O";
				break;
			default:
				code = "";
				break;
			}
		}

		return code;
	}

	@Override
	public Etat parse(String text, Locale locale) throws ParseException {
		
		Etat etat = null;
		if (StringUtils.hasText(text)) {

			if ("I".equals(text)) {
				etat =  Etat.init;
			}

			else if ("C".equals(text)) {
				etat =  Etat.close;
			}

			else if ("O".equals(text)) {
				etat =  Etat.open;
			}

			else {

				throw new IllegalArgumentException("Could not parse etat: "
						+ text);

			}

		}

		return etat;
	}
}
