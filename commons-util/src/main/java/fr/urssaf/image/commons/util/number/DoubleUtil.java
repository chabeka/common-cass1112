package fr.urssaf.image.commons.util.number;

import fr.urssaf.image.commons.util.comparable.ObjectComparator;


/**
 * Méthodes utilitaires pour le type Java <code>{@link Double}</code>
 */
public final class DoubleUtil {

   
	private static ObjectComparator<Double> comparator = new ObjectComparator<Double>();

	
	private DoubleUtil() {

	}

	
	/**
    * Renvoie true si num1 est supérieur à num2, false dans le cas contraire
    * 
    * @param num1 le premier Double
    * @param num2 le deuxième Double
    * @return true si num1 est supérieur à num2, false dans le cas contraire
    */
	public static boolean sup(Double num1, Double num2) {
		return comparator.sup(num1, num2);
	}
	

	/**
    * Renvoie true si num1 est inférieur à num2, false dans le cas contraire
    * 
    * @param num1 le premier Double
    * @param num2 le deuxième Double
    * @return true si num1 est inférieur à num2, false dans le cas contraire
    */
	public static boolean inf(Double num1, Double num2) {
		return comparator.inf(num1, num2);
	}
	

	/**
	 * Regarde si un double est compris dans un intervalle
	 * 
	 * @param num le double
	 * @param min la borne inférieure de l'intervalle
	 * @param max la borne supérieure de l'intervalle
	 * @param minClose si true, alors >=, sinon, >
	 * @param maxClose si true, alors <=, sinon, <
	 * @return true si le double est compris dans l'intervalle, false dans le cas contraire
	 */
	public static boolean range(
	      Double num, 
	      Double min, 
	      Double max,
			boolean minClose, 
			boolean maxClose) {
		return comparator.range(num, min, max, minClose, maxClose);
	}

}
