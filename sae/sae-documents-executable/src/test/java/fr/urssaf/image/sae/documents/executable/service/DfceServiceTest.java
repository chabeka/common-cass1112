package fr.urssaf.image.sae.documents.executable.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import net.docubase.toolkit.model.document.Document;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class DfceServiceTest {

   @Autowired
   private DfceService dfceService;
   
   @Before
   public void ouvrirConnexion() {
      dfceService.ouvrirConnexion();
   }
   
   @After
   public void fermerConnexion() {
      dfceService.fermerConnexion();
   }
   
   @Test
   public void executerRequete() {
      Iterator<Document> resultat = dfceService.executerRequete("srt:41882050200023");
      
      Assert.assertNotNull("La liste des documents retrouvés ne doit pas être null", resultat);
      Assert.assertTrue("La liste des documents retrouvés ne contient pas d'éléments", resultat.hasNext());
      Assert.assertNotNull("Le document retrouvé ne doit pas être null", resultat.next());
   }
   
   @Test
   public void recupererContenu() {
      Iterator<Document> resultat = dfceService.executerRequete("srt:41882050200023");
      
      Assert.assertNotNull("La liste des documents retrouvés ne doit pas être null", resultat);
      Assert.assertTrue("La liste des documents retrouvés ne contient pas d'éléments", resultat.hasNext());
      Document document = resultat.next();
      Assert.assertNotNull("Le document retrouvé ne doit pas être null", document);
      
      InputStream stream = dfceService.recupererContenu(document);
      Assert.assertNotNull("Le contenu du fichier récupéré ne doit pas être null", stream);
      
      try {
         stream.close();
      } catch (IOException e) {
         Assert.fail(e.getMessage());
      }
   }
}
