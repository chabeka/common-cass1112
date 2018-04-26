package fr.urssaf.image.commons.dao.spring.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.dao.spring.dao.fixture.Document;
import fr.urssaf.image.commons.dao.spring.dao.fixture.DocumentDao;


/**
 * Tests unitaires de la classe {@link EntityIdDaoImpl} 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-service.xml")
@SuppressWarnings("PMD")
public class EntityIdDaoImplTest {

   @Autowired
   private DocumentDao documentDao;
   
   
   /**
    * Test unitaire de la méthode {@link EntityIdDaoImpl#get}<br>
    * <br>
    * Cas de test : l'élément recherché existe en base<br>
    * <br>
    * Résultat attendu : l'élément est lu correctement
    */
   @Test
   public void getCasNormal() {
      
      Document document = documentDao.get(2);
      
      assertDocument(document);
      
   }
   
   
   private void assertDocument(Document document) {
      
      assertEquals((Integer)2,document.getId());
      assertEquals("titre 2",document.getTitre());
      
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR,1999);
      calendar.set(Calendar.MONTH,6); // les numéros de mois commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH,3);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      Date laDate = calendar.getTime();
      
      assertEquals(laDate.getTime(),document.getDate().getTime());
      
   }
   
   
   
   /**
    * Test unitaire de la méthode {@link EntityIdDaoImpl#get}<br>
    * <br>
    * Cas de test : l'élément recherché n'existe pas en base<br>
    * <br>
    * Résultat attendu : la méthode renvoie null
    */
   @Test
   public void getCasIdInexistant() {

      Document document = documentDao.get(20);
      
      assertNull(document);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link EntityIdDaoImpl#find}<br>
    * <br>
    * Cas de test : l'élément recherché existe en base<br>
    * <br>
    * Résultat attendu : l'élément est lu correctement
    */
   @Test
   public void findCasNormal() {
      
      Document document = documentDao.find(2);
      
      assertDocument(document);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link EntityIdDaoImpl#find}<br>
    * <br>
    * Cas de test : l'élément recherché n'existe pas en base<br>
    * <br>
    * Résultat attendu : la méthode renvoie null
    */
   @Test
   public void findCasIdInexistant() {

      Document document = documentDao.find(20);
      
      assertNull(document);
      
   }
   
   
}
