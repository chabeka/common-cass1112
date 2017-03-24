package fr.urssaf.image.commons.controller.spring3.jndi.exemple.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.controller.spring3.jndi.exemple.modele.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContextTestJndiComplet.xml")
public class DocumentDaoTestAvecJndiComplet {

   @Autowired
   private DocumentDao documentDao;

   
   @Test
   public void list() {
      
      List<Document> documents = documentDao.allDocuments();
      
      assertFalse("la liste des documents doit être non vide", documents.isEmpty());

      assertEquals("le nombre d'enregistrement est incorrect", 5, documents.size());
      
   }

   
}
