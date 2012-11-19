/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocumentDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.DfceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-regionalisation-cassandra-test.xml",
      "/applicationContext-sae-regionalisation-dao-mock-test.xml" })
public class DocumentServiceTest {

   @Autowired
   private DocumentDao documentDao;

   @Autowired
   private DocumentService documentService;

   @After
   public void end() {
      EasyMock.reset(documentDao);
   }

   @Test
   public void testGetDocument() {

      Document document = new DocumentImpl();
      document.setUuid(UUID.randomUUID());

      EasyMock.expect(documentDao.getDocument(EasyMock.anyObject(UUID.class)))
            .andReturn(document);
      EasyMock.replay(documentDao);

      Document value = documentService.getDocument(document.getUuid());

      EasyMock.verify(documentDao);

      Assert.assertEquals("l'uuid doit etre correct", document.getUuid(), value
            .getUuid());
   }

   @Test
   public void testErreurUpdate() {

      try {
         documentDao.updateDocument(EasyMock.anyObject(Document.class));

      } catch (DfceException exception) {
         Assert.fail("pas d'exception attendue");
      }

      EasyMock.expectLastCall().andThrow(
            new DfceException(new Exception("Erreur de sauvegarde")));
      EasyMock.replay(documentDao);

      try {
         documentService.updateDocument(new DocumentImpl());
         Assert.fail("une exception est attendue");

      } catch (DfceException exception) {
         EasyMock.verify(documentDao);
         
         Assert.assertEquals("l'exception mère doit etre du bon type",
               Exception.class, exception.getCause().getClass());
      }
   }

}
