/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification.listener;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.modification.support.traces.TracesServicesModificationMasseSupport;

/**
 * Listener du job modification en masse
 * 
 */
@Component
public class ModificationMasseJobListener {

   /**
    * Support de tracabilité
    */
   @Autowired
   private TracesServicesModificationMasseSupport tracesSupport;

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

      ConcurrentLinkedQueue<String> listExceptions = new ConcurrentLinkedQueue<String>();
      context.put(Constantes.DOC_EXCEPTION, listExceptions);

      ConcurrentLinkedQueue<String> listRollbackExceptions = new ConcurrentLinkedQueue<String>();
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
      tracesSupport.traceEchecModificationMasse(jobExecution);

   }

}
