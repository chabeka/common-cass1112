/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.controle.batch;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.item.ExecutionContext;

import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Classe mère des listeners de controle de documents
 * 
 */
public abstract class AbstractControleDocumentsListener extends
      AbstractListener {

   /**
    * Erreur de transformation
    * 
    * @param untypedType
    *           le document sur lequel s'est produit l'erreur
    * @param exception
    *           exception levée
    */
   @OnProcessError
   @SuppressWarnings( { "PMD.AvoidThrowingRawExceptionTypes" })
   public final void logProcessError(final JAXBElement<Object> untypedType,
         final Exception exception) {

      getCodesErreurListe().add(Constantes.ERR_BUL002);
      getIndexErreurListe().add(
            getStepExecution().getExecutionContext().getInt(
                  Constantes.CTRL_INDEX));
      getExceptionErreurListe().add(new Exception(exception.getMessage()));

      getLogger().error(
            "une erreur est survenue lors des controles des documents",
            exception);

   }

   /**
    * Action exécutée avant chaque process
    * 
    * @param untypedType
    *           le document
    */
   @BeforeProcess
   public final void beforeProcess(final JAXBElement<Object> untypedType) {

      ExecutionContext context = getStepExecution().getExecutionContext();

      int valeur = context.getInt(Constantes.CTRL_INDEX);
      valeur++;

      context.put(Constantes.CTRL_INDEX, valeur);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void specificInitOperations() {
      getStepExecution().getExecutionContext().put(Constantes.CTRL_INDEX, -1);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ExitStatus specificAfterStepOperations() {
      return getStepExecution().getExitStatus();
   }

   /**
    * @return le message de log
    */
   protected abstract String getLogMessage();

   /**
    * @return le logger
    */
   protected abstract Logger getLogger();

}
