/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseVirtualDocument;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatFileSuccessSupport;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadVirtualExecutor;
import fr.urssaf.image.sae.services.capturemasse.support.xsd.XsdValidationSupport;

/**
 * Tasklet pour 'écriture du fichier resultats.xml dans le cas d'un succès de
 * traitement pour les documents virtuels
 * 
 */
@Component
public class ResultatsVirtualFileSuccessTasklet implements Tasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsVirtualFileSuccessTasklet.class);

   @Autowired
   private ResultatFileSuccessSupport support;

   @Autowired
   private InsertionPoolThreadVirtualExecutor executor;
   
   @Autowired
   private XsdValidationSupport xsdValidationSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {
      String trcPrefix = "execute()";
      LOGGER.debug("{} - début", trcPrefix);

      Map<String, Object> map = chunkContext.getStepContext()
            .getJobExecutionContext();
      String pathSommaire = (String) map.get(Constantes.SOMMAIRE_FILE);
      File sommaireFile = new File(pathSommaire);
      File ecdeDirectory = sommaireFile.getParentFile();

      final ConcurrentLinkedQueue<CaptureMasseVirtualDocument> listIntDocs = executor
            .getIntegratedDocuments();

      int initCount = (Integer) map.get(Constantes.DOC_COUNT);

      boolean restitutionUuid = false;
      if (chunkContext.getStepContext().getStepExecution().getJobExecution()
            .getExecutionContext().get(Constantes.RESTITUTION_UUIDS) != null) {
         restitutionUuid = (Boolean) (chunkContext.getStepContext()
               .getStepExecution().getJobExecution().getExecutionContext()
               .get(Constantes.RESTITUTION_UUIDS));
      }

      support.writeVirtualResultatsFile(ecdeDirectory, listIntDocs, initCount,
            restitutionUuid, sommaireFile);

      File resultats = new File(ecdeDirectory, "resultats.xml");

      xsdValidationSupport.resultatsValidation(resultats);
      
      LOGGER.debug("{} - fin", trcPrefix);
      
      return RepeatStatus.FINISHED;
   }

}
