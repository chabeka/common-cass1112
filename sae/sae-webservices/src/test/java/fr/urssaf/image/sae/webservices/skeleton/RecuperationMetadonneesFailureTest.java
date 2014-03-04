package fr.urssaf.image.sae.webservices.skeleton;

import junit.framework.Assert;

import org.apache.axis2.AxisFault;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.RecuperationMetadonnees;
import fr.urssaf.image.sae.services.metadata.MetadataService;
import fr.urssaf.image.sae.webservices.exception.ErreurInterneAxisFault;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
public class RecuperationMetadonneesFailureTest {

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   @Autowired
   private MetadataService metadataService;

   @After
   public void after() {
      EasyMock.reset(metadataService);
   }

   @Test
   public void testRecuperationMetadonneesWithErreurInterneAxisFault()
         throws AxisFault {

      RecuperationMetadonnees request = new RecuperationMetadonnees();

      EasyMock.expect(metadataService.getClientAvailableMetadata()).andThrow(
            new RuntimeException("exception de test"));

      EasyMock.replay(metadataService);

      try {

         skeleton.recuperationMetadonneesSecure(request);

         Assert
               .fail("Une erreur de type ErreurInterneAxisFault aurait du se produire.");

      } catch (ErreurInterneAxisFault ex) {
         Assert.assertEquals("Le message d'erreur n'est pas correct.",
               "Une erreur interne à l'application est survenue.", ex
                     .getMessage());
      }

      EasyMock.verify(metadataService);

      /*
       * Assert.assertNotNull("la réponse doit etre non nulle",
       * recuperationMetadonneesSecure.getRecuperationMetadonneesResponse());
       * Assert.assertNotNull(
       * "la liste des métadonnées en retour doit etre non nulle",
       * recuperationMetadonneesSecure.getRecuperationMetadonneesResponse()
       * .getMetadonnees()); Assert.assertTrue(
       * "la liste des métadonnées en retour ne doit pas etre vide",
       * recuperationMetadonneesSecure.getRecuperationMetadonneesResponse()
       * .getMetadonnees().getMetadonnee().length > 0);
       */
   }
}
