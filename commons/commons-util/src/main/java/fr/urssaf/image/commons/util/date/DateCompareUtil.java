package fr.urssaf.image.commons.util.date;

import java.util.Date;

import fr.urssaf.image.commons.util.comparable.ObjectComparator;


/**
 * Classe de comparaisons de dates
 */
public final class DateCompareUtil {

	
   private static final ObjectComparator<Date> COMPARATOR = new ObjectComparator<Date>();

	
	private DateCompareUtil(){
		
	}
	
	
	/**
	 * Renvoie true si date1 est postérieure à date2, false dans le cas contraire
	 * 
	 * @param date1 la première date
	 * @param date2 la deuxième date
	 * @return true si date1 est postérieur à date2, false dans le cas contraire
	 */
	public static boolean sup(Date date1, Date date2) {
		return COMPARATOR.sup(date1, date2);
	}
	

	/**
	 * Renvoie true si date1 est antérieure à date2, false dans le cas contraire
	 * 
	 * @param date1 la première date
	 * @param date2 la deuxième date
	 * @return true si date1 est antérieure à date2, false dans le cas contraire
	 */
	public static boolean inf(Date date1, Date date2) {
		return COMPARATOR.inf(date1, date2);
	}
	
	
	/**
	 * Renvoie true si la date est dans le futur (postérieure à aujourd'hui minuit)<br>
	 * <br>
    * <b>NB : une date <code>null</code> n'est pas considérée comme étant dans le futur</b>
	 * 
	 * @param date la date
	 * @return true si la date est dans le futur (postérieure à aujourd'hui minuit), false sinon
	 */
	public static boolean futur(Date date) {
		return sup(date, DateUtil.today());
	}
	

	/**
	 * Renvoie true si la date est dans le passé (antérieure à aujourd'hui minuit)<br>
	 * <br>
	 * <b>NB : une date <code>null</code> est considérée comme étant dans le passé</b>
	 * 
	 * @param date la date
	 * @return true si la date est dans le passé (antérieure à aujourd'hui minuit)
	 */
	public static boolean past(Date date) {
		return inf(date, DateUtil.today());
	}
	
}
