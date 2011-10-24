package fr.urssaf.image.sae.dfce.admin.utils;

import java.util.Collections;
import java.util.Map;

/**
 * Cette classe contient des méthodes utilitaires
 * 
 * @author akenore
 * 
 */
public final class Utils {
	
	/**
	 * Simplifie l'écriture des boucles foreach quand l'argument peut être
	 * {@code null}.
	 * 
	 * @param <T>
	 *            le type des éléments
	 * @param anIterable
	 *            les éléments à parcourir
	 * @return les éléments, ou une collection vide si l'argument était null
	 */
	@SuppressWarnings("PMD.OnlyOneReturn")
	public static <T> Iterable<T> nullSafeIterable(final Iterable<T> anIterable) {
		if (anIterable == null) {
			return Collections.emptyList();
		} else {
			return anIterable;
		}
	}

	
	/**
	 * Simplifie l'écriture des map
	 * 
	 * @param map
	 *            le type des éléments
	 * @param <K>
	 *            : type
	 * @param <V>
	 *            : type
	 * @return les éléments, ou une map vide si l'argument était null
	 */
	@SuppressWarnings("PMD.OnlyOneReturn")
	public static <K, V> Map<K, V> nullSafeMap(final Map<K, V> map) {
		if (map == null) {
			return Collections.emptyMap();
		} else {
			return map;
		}
	}

	/** Cette classe n'est pas faite pour être instanciée. */
	private Utils() {
		assert false;
	}

}
