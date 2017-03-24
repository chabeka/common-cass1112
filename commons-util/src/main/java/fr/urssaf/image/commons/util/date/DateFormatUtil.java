package fr.urssaf.image.commons.util.date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Méthodes de formatage de dates
 */
public final class DateFormatUtil {

   
	private DateFormatUtil() {

	}

	
	/**
	 * Renvoie le format de date JJ/MM/AAAA
	 * 
	 * @return le format de date JJ/MM/AAAA
	 */
	public static SimpleDateFormat getFormatFR() {
		synchronized (new Object()) {
			return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		}
	}

	
	/**
	 * Formate une date en chaîne de caractères à l'aide du format passé en paramètre
	 * 
	 * @param date la date à formater
	 * @param format le format à utiliser
	 * @return la date formatée en chaîne de caractères
	 */
	public static String date(Date date, SimpleDateFormat format) {

		String libelle = null;

		if ((date != null) && (format != null)) {
			libelle = format.format(date);
		}

		return libelle;
	}

	
	/**
	 * Formate la date passée en paramètre en JJ/MM/AAAA
	 * 
	 * @param date la date
	 * @return la date formatée en JJ/MM/AAAA
	 */
	public static String dateFr(Date date) {
		return date(date, getFormatFR());
	}

	
	/**
	 * Formate la date du jour en JJ/MM/AAAA
	 * 
	 * @return la date du jour formatée en JJ/MM/AAAA
	 */
	public static String todayFr() {
		return getFormatFR().format(new Date());
	}
	

	/**
	 * Formate des dates à l'aide du format passé en paramètre
	 * 
	 * @param dates la liste des dates à formater
	 * @param pattern le format à utiliser
	 * @return collection des dates formatées
	 */
	public static List<String> date(Collection<Date> dates, String pattern) {

	   List<String> results = new ArrayList<String>();
	   
		if ((dates != null) && (pattern != null)) {
			
		   SimpleDateFormat format = new SimpleDateFormat(
	            pattern, 
	            Locale.getDefault());
		   
		   for (Date date : dates) {
				results.add(date(date, format));
			}

		}
		
		return results;
	}

}
