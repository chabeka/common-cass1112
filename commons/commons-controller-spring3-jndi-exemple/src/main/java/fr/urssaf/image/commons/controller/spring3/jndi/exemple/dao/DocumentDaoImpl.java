package fr.urssaf.image.commons.controller.spring3.jndi.exemple.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.controller.spring3.jndi.exemple.modele.Document;

@Repository
public class DocumentDaoImpl extends HibernateDaoSupport implements DocumentDao {

   @Autowired
   public DocumentDaoImpl(
         @Qualifier("sessionFactory") SessionFactory sessionFactory) {
      super();
      this.setSessionFactory(sessionFactory);
   }

   @SuppressWarnings("unchecked")
   public List<Document> allDocuments() {
      Criteria criteria = this.getSession().createCriteria(Document.class);
      return criteria.list();
   }

   @Override
   @Transactional
   public void save(Document document) {
      this.getSession().save(document);

   }

   @Override
   @SuppressWarnings("PMD.ShortVariable") 
   public Document find(int id) {
      Criteria criteria = this.getSession().createCriteria(Document.class);
      criteria.add(Restrictions.eq("id", id));

      return (Document) criteria.uniqueResult();
   }
}
