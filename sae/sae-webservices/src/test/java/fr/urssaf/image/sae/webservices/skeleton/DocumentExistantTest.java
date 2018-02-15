package fr.urssaf.image.sae.webservices.skeleton;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.DocumentExistant;
import fr.cirtil.www.saeservice.DocumentExistantRequestType;
import fr.cirtil.www.saeservice.DocumentExistantResponse;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.services.documentExistant.SAEDocumentExistantService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.webservices.exception.DocumentExistantAxisFault;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
public class DocumentExistantTest {
   
   @Autowired
   private SaeServiceSkeletonInterface skeleton;
   
   @Autowired
   private SAEDocumentExistantService saeService;
   
   @Test
   public void documentExistant_success_true() throws SearchingServiceEx, ConnectionServiceEx, DocumentExistantAxisFault{
      DocumentExistant request = new DocumentExistant();
      request.setDocumentExistant(new DocumentExistantRequestType());
      
      request.getDocumentExistant().setIdGed(new UuidType());
      request.getDocumentExistant().getIdGed().setUuidType("00000000-0000-0000-0000-000000000000");
      
      boolean res = true;
      
      EasyMock.expect(saeService.documentExistant((UUID) EasyMock.anyObject())).andReturn(res);
      EasyMock.replay(saeService);
      
      DocumentExistantResponse response = skeleton.documentExistant(request);
      
      assertEquals("Le document n'existe pas donc renvoie false", true, res);
      
      EasyMock.reset(saeService);
   }
   
   @Test
   public void documentExistant_success_false() throws SearchingServiceEx, ConnectionServiceEx, DocumentExistantAxisFault{
      DocumentExistant request = new DocumentExistant();
      request.setDocumentExistant(new DocumentExistantRequestType());
      
      request.getDocumentExistant().setIdGed(new UuidType());
      request.getDocumentExistant().getIdGed().setUuidType("00000000-0000-0000-0000-000000000000");
      
      boolean res = false;
      
      EasyMock.expect(saeService.documentExistant((UUID) EasyMock.anyObject())).andReturn(res);
      EasyMock.replay(saeService);
      
      DocumentExistantResponse response = skeleton.documentExistant(request);
      
      assertEquals("Le document n'existe pas donc renvoie false", false, res);

      EasyMock.reset(saeService);
   }
}
