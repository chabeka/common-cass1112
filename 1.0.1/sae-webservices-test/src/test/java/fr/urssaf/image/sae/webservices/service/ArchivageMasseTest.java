package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

   @Test
   public void archivageMasse_success() throws RemoteException,
         URISyntaxException {

      AuthenticateUtils.authenticate("ROLE_TOUS");

      URI urlSommaireEcde = new URI(
            "ecde://cer69-ecde.cer69.recouv/DCL001/19991231/3/sommaire.xml");

      ArchivageMasseResponseType response = service
            .archivageMasse(urlSommaireEcde);

      assertNotNull("Test de l'archivage masse", response);

   }

}
