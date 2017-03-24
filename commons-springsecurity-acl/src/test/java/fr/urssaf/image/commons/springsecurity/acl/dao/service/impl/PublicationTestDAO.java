package fr.urssaf.image.commons.springsecurity.acl.dao.service.impl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.springsecurity.acl.dao.service.TestDAO;
import fr.urssaf.image.commons.springsecurity.acl.model.Publication;

@Repository
@Transactional(propagation = Propagation.SUPPORTS)
public class PublicationTestDAO extends HibernateDaoSupport implements
      TestDAO<Publication, Integer> {

   @Autowired
   public PublicationTestDAO(
         @Qualifier("sessionFactory") SessionFactory sessionFactory) {
      super();
      this.setSessionFactory(sessionFactory);
   }

   @Override
   public Publication findById(Integer identity) {

      Assert.notNull(identity);

      Criteria criteria = this.getSession().createCriteria(Publication.class);
      criteria.add(Restrictions.eq("id", identity));

      return (Publication) criteria.uniqueResult();

   }

}
