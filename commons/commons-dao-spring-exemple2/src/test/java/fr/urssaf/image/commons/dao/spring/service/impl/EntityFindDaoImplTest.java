package fr.urssaf.image.commons.dao.spring.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.dao.spring.dao.fixture.Document;
import fr.urssaf.image.commons.dao.spring.dao.fixture.DocumentDao;


/**
 * Tests unitaires de la classe {@link EntityFindDaoImpl} 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-service.xml")
@SuppressWarnings("PMD")
public class EntityFindDaoImplTest {

   private static final int NB_DOC_TOTAL = 5;
   
   
   @Autowired
   private DocumentDao documentDao;
   
   
   /**
    * Test unitaire de la méthode {@link EntityFindDaoImpl#find()}
    */
   @Test
   public void find_V1() {
    
      List<Document> documents = documentDao.find();
      
      assertNotNull(
            "Le documents n'ont pas été lus",
            documents);
      
      assertEquals(
            "Le nombre de documents lus est incorrect",
            NB_DOC_TOTAL,
            documents.size());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link EntityFindDaoImpl#find(String)}
    */
   @Test
   public void find_V2() {
    
      List<Document> documents = documentDao.find("id");
      
      assertNotNull(
            "Le documents n'ont pas été lus",
            documents);
      
      assertEquals(
            "Le nombre de documents lus est incorrect",
            NB_DOC_TOTAL,
            documents.size());
      
      assertEquals(
            "L'ordre des documents est incorrect",
            (Integer)1,
            documents.get(0).getId());
      
      assertEquals(
            "L'ordre des documents est incorrect",
            (Integer)5,
            documents.get(4).getId());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link EntityFindDaoImpl#find(String, boolean)}
    */
   @Test
   public void find_V3() {
      
      List<Document> documents = documentDao.find("id",true); // tri décroissant
      
      assertNotNull(
            "Le documents n'ont pas été lus",
            documents);
      
      assertEquals(
            "Le nombre de documents lus est incorrect",
            NB_DOC_TOTAL,
            documents.size());
      
      assertEquals(
            "L'ordre des documents est incorrect",
            (Integer)5,
            documents.get(0).getId());
      
      assertEquals(
            "L'ordre des documents est incorrect",
            (Integer)1,
            documents.get(4).getId());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link EntityFindDaoImpl#find(int, int, String, boolean)}
    */
   @Test
   public void find_V4() {
      
      // tri décroissant
      int nbDocs = 3;
      List<Document> documents = documentDao.find(1,nbDocs,"id",true);
      
      assertNotNull(
            "Le documents n'ont pas été lus",
            documents);
      
      assertEquals(
            "Le nombre de documents lus est incorrect",
            nbDocs,
            documents.size());
      
      assertEquals(
            "L'ordre des documents est incorrect",
            (Integer)4,
            documents.get(0).getId());
      
      assertEquals(
            "L'ordre des documents est incorrect",
            (Integer)2,
            documents.get(2).getId());
      
   }
   
   
}
