/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.batch;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.FichierType;

/**
 * Ecouteur pour la partie contrôle des documents du fichier sommaire.xml
 * 
 */
@Component
public class ControleReferenceListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleReferenceListener.class);

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
    * Erreur de transformation
    * 
    * @param fichierType
    *           l'objet numérique sur lequel s'est produit l'erreur
    * @param exception
    *           exception levée
    */
   @OnProcessError
   @SuppressWarnings( { "unchecked", "PMD.AvoidThrowingRawExceptionTypes" })
   public final void logProcessError(
         final JAXBElement<FichierType> fichierType, final Exception exception) {

      ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();

      ConcurrentLinkedQueue<String> listCodes = (ConcurrentLinkedQueue<String>) context
            .get(Constantes.CODE_EXCEPTION);
      ConcurrentLinkedQueue<Integer> listIndex = (ConcurrentLinkedQueue<Integer>) context
            .get(Constantes.INDEX_EXCEPTION);
      ConcurrentLinkedQueue<Exception> listExceptions = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      listCodes.add(Constantes.ERR_BUL002);
      listIndex.add(stepExecution.getExecutionContext().getInt(
            Constantes.CTRL_INDEX));
      listExceptions.add(new Exception(exception.getMessage()));

      LOGGER
            .warn(
                  "une erreur est survenue lors de la conversion du fichier de référence",
                  exception);

   }
}
