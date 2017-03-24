package fr.urssaf.image.commons.dao.spring.exemple.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.exemple.dao.DocumentModifyDao;
import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;

@Repository
@Transactional(propagation = Propagation.SUPPORTS)
@SuppressWarnings( {"PMD.ConsecutiveLiteralAppends" })
public class DocumentModifyDaoImpl extends HibernateDaoSupport implements DocumentModifyDao {

   /**
    * @param sessionFactory
    */
   @Autowired
   public DocumentModifyDaoImpl(
         @Qualifier("sessionFactory") SessionFactory sessionFactory) {
      super();
      this.setSessionFactory(sessionFactory);
   }
   
   private static final String AUTEUR = "auteur";
   private static final String TITRE = "titre";
   private static final String DATE = "date";

   @Override
   @Transactional(propagation = Propagation.MANDATORY)
   public void delete(Document document) {
     this.getSession().delete(document);
      
   }

   @Override
   @Transactional(propagation = Propagation.MANDATORY)
   public void save(Document document) {
      this.getSession().save(document);
      
   }

   @Override
   @Transactional(propagation = Propagation.MANDATORY)
   public void update(Document document) {
      this.getSession().update(document);
   }
   
   @Override
   @Transactional(propagation = Propagation.MANDATORY)
   public void saveSQL(Document document) {

      StringBuffer sql = new StringBuffer(100);
      sql.append("insert into document ");
      sql.append("(id_auteur, titre, date) ");
      sql.append("values (:auteur, :titre, :date) ");

      Query query = this.getSession().createSQLQuery(sql.toString());
      query.setParameter(TITRE, document.getTitre());
      query.setParameter(DATE, document.getDate());
      if (document.getAuteur() == null) {
         query.setParameter(AUTEUR, null);
      } else {
         query.setParameter(AUTEUR, document.getAuteur().getId());
      }

      query.executeUpdate();

      Integer idUnique = (Integer) this.getSession().createSQLQuery(
            "CALL IDENTITY();").list().iterator().next();

      document.setId(idUnique.intValue());
   }

   @Override
   @Transactional(propagation = Propagation.MANDATORY)
   public void updateSQL(Document document) {

      StringBuffer sql = new StringBuffer(100);
      sql.append("update document ");
      sql.append("set ");
      sql.append("id_auteur =:auteur, ");
      sql.append("titre =:titre, ");
      sql.append("date =:date ");
      sql.append("where id =:id ");

      Query query = this.getSession().createSQLQuery(sql.toString());
      query.setParameter(TITRE, document.getTitre());
      query.setParameter(DATE, document.getDate());
      if (document.getAuteur() == null) {
         query.setParameter(AUTEUR, null);
      } else {
         query.setParameter(AUTEUR, document.getAuteur().getId());
      }
      query.setParameter("id", document.getId());

      query.executeUpdate();

   }

   @Override
   @Transactional(propagation = Propagation.MANDATORY)
   public void deleteSQL(Document document) {

      String sql = "delete from document where id =:id";

      Query query = this.getSession().createSQLQuery(sql);
      query.setParameter("id", document.getId());

      query.executeUpdate();

   }


}
