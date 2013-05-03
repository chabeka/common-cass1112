/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.CaptureMasseErreur;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class ResultatsFileEchecSupportTest {

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private ResultatsFileEchecSupport support;

   private EcdeTestSommaire ecdeTestSommaire;

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
   }

   @After
   public void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }
   }

   @Test(expected = IllegalArgumentException.class)
   public void testEcdeDirectoryObligatoire() {
      support.writeResultatsFile(null, new File(""), new CaptureMasseErreur(),
            0);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSommaireObligatoire() {
      support.writeResultatsFile(new File(""), null, new CaptureMasseErreur(),
            0);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testErreurObligatoire() {
      support.writeResultatsFile(new File(""), new File(""), null, 0);
   }

   @Test
   public void testEcritureSommaire() throws IOException, JAXBException {
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();

      File sommaire = new File(ecdeDirectory, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repEcde = new File(ecdeDirectory, "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repEcde, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      CaptureMasseErreur erreur = new CaptureMasseErreur();
      List<String> codes = new ArrayList<String>();
      codes.add(Constantes.ERR_BUL002);
      List<Integer> index = new ArrayList<Integer>();
      index.add(3);
      List<Exception> exceptions = new ArrayList<Exception>();
      exceptions.add(new Exception("la valeur x est erronée"));

      erreur.setListCodes(codes);
      erreur.setListException(exceptions);
      erreur.setListIndex(index);

      support.writeResultatsFile(ecdeDirectory, sommaire, erreur, 21);

      File resultats = new File(ecdeDirectory, "resultats.xml");

      Assert.assertTrue("la fichier resultats.xml doit exister", resultats
            .exists());
      Assert
            .assertTrue("le fichier doit etre non vide", resultats.length() > 0);

   }

   @Test
   public void testVirtualEcdeDirectoryObligatoire() {

      try {
         support.writeVirtualResultatsFile(null, new File(""),
               new CaptureMasseErreur(), 0);
         Assert.fail("exception IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit être correct", exception
               .getMessage().contains("ecdeDirectory"));

      } catch (Exception exception) {
         Assert.fail("exception IllegalArgumentException attendue");
      }

   }

   @Test
   public void testVirtualSommaireObligatoire() {

      try {
         support.writeVirtualResultatsFile(new File(""), null,
               new CaptureMasseErreur(), 0);
         Assert.fail("exception IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit être correct", exception
               .getMessage().contains("sommaireFile"));

      } catch (Exception exception) {
         Assert.fail("exception IllegalArgumentException attendue");
      }
   }

   @Test
   public void testVirtualErreurObligatoire() {

      try {
         support.writeVirtualResultatsFile(new File(""), new File(""), null, 0);
         Assert.fail("exception IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit être correct", exception
               .getMessage().contains("erreur"));

      } catch (Exception exception) {
         Assert.fail("exception IllegalArgumentException attendue");
      }

   }

}
