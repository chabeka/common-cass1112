/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.batch;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

/**
 * Ecouteur pour la partie contrôle des documents du fichier sommaire.xml
 * 
 */
@Component
public class ControleListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleListener.class);

   private StepExecution stepExecution;
   
   /**
    * Etape réalisée avant le step
    * @param stepExecution
    */
   @BeforeStep
   public void init(final StepExecution stepExecution) {
      this.stepExecution = stepExecution;
   }

   /**
    * Méthode réalisée à la fin du step
    * 
    * @param stepExecution
    *           le StepExecution
    * @return le status de sortie
    */
   @SuppressWarnings("unchecked")
   @AfterStep
   public final ExitStatus end(final StepExecution stepExecution) {
      ExitStatus exitStatus = ExitStatus.FAILED;

      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) stepExecution
            .getJobExecution().getExecutionContext().get(
                  Constantes.DOC_EXCEPTION);
      if (CollectionUtils.isEmpty(exceptions)) {
         exitStatus = ExitStatus.COMPLETED;
      }

      return exitStatus;
   }

   /**
    * erreur au moment du read
    * 
    * @param exception
    *           exception levée
    */
   @OnReadError
   @SuppressWarnings( { "unchecked", "PMD.AvoidThrowingRawExceptionTypes" })
   public final void logReadError(final Exception exception) {
      LOGGER.warn("une erreur interne à l'application est survenue "
            + "lors du traitement de la capture de masse", exception);

      ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();
      ConcurrentLinkedQueue<String> listCodes = (ConcurrentLinkedQueue<String>) context
            .get(Constantes.CODE_EXCEPTION);
      ConcurrentLinkedQueue<Integer> listIndex = (ConcurrentLinkedQueue<Integer>) context
            .get(Constantes.INDEX_EXCEPTION);
      ConcurrentLinkedQueue<Exception> listExceptions = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      listCodes.add(Constantes.ERR_BUL001);
      listIndex.add(0);
      listExceptions.add(new Exception(exception.getMessage()));

   }
}
