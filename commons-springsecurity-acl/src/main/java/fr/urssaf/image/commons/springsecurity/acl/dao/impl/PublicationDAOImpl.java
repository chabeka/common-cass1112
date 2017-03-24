package fr.urssaf.image.commons.springsecurity.acl.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.springsecurity.acl.dao.PublicationDAO;
import fr.urssaf.image.commons.springsecurity.acl.model.Publication;

@Repository
@Transactional(propagation = Propagation.SUPPORTS)
public class PublicationDAOImpl extends HibernateDaoSupport implements
      PublicationDAO {

   private static final String AUTHOR = "author";

   @Autowired
   public PublicationDAOImpl(
         @Qualifier("sessionFactory") SessionFactory sessionFactory) {
      super();
      this.setSessionFactory(sessionFactory);
   }

   @Override
   public Publication find(int identity) {
      Criteria criteria = this.getSession().createCriteria(Publication.class);
      criteria.add(Restrictions.eq("id", identity));
      return (Publication) criteria.uniqueResult();
   }

   @Override
   public List<Publication> find() {
      Criteria criteria = this.getSession().createCriteria(Publication.class);

      @SuppressWarnings("unchecked")
      List<Publication> publications = criteria.list();

      return publications;
   }

   @Override
   public List<Publication> findByAuthor(int idAuthor) {

      Criteria criteria = this.getSession().createCriteria(Publication.class);

      Criteria authorCriteria = criteria.createCriteria(AUTHOR);
      authorCriteria.add(Restrictions.eq("id", idAuthor));

      @SuppressWarnings("unchecked")
      List<Publication> publications = criteria.list();

      return publications;
   }
   
   @Override
   @Transactional
   public void save(Publication publication) {

      this.getSession().persist(publication);

   }

   @Override
   @Transactional
   public void update(Publication publication) {
      this.getSession().update(publication);

   }

}
