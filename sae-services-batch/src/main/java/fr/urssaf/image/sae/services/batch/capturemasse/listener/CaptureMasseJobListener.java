/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.listener;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.traces.TracesServicesSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Listener du job capture de masse
 * 
 */
@Component
public class CaptureMasseJobListener {

   @Autowired
   private TracesServicesSupport tracesSupport;

   /**
    * Initialisation des variables nécessaires au bon déroulement du job
    * 
    * @param jobExecution
    *           le jobExecution
    */
   @BeforeJob
   public final void init(JobExecution jobExecution) {

      ExecutionContext context = jobExecution.getExecutionContext();

      ConcurrentLinkedQueue<Integer> listIndex = new ConcurrentLinkedQueue<Integer>();
      context.put(Constantes.INDEX_EXCEPTION, listIndex);
      
      ConcurrentLinkedQueue<Integer> listRefIndex = new ConcurrentLinkedQueue<Integer>();
      context.put(Constantes.INDEX_REF_EXCEPTION, listRefIndex);

      ConcurrentLinkedQueue<String> listCodes = new ConcurrentLinkedQueue<String>();
      context.put(Constantes.CODE_EXCEPTION, listCodes);

      ConcurrentLinkedQueue<Exception> listExceptions = new ConcurrentLinkedQueue<Exception>();
      context.put(Constantes.DOC_EXCEPTION, listExceptions);

      ConcurrentLinkedQueue<Exception> listRollbackExceptions = new ConcurrentLinkedQueue<Exception>();
      context.put(Constantes.ROLLBACK_EXCEPTION, listRollbackExceptions);

   }

   /**
    * Méthode appelée après l'exécution du job, que ce soit en réussite ou en
    * échec
    * 
    * @param jobExecution
    *           le jobExecution
    */
   @AfterJob
   public final void afterJob(JobExecution jobExecution) {

      // Ecriture d'une trace d'échec de capture de masse, le cas échéant
      tracesSupport.traceEchecCaptureMasse(jobExecution);

   }

}
