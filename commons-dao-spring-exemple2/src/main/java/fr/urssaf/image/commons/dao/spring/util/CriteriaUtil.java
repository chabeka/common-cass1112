package fr.urssaf.image.commons.dao.spring.util;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;


/**
 * Fonctions utilitaires pour Hibernate Criteria
 *
 */
public final class CriteriaUtil {

   
	private CriteriaUtil() {
	}

	/**
	 * Initialise les critères de pagination
	 * 
	 * @param criteria criteria à initialiser
	 * @param firstResult index de la première entité à lire
	 * @param maxResult nombre maximum de résultats
	 */
	public static void resultats(
	      Criteria criteria,
			int firstResult, 
			int maxResult) {
		criteria.setMaxResults(maxResult);
		criteria.setFirstResult(firstResult);
	}

	/**
	 * Initialise les critères de tri
	 * 
	 * @param criteria criteria à initialiser
	 * @param order nom de la propriété sur laquelle il faut trier
	 * @param inverse true s'il faut trier de manière décroissante
	 */
	public static void order(
	      Criteria criteria, 
	      String order,
			boolean inverse) {

		if (order != null) {
			if (inverse) {
			   criteria.addOrder(Order.desc(order));
			} else {
			   criteria.addOrder(Order.asc(order));
			}
		}
	}

}
