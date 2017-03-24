package fr.urssaf.image.commons.dao.spring.dao.fixture;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.support.MyHibernateDaoSupport;


/**
 * Implémentation DAO pour Document 
 *
 */
@SuppressWarnings("PMD")
@Repository
@Transactional(propagation = Propagation.SUPPORTS)
public final class DocumentDaoImpl
   extends
      MyHibernateDaoSupport<Document, Integer>
	implements
	   DocumentDao
{
	
   
   /**
    * Constructeur
    * 
    * @param sessionFactory la fabrique de session d'Hibernate
    */
	@Autowired
	public DocumentDaoImpl(
			@Qualifier("sessionFactory") SessionFactory sessionFactory) {
	   super(sessionFactory, Document.class,"id");
	}
	
	
	@Override
	public String testGetTable() {
	   return this.getTable();
	}

	

   @Override
   public List<Document> testFindAvecCriteria1(
         int idGreaterOrEqualThen,
         int firstResult,
         int maxResult, 
         String order, 
         boolean inverse) {
      
      Criteria criteria = this.getCriteria();
      criteria.add(Restrictions.ge("id", idGreaterOrEqualThen));
      
      return this.find(criteria,firstResult,maxResult,order,inverse);
      
   }


   @Override
   public List<Document> testFindAvecCriteria2(
         int idGreaterOrEqualThen,
         String order, 
         boolean inverse) {
      
      Criteria criteria = this.getCriteria();
      criteria.add(Restrictions.ge("id", idGreaterOrEqualThen));
      
      return this.find(criteria,order,inverse);
      
   }

   
   @Override
   public List<Document> testFindAvecCriteria3(
         int idGreaterOrEqualThen,
         int firstResult, 
         int maxResult) {
      
      Criteria criteria = this.getCriteria();
      criteria.add(Restrictions.ge("id", idGreaterOrEqualThen));
      
      return this.find(criteria,firstResult,maxResult);
      
   }
   

   @Override
   public List<Document> testFindAvecCriteria4(
         int idGreaterOrEqualThen) {
      
      Criteria criteria = this.getCriteria();
      criteria.add(Restrictions.ge("id", idGreaterOrEqualThen));
      
      return this.find(criteria);
      
   }
   
   
   @Override
   public List<Document> testFindByExample(Document obj) {
      return this.findByExample(obj);
   }
   
   
   @Override
   public int testCountAvecCriteria(
         int idGreaterOrEqualThen){
      
      Criteria criteria = this.getCriteria();
      criteria.add(Restrictions.ge("id", idGreaterOrEqualThen));
      
      return this.count(criteria);
      
   }


}
