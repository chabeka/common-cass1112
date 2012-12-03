package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Assert;

import org.apache.axis2.AxisFault;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.configuration.EcdeManager;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasseAvecHashResponseType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class ArchivageMasseAvecHashTest {

   @Autowired
   private ArchivageMasseAvecHashService service;
   

   @BeforeClass
   public static void beforeClass() throws ConfigurationException, IOException {

      EcdeManager.cleanEcde();
   }

   /**
    * Test permettant de vérifier que le webService repond
    * @throws URISyntaxException
    * @throws IOException
    */
   
   @Test
   @Ignore
   public void archivageMasseAvecHash_success() throws URISyntaxException, IOException {

      // enregistrement du sommaire dans l'ECDE
      // File sommaire = new File("src/test/resources/storage/sommaire.xml");
      // EcdeManager.copyFile(sommaire, "DCL001/19991231/3/sommaire.xml");

      URI urlSommaireEcde = new URI(
            "ecde://CER69-TEC24251/SAE_INTEGRATION/20110822/CaptureMasse-212-CaptureMasse-Pile-OK-ECDE-local-1/sommaire.xml");

      // File attestation = new
      // File("src/test/resources/storage/attestation.pdf");
      // EcdeManager.copyFile(attestation,
      // "DCL001/19991231/3/documents/attestation.pdf");
      String hash="42b7d80f7ba0fd3b31e46141876ce4301fdba4a0";
      String typeHash="SHA-1";
      
      ArchivageMasseAvecHashResponseType response = service.archivageMasse(urlSommaireEcde, hash, typeHash);
      
      assertNotNull("Echec du test de récupération de l'identifiat de traitement de capture de masse avec hash", response.getUuid());

   }

   
   @Test
   @Ignore
   public void archivageMasseAvecHash_FailureHashKo() throws URISyntaxException, IOException {

      URI urlSommaireEcde = new URI(
            "ecde://CER69-TEC24251/SAE_INTEGRATION/20110822/CaptureMasse-212-CaptureMasse-Pile-OK-ECDE-local-1/sommaire.xml");
      // le hash est incorrecte
      String hash = "42b7d80f0fd3b31e46141876ce4301fdba4a0";
      String typeHash = "SHA-1";
      try {
         ArchivageMasseAvecHashResponseType response = service.archivageMasse(
               urlSommaireEcde, hash, typeHash);
      } catch (AxisFault fault) {

         Assert.assertEquals("HashSommaireIncorrect", fault.getFaultCode().getLocalPart());

      }

   }
   
   @Test
   @Ignore
   public void archivageMasseAvecHash_FailureTypeHashKo() throws URISyntaxException, IOException {

      URI urlSommaireEcde = new URI(
            "ecde://CER69-TEC24251/SAE_INTEGRATION/20110822/CaptureMasse-212-CaptureMasse-Pile-OK-ECDE-local-1/sommaire.xml");
      String hash="42b7d80f0fd3b31e46141876ce4301fdba4a0";
      // le type de hash est incorrecte
      String typeHash="algo-1";
      
      try {
         ArchivageMasseAvecHashResponseType response = service.archivageMasse(
               urlSommaireEcde, hash, typeHash);
      } catch (AxisFault fault) {

         Assert.assertEquals("TypeHashSommaireIncorrect", fault.getFaultCode().getLocalPart());

      }

   }
   
   @Test
   @Ignore
   public void archivageMasseAvecHash_FailureNoParamKo() throws URISyntaxException, IOException {

      URI urlSommaireEcde = new URI(
            "ecde://CER69-TEC24251/SAE_INTEGRATION/20110822/CaptureMasse-212-CaptureMasse-Pile-OK-ECDE-local-1/sommaire.xml");
      String hash="";
      // le type de hash est incorrecte
      String typeHash="";
      
      try {
         ArchivageMasseAvecHashResponseType response = service.archivageMasse(
               urlSommaireEcde, hash, typeHash);
      } catch (AxisFault fault) {

         Assert.assertEquals("TypeHashSommaireIncorrect", fault.getFaultCode().getLocalPart());

      }

   }
}
