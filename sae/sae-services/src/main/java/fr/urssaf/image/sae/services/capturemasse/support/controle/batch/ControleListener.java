/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.batch;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;

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
    * réalisé avant le step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void init(final StepExecution stepExecution) {
      this.stepExecution = stepExecution;
      this.stepExecution.getExecutionContext().put(Constantes.CTRL_INDEX, -1);
   }

   /**
    * Méthode réalisée à la fin du step
    * 
    * @param stepExecution
    *           le StepExecution
    * @return le status de sortie
    */
   @AfterStep
   public final ExitStatus end(final StepExecution stepExecution) {
      ExitStatus exitStatus = ExitStatus.FAILED;

      if (stepExecution.getJobExecution().getExecutionContext().get(
            Constantes.DOC_EXCEPTION) == null) {
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
   public final void logReadError(final Exception exception) {
      LOGGER.error("une erreur interne à l'application est survenue "
            + "lors du traitement de la capture de masse", exception);

      final CaptureMasseSommaireDocumentException documentException = new CaptureMasseSommaireDocumentException(
            0, exception);

      stepExecution.getJobExecution().getExecutionContext().put(
            Constantes.DOC_EXCEPTION, documentException);

   }

   /**
    * Erreur de transformation
    * 
    * @param untypedType
    *           le document sur lequel s'est produit l'erreur
    * @param exception
    *           exception levée
    */
   @OnProcessError
   public final void logProcessError(
         final JAXBElement<UntypedDocument> untypedType,
         final Exception exception) {

      final CaptureMasseSommaireDocumentException documentException = new CaptureMasseSommaireDocumentException(
            stepExecution.getExecutionContext().getInt(Constantes.CTRL_INDEX),
            exception);

      stepExecution.getJobExecution().getExecutionContext().put(
            Constantes.DOC_EXCEPTION, documentException);

   }

   @BeforeProcess
   public void beforeProcess(final JAXBElement<UntypedDocument> untypedType) {

      ExecutionContext context = stepExecution.getExecutionContext();

      int valeur = context.getInt(Constantes.CTRL_INDEX);
      valeur++;

      context.put(Constantes.CTRL_INDEX, valeur);

   }
}
