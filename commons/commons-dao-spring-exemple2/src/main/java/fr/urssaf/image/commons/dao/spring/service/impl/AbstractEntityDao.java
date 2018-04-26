package fr.urssaf.image.commons.dao.spring.service.impl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe abstraite pour les implémentations DAO
 *
 * @param <P> la classe de l'entité persistante
 */
@Transactional(propagation=Propagation.SUPPORTS)
public abstract class AbstractEntityDao<P> extends HibernateDaoSupport{

	private final Class<P> table;
	
	
	/**
	 * Constructeur
	 * 
	 * @param sessionFactory la fabrique de sessions d'Hibernate
	 * @param table la classe de l'entité persistante
	 */
	public AbstractEntityDao(SessionFactory sessionFactory,Class<P> table){
		super();
		setSessionFactory(sessionFactory);
		this.table = table;
	}
	
	
	protected final Criteria getCriteria(){
	   return this.getSession().createCriteria(this.table);
	}
	
	
	protected final Class<P> getTable() {
	   return this.table;
	}

}
