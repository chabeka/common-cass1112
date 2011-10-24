package fr.urssaf.image.sae.metadata.constants;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Cette classe contient la liste des constantes utilisées dans l'application.
 * 
 * @author akenore.
 */
public final class Constants {
	// Numérique pattern.
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	// ici la date est comprise entre 1900-01-01 et 2099-12-31 et doit être de
	// la syntaxe yyyy-mm-dd
	public static final String DATE_PATTERN = "(19|20)\\d\\d[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])";
	// Numérique pattern.
	public static final String NUMERIC_PATTERN = "[0-9]*";
	// Numérique pattern.
	public static final String STRING_PATTERN = "[A-Za-z0-9]*";
	// Numérique pattern.
	public static final String BOOLEAN_PATTERN = "true|false";
	// le local
	public static final Locale DEFAULT_LOCAL = Locale.FRENCH;
	/** encoding de lecture **/
	public static final Charset ENCODING = Charset.forName("UTF-8");
	// Message par défaut
	@SuppressWarnings("PMD.LongVariable")
	public static final String NO_MESSAGE_FOR_THIS_KEY = "Pas de méssage correspondant à cette clé";

	/** Cette classe n'est pas faite pour être instanciée. */
	private Constants() {
		assert false;
	}

}
