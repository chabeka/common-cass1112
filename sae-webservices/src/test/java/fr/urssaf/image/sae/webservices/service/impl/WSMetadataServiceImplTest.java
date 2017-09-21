package fr.urssaf.image.sae.webservices.service.impl;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import fr.cirtil.www.saeservice.MetadonneeDispoType;
import fr.cirtil.www.saeservice.RecuperationMetadonneesResponse;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.services.metadata.MetadataService;
import fr.urssaf.image.sae.webservices.exception.ErreurInterneAxisFault;

/**
 * Tests unitaires de la classe {@link WSMetadataServiceImpl}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
public class WSMetadataServiceImplTest {

   @Autowired
   private WSMetadataServiceImpl wsMetadataService;
   
   @Autowired
   private MetadataService metadataService;
   
   @Test
   public void recupererMetadonnees() throws ErreurInterneAxisFault {
      
      List<MetadataReference> retour = construireListeMeta(10);
      
      EasyMock.expect(metadataService.getClientAvailableMetadata()).andReturn(retour).once();
      
      EasyMock.replay(metadataService);
      
      RecuperationMetadonneesResponse response = wsMetadataService.recupererMetadonnees();
      Assert.assertNotNull("La réponse ne doit pas être null", response);
      Assert.assertNotNull("L'objet RecuperationMetadonneesResponse ne doit pas être null", response.getRecuperationMetadonneesResponse());
      Assert.assertNotNull("L'objet ListeMetadonneeDispoType ne doit pas être null", response.getRecuperationMetadonneesResponse().getMetadonnees());
      MetadonneeDispoType[] metadatas = response.getRecuperationMetadonneesResponse().getMetadonnees().getMetadonnee();
      Assert.assertNotNull("Le tableau MetadonneeDispoType ne doit pas être null", metadatas);
      Assert.assertEquals("Le tableau MetadonneeDispoType ne doit pas être vide", 10, metadatas.length);
      
      EasyMock.reset(metadataService);
   }
   
   private List<MetadataReference> construireListeMeta(int nbMeta) {
      List<MetadataReference> retour = new ArrayList<MetadataReference>();
      for (int index = 0; index < nbMeta; index++) {
         MetadataReference meta = new MetadataReference();
         meta.setLongCode("metadata" + Integer.toString(index));
         meta.setLabel("Metadonnee numero " + Integer.toString(index));
         meta.setDescription("Ceci est ma metadonnee numero " + Integer.toString(index));
         meta.setShortCode("M" + Integer.toString(index));
         meta.setType("string");
         meta.setPattern("");
         meta.setArchivable(Boolean.TRUE);
         meta.setRequiredForArchival(Boolean.TRUE);
         meta.setLength(30);
         meta.setSearchable(Boolean.TRUE);
         meta.setIsIndexed(Boolean.FALSE);
         meta.setModifiable(Boolean.TRUE);
         retour.add(meta);
      }
      return retour;
   }
}

