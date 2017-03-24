package fr.urssaf.image.commons.dao.spring.exemple.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.exemple.dao.DocumentDao;
import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;

@Repository
@Transactional(propagation = Propagation.SUPPORTS)
@SuppressWarnings( {"PMD.ConsecutiveLiteralAppends" })
public class DocumentDaoImpl extends HibernateDaoSupport implements DocumentDao {

   
   @Autowired
   public DocumentDaoImpl(
         @Qualifier("sessionFactory") SessionFactory sessionFactory) {
      super();
      this.setSessionFactory(sessionFactory);
   }
  
   @Override
   public Document find(Integer identifiant) {
      Criteria criteria = this.getSession().createCriteria(Document.class);
      criteria.add(Restrictions.eq("id", identifiant));

      return (Document) criteria.uniqueResult();
   }
   
   @Override
   public Document findSQL(Integer identifiant) {
      
      StringBuffer sql = new StringBuffer(150);
      sql.append("select doc.id,doc.titre,doc.date,doc.id_auteur,aut.nom ");
      sql.append("from document doc ");
      sql.append("left outer join auteur aut on doc.id_auteur=aut.id ");
      sql.append("where doc.id=:id ");

      Query query = this.getSession().createSQLQuery(sql.toString());
      query.setParameter("id", identifiant);

      Object[] obj = (Object[]) query.uniqueResult();

      return DocumentUtil.getDocument(obj);
   }

   @Override
   public Document get(Integer identifiant) {
      return (Document) this.getSession().get(Document.class, identifiant);
   }

   @Override
   public int count() {
      
      Criteria criteria = this.getSession().createCriteria(Document.class);
      criteria.setProjection(Projections.rowCount());

      return ((Integer) criteria.uniqueResult());
   }

}
