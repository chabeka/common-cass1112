package fr.urssaf.image.commons.controller.spring3.jndi.exemple.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.controller.spring3.jndi.exemple.jndi.JndiSupport;
import fr.urssaf.image.commons.controller.spring3.jndi.exemple.modele.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContextTest.xml")
public class DocumentDaoTest {

   private static final Logger LOG = Logger.getLogger(DocumentDaoTest.class);

   @Autowired
   private DocumentDao documentDao;

   @BeforeClass
   public static void init()
   {
      JndiSupport.insereDataSource();
   }


   @Test
   public void list() {
      
      List<Document> documents = documentDao.allDocuments();
      
      assertFalse(
            "la liste des documents doit être non vide", 
            documents.isEmpty());

      for (Document document : documents) {
         LOG.debug(document.getId() + ":" + document.getTitre());
      }

      assertEquals(
            "le nombre d'enregistrement est incorrect", 
            5, 
            documents.size());
      
   }

   
   @Test
   @Transactional
   public void save() {

      Date date = new Date();
      String titre = "titre test";

      Document document = new Document(titre, date);
      documentDao.save(document);

      Document documentBase = documentDao.find(document.getId());

      assertEquals("titre est incorrecte", titre, documentBase.getTitre());
      assertEquals("date est incorrecte", date, documentBase.getDate());

   }

}
