/**
 * 
 */
package fr.urssaf.image.sae.services.batch.reprise.listener;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.reprise.support.traces.TracesServicesRepriseMasseSupport;

/**
 * Listener du job modification en masse
 * 
 */
@Component
public class RepriseMasseJobListener {

   /**
    * Support de tracabilité
    */
   @Autowired
   private TracesServicesRepriseMasseSupport tracesSupport;

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

      ConcurrentLinkedQueue<Integer> listIndexDocumentDone = new ConcurrentLinkedQueue<Integer>();
      context.put(Constantes.INDEX_DOCUMENT_DONE, listIndexDocumentDone);
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
      tracesSupport.traceEchecRepriseMasse(jobExecution);

   }

}
