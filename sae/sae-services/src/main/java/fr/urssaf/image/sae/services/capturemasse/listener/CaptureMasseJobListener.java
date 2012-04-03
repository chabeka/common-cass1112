/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.listener;

import java.util.ArrayList;
import java.util.List;

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
   public void init(JobExecution jobExecution) {

      ExecutionContext context = jobExecution.getExecutionContext();

      List<Integer> listIndex = new ArrayList<Integer>();
      context.put(Constantes.INDEX_EXCEPTION, listIndex);

      List<String> listCodes = new ArrayList<String>();
      context.put(Constantes.CODE_EXCEPTION, listCodes);

      List<Exception> listExceptions = new ArrayList<Exception>();
      context.put(Constantes.DOC_EXCEPTION, listExceptions);
   }

}
