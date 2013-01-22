/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatFileSuccessSupport;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.services.util.XmlValidationUtils;

/**
 * Tasklet pour l'écriture du fichier resultats.xml quand le traitement est en
 * succès
 * 
 */
@Component
public class ResultatsFileSuccessTasklet implements Tasklet {

   private static final String RESULTATS_XSD = "xsd_som_res/resultats.xsd";

   @Autowired
   private ResultatFileSuccessSupport successSupport;

   @Autowired
   private ApplicationContext context;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsFileSuccessTasklet.class);

   // Pool d'execution des insertions de documents
   @Autowired
   InsertionPoolThreadExecutor executor;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      final Map<String, Object> map = chunkContext.getStepContext()
            .getJobExecutionContext();

      final String path = (String) map.get(Constantes.SOMMAIRE_FILE);

      final File sommaireFile = new File(path);
      final File ecdeDirectory = sommaireFile.getParentFile();

      final ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> listIntDocs = executor
            .getIntegratedDocuments();

      int initCount = (Integer) map.get(Constantes.DOC_COUNT);

      boolean restitutionUuid = false;
      if (chunkContext.getStepContext()
            .getStepExecution().getJobExecution().getExecutionContext()
            .get(Constantes.RESTITUTION_UUIDS) != null) {
         restitutionUuid = (Boolean) (chunkContext.getStepContext()
               .getStepExecution().getJobExecution().getExecutionContext()
               .get(Constantes.RESTITUTION_UUIDS));
      }
      successSupport.writeResultatsFile(ecdeDirectory, listIntDocs, initCount,
            restitutionUuid, sommaireFile);

      File resultats = new File(ecdeDirectory, "resultats.xml");

      try {
         Resource sommaireXSD = context.getResource(RESULTATS_XSD);
         URL xsdSchema = sommaireXSD.getURL();

         XmlValidationUtils.parse(resultats, xsdSchema);
      } catch (IOException ioExcept) {
         LOGGER.warn("Erreur lors de la validation XSD", ioExcept);
      } catch (ParserConfigurationException parseExcept) {
         LOGGER.warn("Erreur lors de la validation XSD", parseExcept);
      } catch (SAXException saxExcept) {
         LOGGER.warn("Erreur lors de la validation XSD", saxExcept);
      }

      return RepeatStatus.FINISHED;
   }
}
