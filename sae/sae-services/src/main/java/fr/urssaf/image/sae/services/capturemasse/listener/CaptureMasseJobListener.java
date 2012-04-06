/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.listener;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

/**
 * Listener du job capture de masse
 * 
 */
@Component
public class CaptureMasseJobListener {

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

      ConcurrentLinkedQueue<String> listCodes = new ConcurrentLinkedQueue<String>();
      context.put(Constantes.CODE_EXCEPTION, listCodes);

      ConcurrentLinkedQueue<Exception> listExceptions = new ConcurrentLinkedQueue<Exception>();
      context.put(Constantes.DOC_EXCEPTION, listExceptions);
   }

}
