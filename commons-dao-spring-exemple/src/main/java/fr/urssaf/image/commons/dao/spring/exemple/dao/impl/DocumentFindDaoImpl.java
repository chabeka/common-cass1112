package fr.urssaf.image.commons.dao.spring.exemple.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.exemple.dao.DocumentFindDao;
import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;

@Repository
@Transactional(propagation = Propagation.SUPPORTS)
@SuppressWarnings( {"PMD.ConsecutiveLiteralAppends" })
public class DocumentFindDaoImpl extends HibernateDaoSupport implements DocumentFindDao {

  
   /**
    * @param sessionFactory
    */
   @Autowired
   public DocumentFindDaoImpl(
         @Qualifier("sessionFactory") SessionFactory sessionFactory) {
      super();
      this.setSessionFactory(sessionFactory);
   }
   
   private static final Logger LOGGER = Logger.getLogger(DocumentFindDaoImpl.class);

   private static final String AUTEUR = "auteur";
   private static final String TITRE = "titre";
   private static final String DATE = "date";

   private static final String UNCHECKED = "unchecked";
   
   @SuppressWarnings(UNCHECKED)
   @Override
   public List<Document> findByCriteria(int firstResult, int maxResult, String order,
         boolean inverse) {

      Criteria criteria = this.getSession().createCriteria(Document.class);
      criteria.setFetchMode(AUTEUR, FetchMode.JOIN);

      criteria.setMaxResults(maxResult);
      criteria.setFirstResult(firstResult);
      
      if (order != null) {
         if (inverse) {
            criteria.addOrder(Order.desc(order));
         } else {
            criteria.addOrder(Order.asc(order));
         }
      }
      
      return criteria.list();
   }
   
   @SuppressWarnings(UNCHECKED)
    @Override
   public List<Document> findBySQL(int firstResult, int maxResults, String order,
         boolean inverse) {

      String inv = inverse?"desc":"asc";
    
      StringBuffer sql = new StringBuffer(150);
      sql.append("select doc.id,doc.titre,doc.date,doc.id_auteur,aut.nom ");
      sql.append("from document doc ");
      sql.append("left outer join auteur aut on doc.id_auteur=aut.id ");

      sql.append("order by doc." + order + " " + inv + " ");

      // NB :
      // - pour traiter firstResult et maxResults, on se limite à du SQL 92
      // => ne pas utiliser TOP, LIMIT, ...
      // cf. http://troels.arvin.dk/db/rdbms/#select-limit

      Query query = this.getSession().createSQLQuery(sql.toString());

      List<Object[]> objects = (List<Object[]>) query.list();

      List<Document> documents = new ArrayList<Document>();

      int debutCpt = firstResult;
      int finCpt = Math.min(firstResult + maxResults, objects.size());
      for (int i = debutCpt; i <= (finCpt - 1); i++) {
         documents.add(DocumentUtil.getDocument(objects.get(i)));
      }

      return documents;
   }



   @SuppressWarnings(UNCHECKED)
   @Override
   public List<Document> findByCriteria() {

      Criteria criteria = this.getSession().createCriteria(Document.class);
      criteria.add(Restrictions.gt(DATE, new Date()));

      Criteria criteriaAuteur = criteria.createCriteria(AUTEUR);
      criteriaAuteur.add(Restrictions.in("nom", new Object[] { "auteur 1",
            "auteur 0" }));

      criteria.addOrder(Order.asc(TITRE));

      return criteria.list();
   }

   @SuppressWarnings(UNCHECKED)
   @Override
   public List<Document> findBySQL() {

      StringBuffer sql = new StringBuffer(200);
      sql.append("select doc.id,doc.titre,doc.date,doc.id_auteur,aut.nom ");
      sql.append("from document doc ");
      sql.append("left outer join auteur aut on doc.id_auteur=aut.id ");
      sql.append("where doc.date > :date ");
      sql.append("and aut.nom in (:nom) ");

      sql.append("order by doc.titre asc ");

      Query query = this.getSession().createSQLQuery(sql.toString());
      query.setParameter(DATE, new Date());
      query.setParameterList("nom", new Object[] { "auteur 1", "auteur 0" });

      List<Object[]> objects = (List<Object[]>) query.list();

      List<Document> documents = new ArrayList<Document>();

      for (Object[] obj : objects) {
         documents.add(DocumentUtil.getDocument(obj));
      }

      return documents;
   }

   @SuppressWarnings(UNCHECKED)
   @Override
   public List<Document> findByHQL() {

      StringBuffer hql = new StringBuffer(150);
      hql.append("select aut,doc ");
      hql.append("from Document doc left outer join doc.auteur aut ");
      hql.append("where date > :date ");
      hql.append("and aut.nom in (:nom) ");
      hql.append("order by doc.titre asc");

      Query query = this.getSession().createQuery(hql.toString());
      query.setParameter(DATE, new Date());
      query.setParameterList("nom", new Object[] { "auteur 1", "auteur 0" });

      query.setResultTransformer(Criteria.ROOT_ENTITY);

      return query.list();
   }

   @Override
   public void scroll() {

      Criteria criteria = this.getSession().createCriteria(Document.class);

      ScrollableResults scroll = criteria.scroll();

      while (scroll.next()) {

         Document doc = (Document) scroll.get(0);

         if (scroll.getRowNumber() % 10000 == 0) {
            LOGGER.debug(scroll.getRowNumber());
         }
         this.getSession().evict(doc);

      }
      scroll.close();

   }


   @SuppressWarnings(UNCHECKED)
   @Override
   public List<Document> loadEtatsByCriteria() {
      
      Criteria criteria = this.getSession().createCriteria(Document.class);
      Criteria criteriaEtat = criteria.createCriteria("etats",
            Criteria.LEFT_JOIN);
      criteriaEtat.add(Restrictions.ne("etat", "close"));
      criteria.addOrder(Order.asc(TITRE));

      criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

      return criteria.list();
   }
   
   @SuppressWarnings(UNCHECKED)
   @Override
   public List<Document> loadEtatsByInitialize() {

      Criteria criteria = this.getSession().createCriteria(Document.class);
      
      List<Document> documents = criteria.list();

      for (Document document : documents) {
         Hibernate.initialize(document.getEtats());
      }

      return documents;
   }


   @SuppressWarnings(UNCHECKED)
   @Override
   public List<Document> loadEtatsByHQL() {
      
      StringBuffer hql = new StringBuffer(120);
      hql.append("select doc ");
      hql.append("from Document doc join fetch doc.etats etat ");
      hql.append("where etat.etat != 'close'");
      hql.append("order by doc.titre asc");

      Query query = this.getSession().createQuery(hql.toString());

      query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

      return query.list();
   }
   
   

}
