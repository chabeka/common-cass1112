package fr.urssaf.image.commons.dao.spring.service.impl;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import fr.urssaf.image.commons.dao.spring.service.EntityIdDao;

/**
 * 
 * Implémentation standard de l'interface DAO EntityIdDao
 *
 * @param <P> classe de l'entité persistée
 * @param <I> type Java de l'identifiant de la classe de l'entité persistée
 */
public final class EntityIdDaoImpl<P, I extends Serializable>
   extends
      AbstractEntityDao<P>
   implements
		EntityIdDao<P, I>
{

	private final String identifiant;
	

	/**
    * Constructeur
    * 
    * @param sessionFactory la fabrique de session d'Hibernate
    * @param table la classe de l'entité persistante
    * @param identifiant nom de la propriété représentant l'identifiant unique 
    * de l'entité persistante
    */
	public EntityIdDaoImpl(
	      SessionFactory sessionFactory, 
	      Class<P> table, 
	      String identifiant) {
		super(sessionFactory,table);
		this.identifiant = identifiant;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public P find(I identifiant) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq(this.identifiant, identifiant));

		return (P) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public P get(I identifiant) {
		return (P) this.getSession().get(this.getTable(), identifiant);
	}

}
