package fr.urssaf.image.sae.webservices.skeleton;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.RecuperationMetadonnees;
import fr.cirtil.www.saeservice.RecuperationMetadonneesResponse;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.services.metadata.MetadataService;
import fr.urssaf.image.sae.webservices.exception.ErreurInterneAxisFault;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
public class RecuperationMetadonneesTest {

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   @Autowired
   private MetadataService metadataService;

   @After
   public void after() {
      EasyMock.reset(metadataService);
   }

   @Test
   public void testRecuperationMetadonnees() throws ErreurInterneAxisFault,
         AxisFault {

      RecuperationMetadonnees request = new RecuperationMetadonnees();

      List<MetadataReference> listeMeta = new ArrayList<MetadataReference>();
      MetadataReference meta1 = new MetadataReference();
      meta1.setLongCode("Titre");
      meta1.setLength(100);
      meta1.setArchivable(Boolean.TRUE);
      meta1.setRequiredForArchival(Boolean.TRUE);
      meta1.setSearchable(Boolean.TRUE);
      meta1.setIsIndexed(Boolean.FALSE);
      meta1.setModifiable(Boolean.TRUE);
      listeMeta.add(meta1);
      MetadataReference meta2 = new MetadataReference();
      meta2.setLongCode("Siren");
      meta2.setLength(10);
      meta2.setArchivable(Boolean.TRUE);
      meta2.setRequiredForArchival(Boolean.FALSE);
      meta2.setSearchable(Boolean.TRUE);
      meta2.setIsIndexed(Boolean.TRUE);
      meta2.setModifiable(Boolean.TRUE);
      listeMeta.add(meta2);

      EasyMock.expect(metadataService.getClientAvailableMetadata()).andReturn(
            listeMeta);

      EasyMock.replay(metadataService);

      RecuperationMetadonneesResponse recuperationMetadonneesSecure = skeleton
            .recuperationMetadonneesSecure(request);

      EasyMock.verify(metadataService);

      Assert.assertNotNull("la réponse doit etre non nulle",
            recuperationMetadonneesSecure.getRecuperationMetadonneesResponse());
      Assert.assertNotNull(
            "la liste des métadonnées en retour doit etre non nulle",
            recuperationMetadonneesSecure.getRecuperationMetadonneesResponse()
                  .getMetadonnees());
      Assert.assertTrue(
            "la liste des métadonnées en retour ne doit pas etre vide",
            recuperationMetadonneesSecure.getRecuperationMetadonneesResponse()
                  .getMetadonnees().getMetadonnee().length > 0);
   }
}
