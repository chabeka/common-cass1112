package fr.urssaf.image.commons.util.comparable;

import java.util.Comparator;


/**
 * Classe générique abstraite de comparaison entre deux éléments<br>
 * <br>
 * Prend en compte les valeurs null<br>
 * <br>
 * <b>NB : Une valeur null est considérée comme strictement inférieure à une valeur non null</b>
 *
 * @param <T> le type des éléments à comparer entre eux
 */
public abstract class AbstractComparator<T> implements Comparator<T> {

	private boolean inverse;

	protected final void setInverse(boolean inverse) {
		this.inverse = inverse;
	}

	protected final boolean isInverse() {
		return this.inverse;
	}

	
	/**
	 * Compare les deux éléments<br>
	 * 
	 * @param arg1 le premier élément de la comparaison
	 * @param arg2 le deuxième élément de la comparaison
	 * 
	 * @return <ul>
	 *            <li>0 si arg1 est "égal" à arg2</li>
	 *            <li>une valeur inférieure à 0 si arg1 est inférieur à arg2</li>
	 *            <li>une valeur supérieure à 0 si arg1 est supérieur à arg2</li>
	 *         </ul>
	 */
	public final int compare(T arg1, T arg2) {

	   int compare = 0;

		if (arg1 != null && arg2 == null) {
			compare = 1;
		} else if (arg1 == null && arg2 == null) {
			compare = 0;
		} else if (arg1 == null && arg2 != null) {
			compare = -1;
		} else {

			compare = compareImpl(arg1, arg2);
		}
		
		if (inverse) {
			compare = -1 * compare;
		}

		return compare;
		
	}

	
	/**
	 * Détermine si arg1 est strictement supérieur à arg2
	 * 
	 * @param arg1 le premier élément de la comparaison
    * @param arg2 le deuxième élément de la comparaison
	 * @return true si arg1 est strictement supérieur à arg2, false dans le cas contraire
	 */
	public final boolean sup(T arg1, T arg2) {
	   return compare(arg1, arg2) > 0; 
	}

	
	/**
    * Détermine si arg1 est strictement inférieur à arg2
    * 
    * @param arg1 le premier élément de la comparaison
    * @param arg2 le deuxième élément de la comparaison
    * @return true si arg1 est strictement inférieur à arg2, false dans le cas contraire
    */
	public final boolean inf(T arg1, T arg2) {
		return compare(arg1, arg2) < 0 ;
	}

	
	/**
	 * Détermine si une valeur est dans un intervalle
	 * 
	 * @param arg la valeur à tester
	 * @param min la borne inférieure de l'intervalle
	 * @param max la borne supérieure de l'intervalle
	 * @param minClose si true, alors >=, sinon, >
    * @param maxClose si true, alors <=, sinon, <
	 * @return true si la valeur est comprise dans l'intervalle, false dans le cas contraire
	 */
	public final boolean range(T arg, T min, T max, boolean minClose, boolean maxClose) {

		return (minClose ? !inf(arg, min) : sup(arg, min))
				&& (maxClose ? !sup(arg, max) : inf(arg, max));
	}

	
	protected abstract int compareImpl(T arg1, T arg2);
	
}
