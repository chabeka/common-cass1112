package fr.urssaf.image.commons.util.number;

import fr.urssaf.image.commons.util.comparable.ObjectComparator;

/**
 * Méthodes utilitaires pour le type Java <code>{@link Integer}</code>
 */
public final class IntegerUtil{

   
	private static ObjectComparator<Integer> comparator = new ObjectComparator<Integer>();

	
	private IntegerUtil(){
		
	}
	
	
	/**
    * Renvoie true si num1 est supérieur à num2, false dans le cas contraire
    * 
    * @param num1 le premier Integer
    * @param num2 le deuxième Integer
    * @return true si num1 est supérieur à num2, false dans le cas contraire
    */
	public static boolean sup(Integer num1,Integer num2) {
		return comparator.sup(num1, num2);
	}
	
	
	/**
    * Renvoie true si num1 est inférieur à num2, false dans le cas contraire
    * 
    * @param num1 le premier Integer
    * @param num2 le deuxième Integer
    * @return true si num1 est inférieur à num2, false dans le cas contraire
    */
	public static boolean inf(Integer num1,Integer num2) {
		return comparator.inf(num1, num2);
	}
	
	
	/**
	 * Renvoie true si le nombre est positif
	 * 
	 * @param num le nombre à tester
	 * @return true si le nombre est positif, false dans le cas contraire 
	 */
	public static boolean positif(Integer num){
		return comparator.sup(num,0);
	}
	
	
	/**
	 * Renvoie true si le nombre est négatif
	 * 
	 * @param num le nombre à tester
	 * @return true si le nombre est négatif, false dans le cas contraire
	 */
	public static boolean negatif(Integer num){
		return comparator.inf(num,0);
	}
	
	
}
