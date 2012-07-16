package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.configuration.EcdeManager;
import fr.urssaf.image.sae.webservices.configuration.SecurityConfiguration;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasseResponseType;
import fr.urssaf.image.sae.webservices.util.AuthenticateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class ArchivageMasseTest {

   @Autowired
   private ArchivageMasseService service;

   @After
   public final void after() {

      SecurityConfiguration.cleanSecurityContext();
   }

   @BeforeClass
   public static void beforeClass() throws ConfigurationException, IOException {

      EcdeManager.cleanEcde();
   }

   @Test
   public void archivageMasse_success() throws URISyntaxException, IOException {

      AuthenticateUtils.authenticate("ROLE_TOUS");

      // enregistrement du sommaire dans l'ECDE
      // File sommaire = new File("src/test/resources/storage/sommaire.xml");
      // EcdeManager.copyFile(sommaire, "DCL001/19991231/3/sommaire.xml");

      URI urlSommaireEcde = new URI(
            "ecde://cnp69devecde.cer69.recouv/SAE_INTEGRATION/20110822/CaptureMasse-273-CaptureMasse-KO-JarArretTomCatSansRepriseAvecStockage/sommaire.xml");

      // File attestation = new
      // File("src/test/resources/storage/attestation.pdf");
      // EcdeManager.copyFile(attestation,
      // "DCL001/19991231/3/documents/attestation.pdf");

      ArchivageMasseResponseType response = service
            .archivageMasse(urlSommaireEcde);

      assertNotNull("Test de l'archivage masse", response);

   }

}
