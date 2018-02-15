package fr.urssaf.image.commons.dao.spring.service;

/**
 * Fonctions classiques de lecture d'entités persistées en utilisant
 * l'identifiant unique de l'entité.
 * 
 * @param <P> classe de l'entité persistée
 * @param <I> type Java de l'identifiant de la classe de l'entité persistée
 */
public interface EntityIdDao<P, I> {

   
	/**
	 * Renvoie une entitée persistée.<br> 
	 * Cherche en session puis en base.
	 * 
	 * @param identifiant identifiant de l'entitée persistée
	 * @return entitée persistée
	 */
	P get(I identifiant);

	
	/**
	 * Renvoie une entitée persistée.<br>
	 * Cherche en base.
	 * 
	 * @param identifiant identifiant de l'entitée persistée
	 * @return entitée persistée
	 */
	P find(I identifiant);
	
}
