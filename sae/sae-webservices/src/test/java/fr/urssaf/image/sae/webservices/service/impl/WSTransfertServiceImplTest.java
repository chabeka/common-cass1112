/**
 * 
 */
package fr.urssaf.image.sae.webservices.service.impl;

import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.Transfert;
import fr.cirtil.www.saeservice.TransfertRequestType;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.transfert.ArchiveAlreadyTransferedException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.transfert.SAETransfertService;
import fr.urssaf.image.sae.webservices.exception.TransfertAxisFault;
import fr.urssaf.image.sae.webservices.service.WSTransfertService;

/**
 * Classe de test du ws de transfert de documents de
 * la GNT vers la GNS.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
public class WSTransfertServiceImplTest {

   @Autowired
   WSTransfertService wsTransfert;
   
   @Autowired
   SAETransfertService transfertService;
   
   private final String UUID = "df225dfa-9a30-4947-929e-21c00220faba";
   
   @After
   public void end() {
      EasyMock.reset(transfertService);
   }
   
   private void mockTransfertDoc() throws TransfertAxisFault{
      Transfert request = new Transfert();
      request.setTransfert(new TransfertRequestType());
      request.getTransfert().setUuid(new UuidType());
      request.getTransfert().getUuid().setUuidType(UUID);
      wsTransfert.transfert(request);
   }
   
   @Test
   public void testTransfert_success() throws TransfertAxisFault {
      mockTransfertDoc();
   }
   
   @Test(expected=TransfertAxisFault.class)
   public void testTransfert_archiveInexistanteEx() 
      throws TransfertAxisFault, TransfertException, ArchiveAlreadyTransferedException, ArchiveInexistanteEx {
      
      transfertService.transfertDoc(EasyMock.anyObject(UUID.class));
      EasyMock.expectLastCall().andThrow(new ArchiveInexistanteEx("test unitaire: ArchiveInexistanteEx"));
      EasyMock.replay(transfertService);
      
      mockTransfertDoc();
   }

   @Test(expected=TransfertAxisFault.class)
   public void testTransfert_archiveAlreadyTransfered() 
      throws TransfertAxisFault, TransfertException, ArchiveAlreadyTransferedException, ArchiveInexistanteEx {
      
      transfertService.transfertDoc(EasyMock.anyObject(UUID.class));
      EasyMock.expectLastCall().andThrow(new ArchiveAlreadyTransferedException("test unitaire: ArchiveAlreadyTransferedException"));
      EasyMock.replay(transfertService);
      
      mockTransfertDoc();
   }
   
   @Test(expected=TransfertAxisFault.class)
   public void testTransfert_transfertException() 
   throws TransfertAxisFault, TransfertException, ArchiveAlreadyTransferedException, ArchiveInexistanteEx {
      
      transfertService.transfertDoc(EasyMock.anyObject(UUID.class));
      EasyMock.expectLastCall().andThrow(new TransfertException("test unitaire : TransfertException"));
      EasyMock.replay(transfertService);
      
      mockTransfertDoc();
   }
   
}
