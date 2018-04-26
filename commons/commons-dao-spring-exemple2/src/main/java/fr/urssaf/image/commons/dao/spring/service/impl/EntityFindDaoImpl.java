package fr.urssaf.image.commons.dao.spring.service.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import fr.urssaf.image.commons.dao.spring.service.EntityFindDao;
import fr.urssaf.image.commons.dao.spring.util.CriteriaUtil;


/**
 * Implémentation standard de l'interface DAO EntityFindDao
 *
 * @param <P> la classe de l'entité persistante
 */
public final class EntityFindDaoImpl<P>
   extends 
      AbstractEntityDao<P>
   implements
		EntityFindDao<P>
{

   /**
    * Constructeur
    * 
    * @param sessionFactory la fabrique de session d'Hibernate
    * @param table la classe de l'entité persistante
    */
	public EntityFindDaoImpl(SessionFactory sessionFactory, Class<P> table) {
		super(sessionFactory, table);
	}

	
	@Override
	public List<P> find(
	      int firstResult, 
	      int maxResult, 
	      String order,
			boolean inverse) {
		Criteria criteria = this.getCriteria();
		return this.find(criteria, firstResult, maxResult, order, inverse);
	}

	
	@Override
	public List<P> find(
	      String order, 
	      boolean inverse) {
	   Criteria criteria = this.getCriteria();
	   CriteriaUtil.order(criteria, order, inverse);
	   return this.find(criteria);
	}

	
	@Override
	public List<P> find(String order) {
		return this.find(order, false);

	}

	
	@Override
	public List<P> find() {
		Criteria criteria = this.getCriteria();
		return this.find(criteria);
	}

	
	/**
    * Renvoie tous les objets persistants sélectionnés par une requête Criteria
    * 
    * @param criteria requête criteria
    * @return liste des objets répondants aux critères
    */
   @SuppressWarnings("unchecked")
   public List<P> find(Criteria criteria) {
      return criteria.list();
   }
	
	
	/**
	 * Renvoie tous les objets persistants sélectionnés par une requête Criteria,
	 * triés et filtrés selon un intervalle
	 * 
	 * @param criteria requête criteria
	 * @param firstResult index de la première entité à lire de la persistance 
    * (les index commencent à 0)
    * @param maxResult nombre maximal de résultat
    * @param order nom de la propriété sur laquelle il faut trier
    * @param inverse true s'il faut trier de manière décroissante
	 * @return liste des objets répondants aux critères
	 */
	public List<P> find(
	      Criteria criteria, 
	      int firstResult, 
	      int maxResult,
			String order, 
			boolean inverse) {
		CriteriaUtil.order(criteria, order, inverse);
		return this.find(criteria, firstResult, maxResult);

	}

	
	/**
	 * Renvoie tous les objets persistants sélectionnés par une requête Criteria,
	 * et triés
	 * 
	 * @param criteria requête criteria
	 * @param order nom de la propriété sur laquelle il faut trier
    * @param inverse true s'il faut trier de manière décroissante
	 * @return liste des objets répondants aux critères
	 */
	public List<P> find(
	      Criteria criteria, 
	      String order, 
	      boolean inverse) {
		CriteriaUtil.order(criteria, order, inverse);
		return this.find(criteria);
	}
	

	/**
	 * Renvoie tous les objets persistants sélectionnés par une requête Criteria,
    * filtrés selon un intervalle
	 * 
	 * @param criteria requête criteria
	 * @param firstResult index de la première entité à lire de la persistance 
    * (les index commencent à 0)
    * @param maxResult nombre maximal de résultat
	 * @return liste des objets répondants aux critères
	 */
	public List<P> find(
	      Criteria criteria, 
	      int firstResult, 
	      int maxResult) {
		CriteriaUtil.resultats(criteria, firstResult, maxResult);
		return find(criteria);
	}


}
