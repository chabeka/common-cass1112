/**
 * 
 */
package fr.urssaf.image.sae.services.batch.restore.listener;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.restore.support.traces.TracesServicesRestoreMasseSupport;

/**
 * Listener du job restore de masse
 * 
 */
@Component
public class RestoreMasseJobListener {

   @Autowired
   private TracesServicesRestoreMasseSupport tracesSupport;

   /**
    * Initialisation des variables nécessaires au bon déroulement du job
    * 
    * @param jobExecution
    *           le jobExecution
    */
   @BeforeJob
   public final void init(JobExecution jobExecution) {

      ExecutionContext context = jobExecution.getExecutionContext();

      ConcurrentLinkedQueue<Exception> listExceptions = new ConcurrentLinkedQueue<Exception>();
      context.put(Constantes.RESTORE_EXCEPTION, listExceptions);

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

      // Ecriture d'une trace d'échec de restore de masse, le cas échéant
      tracesSupport.traceEchecRestoreMasse(jobExecution);
   }

}