package fr.urssaf.image.sae.ecde.service.validation;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.exception.EcdeBadFileException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.service.EcdeFileService;

/**
 * Classe permettant de tester l'aspect sur la validation des paramètres
 * d'entree des méthodes de la classe EcdeFileServiceValidation
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-ecde-test.xml")
@SuppressWarnings("PMD")
public class EcdeFileServiceValidationTest {

   @Autowired
   private EcdeFileService ecdeFileService;

   private static final String ECDECER69 = "ecde.cer69.recouv";
   private static final String ECDE = "ecde";
   private static final String ATTESTATION = "/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf";

   private static final String MESSAGE_INATTENDU = "message non attendu";

   // utilisation pour la convertion
   private static EcdeSource ecde1, ecde2, ecde3, ecde4, ecde5, ecde6, ecde7;

   private static final File ECDE_LYON = new File("/ecde/ecde_lyon/");

   // file attestation
   private static final File ATTESTATION_FILE = new File(
         "/ecde/ecde_lyon/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf");

   @BeforeClass
   public static void init() throws URISyntaxException {
      ecde1 = new EcdeSource("ecde.hoth.recouv", new File("/ecde/ecde_host/"));
      ecde2 = new EcdeSource(ECDECER69, ECDE_LYON);
      ecde3 = new EcdeSource("ecde.tatoine.recouv", new File(
            "/ecde/ecde_tatoine/"));
      ecde4 = new EcdeSource("host4", null);
      ecde5 = new EcdeSource("", ECDE_LYON);
      ecde6 = new EcdeSource(null, ECDE_LYON);
      ecde7 = new EcdeSource(" ", ECDE_LYON);
   }

   /**
    * convertFileToURI() - Le paramètre d'entrée ecdeFile n'est pas renseigné
    */
   @Test
   public void convertFileToURI_failure_argumentEcdeFileNonRenseinge()
         throws EcdeBadFileException {
      try {
         ecdeFileService.convertFileToURI(null, ecde1, ecde3);
         Assert
               .fail("Une exception IllegalArgumentException aurait dû être levée");
      } catch (IllegalArgumentException ex) {
         Assert.assertEquals("Le message de l'exception est incorrect",
               "L'argument 'ecdeFile' doit être renseigné.", ex.getMessage());
      }
   }

   /**
    * convertFileToURI() - Aucun ECDE n'est renseigné
    */
   @Test
   public void convertFileToURI_failure_aucunEcde() throws EcdeBadFileException {
      try {
         ecdeFileService.convertFileToURI(ATTESTATION_FILE);
         Assert.fail("Une exception aurait dû être levée");
      } catch (IllegalArgumentException e) {
         assertEquals(MESSAGE_INATTENDU,
               "Aucun ECDE n'est transmis en paramètre.", e.getMessage());
      }
   }

   /**
    * convertFileToURI() - L'attribut BasePath d'un ECDE n'est pas renseigne
    */
   @Test
   public void convertFileToURI_failure_attributBasePathNonRenseigne()
         throws EcdeBadFileException {
      try {
         ecdeFileService
               .convertFileToURI(ATTESTATION_FILE, ecde1, ecde2, ecde4);
         Assert.fail("Une exception aurait dû être levée");
      } catch (IllegalArgumentException ex) {
         Assert.assertEquals("Le message de l'exception est incorrect",
               "L'attribut 'BasePath' de l'ECDE No 2 n'est pas renseigné.", ex
                     .getMessage());
      }
   }

   /**
    * convertFileToURI() - L'attribut Host n'est pas renseigné (chaîne vide)
    */
   @Test
   public void convertFileToURI_failure_attributHostVide()
         throws EcdeBadFileException {
      try {
         ecdeFileService.convertFileToURI(ATTESTATION_FILE, ecde5);
         Assert.fail("Une exception aurait dû être levée");
      } catch (IllegalArgumentException e) {
         assertEquals(MESSAGE_INATTENDU,
               "L'attribut 'Host' de l'ECDE No 0 n'est pas renseigné.", e
                     .getMessage());
      }
   }

   /**
    * convertFileToURI() - L'attribut Host n'est pas renseigné (null)
    */
   @Test
   public void convertFileToURI_failure_attributHostNull()
         throws EcdeBadFileException {
      try {
         ecdeFileService.convertFileToURI(ATTESTATION_FILE, ecde6);
         Assert.fail("Une exception aurait dû être levée");
      } catch (IllegalArgumentException e) {
         assertEquals(MESSAGE_INATTENDU,
               "L'attribut 'Host' de l'ECDE No 0 n'est pas renseigné.", e
                     .getMessage());
      }
   }

   /**
    * convertFileToURI() - L'attribut Host n'est pas renseigné (des espaces)
    */
   @Test
   public void convertFileToURI_failure_attributHostEspace()
         throws EcdeBadFileException {
      try {
         ecdeFileService.convertFileToURI(ATTESTATION_FILE, ecde7);
         Assert.fail("Une exception aurait dû être levée");
      } catch (IllegalArgumentException e) {
         assertEquals(MESSAGE_INATTENDU,
               "L'attribut 'Host' de l'ECDE No 0 n'est pas renseigné.", e
                     .getMessage());
      }
   }

   /**
    * convertURIToFile() - L'argument ecdeUrl est null
    */
   @Test
   public void convertUrlToFile_failure_urlNull() throws EcdeBadURLException,
         EcdeBadURLFormatException {
      try {
         ecdeFileService.convertURIToFile(null, ecde1, ecde2, ecde3);
         Assert.fail("Une exception aurait dû être levée");
      } catch (IllegalArgumentException e) {
         assertEquals(MESSAGE_INATTENDU,
               "L'argument 'ecdeURL' doit être renseigné.", e.getMessage());
      }
   }

   /**
    * convertURIToFile() - Aucun ECDE
    */
   @Test
   public void convertUrlToFileTestUrlNull() throws URISyntaxException,
         EcdeBadURLException, EcdeBadURLFormatException {
      try {
         URI uri = new URI(ECDE, ECDECER69, ATTESTATION, null);
         ecdeFileService.convertURIToFile(uri);
      } catch (IllegalArgumentException e) {
         assertEquals(MESSAGE_INATTENDU,
               "Aucun ECDE n'est transmis en paramètre.", e.getMessage());
      }
   }

   /**
    * convertURIToFile() - L'attribut scheme n'est pas correctement renseigne à
    * savoir "ecde"
    */
   @Test
   public void convertUrlToFile_failure_attributSchemeIncorrect()
         throws URISyntaxException, EcdeBadURLException,
         EcdeBadURLFormatException {
      try {
         URI uri = new URI("e", ECDECER69, ATTESTATION, null);
         ecdeFileService.convertURIToFile(uri, ecde1, ecde2, ecde3);
      } catch (EcdeBadURLFormatException e) {
         assertEquals(
               MESSAGE_INATTENDU,
               "L'URL ECDE e://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf est incorrecte.",
               e.getMessage());
      }
   }

}