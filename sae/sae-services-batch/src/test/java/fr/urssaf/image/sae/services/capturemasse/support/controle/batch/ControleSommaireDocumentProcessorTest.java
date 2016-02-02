package fr.urssaf.image.sae.services.capturemasse.support.controle.batch;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireDocumentNotFoundException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.CaptureMasseControleSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.batch.ControleSommaireDocumentProcessor;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.model.CaptureMasseControlResult;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-capturemasse-test.xml",
"/applicationContext-sae-services-capturemasse-test-mock-storagedocument.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ControleSommaireDocumentProcessorTest {

   @Autowired
   private ControleSommaireDocumentProcessor processor;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private CaptureMasseControleSupport cmControleSupport;

   private EcdeTestSommaire ecdeTestSommaire;

   private StepExecution stepExecution;

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      JobExecution jobExecution = new JobExecution(Long.valueOf(1));
      stepExecution = new StepExecution("controleDocuments", jobExecution);
   }

   @After
   public void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien à faire
      }
   }

   @SuppressWarnings("unchecked")
   @Test
   public void processWithoutIdentificationAndValidation() throws Exception {

      CaptureMasseControlResult resultat = new CaptureMasseControlResult();

      int nbElementInContext = launchControle(resultat);

      Assert.assertEquals("La map n'aurait pas du être ajoutée dans le contexte spring batch", nbElementInContext, stepExecution.getJobExecution().getExecutionContext().size());
      Map<String, CaptureMasseControlResult> map = (Map<String, CaptureMasseControlResult>) stepExecution.getJobExecution().getExecutionContext().get("mapCaptureControlResult");
      Assert.assertNull("La map ne doit pas être dans le contexte spring batch", map);

      EasyMock.reset(cmControleSupport);
   }

   @SuppressWarnings("unchecked")
   @Test
   public void processWithIdentificationOnly() throws Exception {

      CaptureMasseControlResult resultat = new CaptureMasseControlResult();
      resultat.setIdentificationActivee(Boolean.TRUE);

      int nbElementInContext = launchControle(resultat);

      Assert.assertEquals("La map aurait du être ajoutée dans le contexte spring batch", nbElementInContext + 1, stepExecution.getJobExecution().getExecutionContext().size());
      Map<String, CaptureMasseControlResult> map = (Map<String, CaptureMasseControlResult>) stepExecution.getJobExecution().getExecutionContext().get("mapCaptureControlResult");
      Assert.assertNotNull("La map doit être dans le contexte spring batch", map);
      Assert.assertTrue("La map doit contenir au moins un resultat de controle" , map.size() > 0);

      EasyMock.reset(cmControleSupport);
   }

   @SuppressWarnings("unchecked")
   @Test
   public void processWithValidationOnly() throws Exception {

      CaptureMasseControlResult resultat = new CaptureMasseControlResult();
      resultat.setValidationActivee(Boolean.TRUE);

      int nbElementInContext = launchControle(resultat);

      Assert.assertEquals("La map aurait du être ajoutée dans le contexte spring batch", nbElementInContext + 1, stepExecution.getJobExecution().getExecutionContext().size());
      Map<String, CaptureMasseControlResult> map = (Map<String, CaptureMasseControlResult>) stepExecution.getJobExecution().getExecutionContext().get("mapCaptureControlResult");
      Assert.assertNotNull("La map doit être dans le contexte spring batch", map);
      Assert.assertTrue("La map doit contenir au moins un resultat de controle" , map.size() > 0);

      EasyMock.reset(cmControleSupport);
   }

   @SuppressWarnings("unchecked")
   @Test
   public void processWithIdentificationAndValidation() throws Exception {

      CaptureMasseControlResult resultat = new CaptureMasseControlResult();
      resultat.setIdentificationActivee(Boolean.TRUE);
      resultat.setValidationActivee(Boolean.TRUE);

      int nbElementInContext = launchControle(resultat);

      Assert.assertEquals("La map aurait du être ajoutée dans le contexte spring batch", nbElementInContext + 1, stepExecution.getJobExecution().getExecutionContext().size());
      Map<String, CaptureMasseControlResult> map = (Map<String, CaptureMasseControlResult>) stepExecution.getJobExecution().getExecutionContext().get("mapCaptureControlResult");
      Assert.assertNotNull("La map doit être dans le contexte spring batch", map);
      Assert.assertTrue("La map doit contenir au moins un resultat de controle" , map.size() > 0);

      EasyMock.reset(cmControleSupport);
   }

   private int launchControle(CaptureMasseControlResult resultat) throws CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx,
      UnknownMetadataEx, DuplicatedMetadataEx,
      InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
      RequiredArchivableMetadataEx, UnknownHashCodeEx, UnknownCodeRndEx,
      MetadataValueNotInDictionaryEx, UnknownFormatException,
      ValidationExceptionInvalidFile, Exception {
      EasyMock.expect(cmControleSupport.controleSAEDocument(EasyMock
            .anyObject(UntypedDocument.class), EasyMock.anyObject(File.class))).andReturn(
                  resultat).once();

      EasyMock.replay(cmControleSupport);

      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      File sommaire = new File(ecdeDirectory, "sommaire.xml");

      UntypedDocument document = new UntypedDocument();

      stepExecution.getJobExecution().getExecutionContext().put(Constantes.SOMMAIRE_FILE, sommaire.getPath());

      processor.beforeStep(stepExecution);

      int nbElementInContext = stepExecution.getJobExecution().getExecutionContext().size();

      processor.process(document);
      return nbElementInContext;
   }
}
