/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.controle.batch;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.FichierType;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Ecouteur pour la partie contrôle des documents du fichier sommaire.xml
 * 
 */
@Component
public class ControleReferenceListener extends AbstractListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleReferenceListener.class);

   /**
    * Erreur de transformation
    * 
    * @param fichierType
    *           l'objet numérique sur lequel s'est produit l'erreur
    * @param exception
    *           exception levée
    */
   @OnProcessError
   @SuppressWarnings( { "PMD.AvoidThrowingRawExceptionTypes" })
   public final void logProcessError(
         final JAXBElement<FichierType> fichierType, final Exception exception) {

      getCodesErreurListe().add(Constantes.ERR_BUL002);
      getIndexReferenceErreurListe().add(
            getStepExecution().getExecutionContext().getInt(
                  Constantes.CTRL_REF_INDEX));
      getExceptionErreurListe().add(new Exception(exception.getMessage()));

      LOGGER
            .warn(
                  "une erreur est survenue lors de la conversion du fichier de référence",
                  exception);

   }

   /**
    * Action exécutée avant chaque process
    * 
    * @param untypedType
    *           le document
    */
   @BeforeProcess
   public final void beforeProcess(final JAXBElement<FichierType> untypedType) {

      ExecutionContext context = getStepExecution().getExecutionContext();

      int valeur = context.getInt(Constantes.CTRL_REF_INDEX);
      valeur++;

      context.put(Constantes.CTRL_REF_INDEX, valeur);

   }

   /**
    * erreur au moment du read
    * 
    * @param exception
    *           exception levée
    */
   @OnReadError
   @SuppressWarnings( { "PMD.AvoidThrowingRawExceptionTypes" })
   public final void logReadError(final Exception exception) {
      LOGGER.warn("une erreur interne à l'application est survenue "
            + "lors du traitement de la capture de masse", exception);

      getCodesErreurListe().add(Constantes.ERR_BUL001);
      getIndexReferenceErreurListe().add(0);
      getExceptionErreurListe().add(new Exception(exception.getMessage()));

   }

   @Override
   protected ExitStatus specificAfterStepOperations() {
      ExitStatus exitStatus = ExitStatus.FAILED;

      if (CollectionUtils.isEmpty(getExceptionErreurListe())) {
         exitStatus = ExitStatus.COMPLETED;
      }

      return exitStatus;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void specificInitOperations() {
      getStepExecution().getExecutionContext().put(Constantes.CTRL_INDEX, -1);
      getStepExecution().getExecutionContext().put(Constantes.CTRL_REF_INDEX,
            -1);

   }
}
