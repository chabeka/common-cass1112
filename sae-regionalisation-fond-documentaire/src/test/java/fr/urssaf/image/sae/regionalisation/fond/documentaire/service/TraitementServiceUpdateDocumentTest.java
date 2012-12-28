/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.common.Constants;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.DfceException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.ErreurTechniqueException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.ServiceProviderSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-regionalisation-cassandra-test.xml",
      "/applicationContext-sae-regionalisation-service-mock-test.xml" })
public class TraitementServiceUpdateDocumentTest {

   @Autowired
   private DocumentService documentService;

   @Autowired
   private ServiceProviderSupport providerSupport;

   @Autowired
   private TraitementService service;

   private File inputFile;
   private File propertiesFile;
   private File outFile;

   @Before
   public void init() throws IOException {
      inputFile = File.createTempFile("input", ".txt");
      propertiesFile = File.createTempFile("prop", ".txt");
      outFile = File.createTempFile("out", ".txt");
   }

   @After
   public void end() {
      FileUtils.deleteQuietly(inputFile);
      FileUtils.deleteQuietly(propertiesFile);
      FileUtils.deleteQuietly(outFile);
      EasyMock.reset(providerSupport, documentService);
   }

   @Test
   public void testFichierEntreeInexistant() {
      initProvider();
      FileUtils.deleteQuietly(inputFile);

      try {
         service.updateDocuments("fichierInexistant", "fichierInexistant",
               "fichierInexistant", 0, 0);
         Assert.fail("une exception ErreurTechniqueException est attendue");
      } catch (ErreurTechniqueException exception) {
         EasyMock.verify(providerSupport);
      } catch (Exception exception) {
         Assert.fail("une exception ErreurTechniqueException est attendue");
      }
   }

   @Test
   public void testUpdateDocumentErreur() throws DfceException, IOException {
      initProvider();
      initDocumentServiceErreur();
      initDatasErreur();
      FileUtils.deleteQuietly(outFile);

      try {
         service.updateDocuments(inputFile.getAbsolutePath(), outFile
               .getAbsolutePath(), propertiesFile.getAbsolutePath(), 1, 2);
         Assert.fail("une exception ErreurTechniqueException est attendue");

      } catch (ErreurTechniqueException exception) {
         EasyMock.verify(providerSupport, documentService);

      } catch (Exception exception) {
         Assert.fail("une exception ErreurTechniqueException est attendue");
      }
   }

   @Test
   public void testUpdateDocumentOk() throws DfceException, IOException {
      initProvider();
      initDocumentService();
      initDatas();
      FileUtils.deleteQuietly(outFile);

      service.updateDocuments(inputFile.getAbsolutePath(), outFile
            .getAbsolutePath(), propertiesFile.getAbsolutePath(), 1, 2);

      EasyMock.verify(providerSupport, documentService);
   }

   private void initProvider() {
      providerSupport.connect();
      EasyMock.expectLastCall().once();

      providerSupport.disconnect();
      EasyMock.expectLastCall().once();

      EasyMock.replay(providerSupport);
   }

   private void initDocumentServiceErreur() throws DfceException {
      EasyMock.expect(
            documentService.getDocument(EasyMock.anyObject(UUID.class)))
            .andReturn(new DocumentImpl()).once();
      documentService.updateDocument(EasyMock.anyObject(Document.class));
      EasyMock.expectLastCall().andThrow(
            new DfceException(new Exception("erreur de sauvegarde")));
      EasyMock.replay(documentService);
   }

   private void initDocumentService() throws DfceException {

      Document document = new DocumentImpl();
      document.setUuid(UUID.randomUUID());
      document.addCriterion(Constants.CODE_ORG_GEST, "UR200");
      document.addCriterion(Constants.CODE_ORG_PROP, "UR100");

      EasyMock.expect(
            documentService.getDocument(EasyMock.anyObject(UUID.class)))
            .andReturn(document).once();
      documentService.updateDocument(EasyMock.anyObject(Document.class));
      EasyMock.expectLastCall();
      EasyMock.replay(documentService);
   }

   private void initDatasErreur() throws IOException {
      FileUtils.writeLines(inputFile, Arrays.asList(UUID.randomUUID()
            .toString()));
   }

   private void initDatas() throws IOException {
      FileUtils.writeLines(inputFile, Arrays.asList(UUID.randomUUID()
            .toString()));
      FileUtils.writeLines(propertiesFile, Arrays.asList("UR200=UR100"));
   }
}
