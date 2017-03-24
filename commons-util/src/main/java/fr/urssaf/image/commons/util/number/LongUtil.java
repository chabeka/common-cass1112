package fr.urssaf.image.commons.util.number;

import fr.urssaf.image.commons.util.comparable.ObjectComparator;


/**
 * Méthodes utilitaires pour le type Java <code>{@link Long}</code>
 */
public final class LongUtil{

   
	private static ObjectComparator<Long> comparator = new ObjectComparator<Long>();

	
	private LongUtil(){
		
	}
	
	
	/**
    * Renvoie true si num1 est supérieur à num2, false dans le cas contraire
    * 
    * @param num1 le premier Long
    * @param num2 le deuxième Long
    * @return true si num1 est supérieur à num2, false dans le cas contraire
    */
	public static boolean sup(Long num1,Long num2) {
		return comparator.sup(num1, num2);
	}
	
	
	/**
    * Renvoie true si num1 est inférieur à num2, false dans le cas contraire
    * 
    * @param num1 le premier Long
    * @param num2 le deuxième Long
    * @return true si num1 est inférieur à num2, false dans le cas contraire
    */
	public static boolean inf(Long num1,Long num2) {
		return comparator.inf(num1, num2);
	}
	
	
}
