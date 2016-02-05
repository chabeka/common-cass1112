/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.controle.batch;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.CaptureMasseControleSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.batch.ControleStorageDocumentProcessor;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-batch-test.xml",
      "/applicationContext-sae-services-capturemasse-test-mock-storagedocument.xml" })
public class ControleStorageDocumentProcessorTest {

   @Autowired
   private ControleStorageDocumentProcessor processor;

   @Autowired
   private CaptureMasseControleSupport cmControleSupport;

   @After
   public void reset() {
      EasyMock.reset(cmControleSupport);
   }

   @Test(expected = RequiredStorageMetadataEx.class)
   public void testErreurSupport() throws Exception {

      cmControleSupport.controleSAEDocumentStockage(EasyMock
            .anyObject(SAEDocument.class));

      EasyMock.expectLastCall().andThrow(
            new RequiredStorageMetadataEx("erreur"));

      EasyMock.replay(cmControleSupport);

      SAEDocument document = new SAEDocument();

      processor.process(document);

      Assert.fail("exception attendue");
   }
   
   
   @Test
   public void testSuccess() {
      

      // StepExecution execution = new Ste
      
   }

}
