package fr.urssaf.image.commons.util.date;

import java.util.Calendar;
import java.util.Date;


/**
 * Méthodes utilitaires pour les dates
 */
public final class DateUtil {

	private DateUtil() {

	}

	
	/**
	 * Renvoie la <u>date</u> du jour (l'heure est positionnée à minuit)
	 * 
	 * @return la date du jour
	 */
	public static Date today() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}

	
	/**
    * Renvoie la <u>date</u> du lendemain (l'heure est positionnée à minuit)
    * 
    * @return la date du lendemain
    */
	public static Date tomorrow() {

		return today(1);
	}

	
	/**
    * Renvoie la <u>date</u> de la veille (l'heure est positionnée à minuit)
    * 
    * @return la date de la veille
    */
	public static Date yesterday() {

		return today(-1);
	}

	
	/**
	 * Renvoie la <u>date</u> du jour incrémentée ou décrémentée du nombre
	 * de jours passés en paramètre (l'heure est positionnée à minuit)
	 * 
	 * @param day le nombre de jours dont il faut incrémenter/décrémenter la date du jour
	 * @return la date du jour décrémentée ou incrémentée du nombre de jours passé en paramètre
	 */
	public static Date today(int day) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today());
		calendar.add(Calendar.DATE, day);

		return calendar.getTime();
	}

}
