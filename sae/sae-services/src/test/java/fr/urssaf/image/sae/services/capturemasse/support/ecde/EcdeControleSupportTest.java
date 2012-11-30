/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.ecde;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import junit.framework.Assert;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireHashException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireTypeHashException;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class EcdeControleSupportTest {

   @Autowired
   private EcdeControleSupport support;

   @Autowired
   private EcdeTestTools ecdeTestTools;

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

   /**
    * Test Argument sommaireFile obligatoire
    */
   @Test(expected = IllegalArgumentException.class)
   public void testSommaireFileObligatoire() {

      try {
         support.checkEcdeWrite(null);
      } catch (CaptureMasseEcdeWriteFileException e) {
         Assert.fail("On doit sortir avant par vérification aspect");
      }

      Assert.fail("On doit sortir avant par vérification aspect");
   }

   @Test
   public void testRepertoireNonInscriptible() {

      try {
         File repEcde = ecdeTestSommaire.getRepEcde();
         File fileSommaire = new File(repEcde, "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(fileSommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);
      } catch (FileNotFoundException e) {
         Assert.fail();
      } catch (IOException e) {
         Assert.fail();
      }

   }
   /**
    * Test vérifiant la levée d'exception en cas d'utilisation d'un algo autre que le SHA-1
    * @throws CaptureMasseSommaireHashException
    * @throws CaptureMasseSommaireTypeHashException
    * @throws IOException
    */
   @Test(expected=CaptureMasseSommaireTypeHashException.class)
   public void testNotSha1File() throws CaptureMasseSommaireHashException, CaptureMasseSommaireTypeHashException, IOException{
      
      ClassPathResource sommaireFile = new ClassPathResource("sommaire.xml");
      support.checkHash(sommaireFile.getFile(), "123456", "TOTO");
   }
   
   /**
    * Test vérifiant la levée d'exception en cas d'impossibilité de lecture du fichier. Dans ce test le fichier sommaire n'existe pas à l'emplacment indiqué
    * @throws CaptureMasseSommaireHashException
    * @throws CaptureMasseSommaireTypeHashException
    * @throws IOException
    */
   @Test(expected=SAECaptureServiceRuntimeException.class)
   public void testNoFileExist() throws CaptureMasseSommaireHashException, CaptureMasseSommaireTypeHashException, IOException{
      File repEcde = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repEcde, "sommaire.xml");
      support.checkHash(fileSommaire, "123456", "SHA-1");
   }

   /**
    * Test vérifiant la levée d'exception en cas de Hash différent
    * @throws CaptureMasseSommaireHashException
    * @throws CaptureMasseSommaireTypeHashException
    * @throws IOException
    */
   @Test(expected=CaptureMasseSommaireHashException.class)
   public void testIncorrectHash() throws CaptureMasseSommaireHashException, CaptureMasseSommaireTypeHashException, IOException{
      ClassPathResource sommaireFile = new ClassPathResource("sommaire.xml");
      support.checkHash(sommaireFile.getFile(), "123456", "SHA-1");
   }
   
   /**
    * Test vérifiant le comportement normal, hash et algo sont valides
    * @throws CaptureMasseSommaireHashException
    * @throws CaptureMasseSommaireTypeHashException
    * @throws IOException
    */
   @Test
   public void testCorrectHash() throws CaptureMasseSommaireHashException, CaptureMasseSommaireTypeHashException, IOException{
      ClassPathResource sommaireFile = new ClassPathResource("sommaire.xml");
      support.checkHash(sommaireFile.getFile(), "dc4ae92653b08ef0b806f4aba451fc28876c15bb", "SHA-1");
   }
   
}
