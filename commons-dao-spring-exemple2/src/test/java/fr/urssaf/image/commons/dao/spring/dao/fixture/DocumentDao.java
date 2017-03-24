package fr.urssaf.image.commons.dao.spring.dao.fixture;

import java.util.List;

import fr.urssaf.image.commons.dao.spring.service.EntityCountDao;
import fr.urssaf.image.commons.dao.spring.service.EntityFindDao;
import fr.urssaf.image.commons.dao.spring.service.EntityIdDao;
import fr.urssaf.image.commons.dao.spring.service.EntityModifyDao;


/**
 * Pour les tests unitaires<br>
 * <br>
 * DAO de l'entité Document
 *
 */
@SuppressWarnings("PMD")
public interface DocumentDao
   extends
      EntityModifyDao<Document>,
		EntityIdDao<Document, Integer>, 
		EntityFindDao<Document>, 
		EntityCountDao 
{

   /**
    * Pour tester la méthode MyHibernateDaoSupport.getTable
    * 
    * @return nom de la classe persistante
    */
   String testGetTable() ;
   
   
   /**
    * Pour tester la méthode MyHibernateDaoSupport.find
    * 
    * @param idGreaterOrEqualThen 
    * @param firstResult  ___
    * @param maxResult ___
    * @param order ___
    * @param inverse ___
    * @return ___
    */
   List<Document> testFindAvecCriteria1(
         int idGreaterOrEqualThen,
         int firstResult, 
         int maxResult,
         String order, 
         boolean inverse);
   
   
   /**
    * Pour tester la méthode MyHibernateDaoSupport.find
    * 
    * @param idGreaterOrEqualThen ___
    * @param order ___
    * @param inverse ___
    * @return ___
    */
   List<Document> testFindAvecCriteria2(
         int idGreaterOrEqualThen,
         String order, 
         boolean inverse);

   
   /**
    * Pour tester la méthode MyHibernateDaoSupport.find
    * 
    * @param idGreaterOrEqualThen ___
    * @param firstResult ___
    * @param maxResult ___
    * @return ___
    */
   List<Document> testFindAvecCriteria3(
         int idGreaterOrEqualThen,
         int firstResult, 
         int maxResult) ;
   

   /**
    * Pour tester la méthode MyHibernateDaoSupport.find
    * 
    * @param idGreaterOrEqualThen ___
    * @return ___
    */
   List<Document> testFindAvecCriteria4(
         int idGreaterOrEqualThen);
   
   
   /**
    * Pour tester la méthode MyHibernateDaoSupport.findByExample
    * 
    * @param obj ___
    * @return ___
    */
   List<Document> testFindByExample(Document obj);
   
   
   /**
    * Pour tester la méthode MyHibernateDaoSupport.count
    * 
    * @param idGreaterOrEqualThen ___
    * @return ___
    */
   int testCountAvecCriteria(
         int idGreaterOrEqualThen);
   
}
