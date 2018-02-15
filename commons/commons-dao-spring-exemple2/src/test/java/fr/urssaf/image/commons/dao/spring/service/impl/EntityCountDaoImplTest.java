package fr.urssaf.image.commons.dao.spring.service.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.dao.spring.dao.fixture.DocumentDao;


/**
 * Tests unitaires de la classe {@link EntityCountDaoImpl} 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-service.xml")
@SuppressWarnings("PMD")
public class EntityCountDaoImplTest {

   
   @Autowired
   private DocumentDao documentDao;
   
   
   /**
    * Test unitaire de la méthode {@link EntityCountDaoImpl#count}
    */
   @Test
   public void count() {
      
      int actual = documentDao.count();
      
      int expected = 5; // 5 enregistrements insérés dans le script de test
      
      assertEquals("Le count est erroné",expected,actual);
      
   }
   
   
}
