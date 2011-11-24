package fr.urssaf.image.sae.mapping.constants;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Cette classe contient la liste des constantes utilisées dans l'application.
 * 
 * @author akenore, rhofir.
 */
public final class Constants {

	// format de date
	public static final String DATE_PATTERN_FR = "yyyy/MM/dd";
	// format de date iso 8601
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	// le local
	public static final Locale DEFAULT_LOCAL = Locale.FRENCH;
	/** encoding de lecture **/
	public static final Charset ENCODING = Charset.forName("UTF-8");
	/** Constante vide. */
	public static final String BLANK = "";
	// Le local par défaut
	public static final Locale LOCAL = Locale.FRENCH;
	// Message par défaut
	@SuppressWarnings("PMD.LongVariable")
	public static final String NO_MESSAGE_FOR_THIS_KEY = "Pas de méssage correspondant à cette clé";

	/** Cette classe n'est pas faite pour être instanciée. */
	private Constants() {
		assert false;
	}

}
