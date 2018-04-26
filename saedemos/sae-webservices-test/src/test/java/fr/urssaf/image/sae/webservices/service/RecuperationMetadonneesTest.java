package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.RecuperationMetadonneesResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class RecuperationMetadonneesTest {

   @Autowired
   private RecuperationMetadonneesService service;

   @Test
   public void recuperationMetadonnees_success() throws RemoteException {

      RecuperationMetadonneesResponse response = service
            .recuperationMetadonnees();

      assertNotNull("La réponse ne doit pas être nulle", response);

      assertNotNull("Le contenu de la réponse ne doit pas être nulle", response
            .getRecuperationMetadonneesResponse());

      assertNotNull("La liste des métadonnées ne doit pas être nulle", response
            .getRecuperationMetadonneesResponse().getMetadonnees());

      assertNotNull("La liste des métadonnées ne doit pas être nulle", response
            .getRecuperationMetadonneesResponse().getMetadonnees()
            .getMetadonnee());

      assertTrue("Le contenu de la réponse ne doit pas être nulle", response
            .getRecuperationMetadonneesResponse().getMetadonnees()
            .getMetadonnee().length > 0);
   }
}
