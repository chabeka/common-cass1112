package fr.urssaf.image.commons.dao.spring.service.impl;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.service.EntityModifyDao;

/**
 * Implémentation standard de l'interface DAO EntityModifyDao
 *
 * @param <P> classe de l'entité persistée
 */
public final class EntityModifyDaoImpl<P>
   extends
      HibernateDaoSupport
   implements
      EntityModifyDao<P>
{

   /**
    * Constructeur
    * 
    * @param sessionFactory la fabrique de session d'Hibernate
    */
	public EntityModifyDaoImpl(SessionFactory sessionFactory){
		super();
		setSessionFactory(sessionFactory);
	}
	
	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void delete(P obj) {
		this.getSession().delete(obj);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void save(P obj) {
		this.getSession().save(obj);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void update(P obj) {
		this.getSession().update(obj);
	}


}
