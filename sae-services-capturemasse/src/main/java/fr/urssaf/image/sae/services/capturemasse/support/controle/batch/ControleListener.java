/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.listener.CheckStateAfterStepListener;

/**
 * Ecouteur pour la partie contrôle des documents du fichier sommaire.xml
 * 
 */
@Component
public class ControleListener extends CheckStateAfterStepListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleListener.class);

   /**
    * erreur au moment du read
    * 
    * @param exception
    *           exception levée
    */
   @OnReadError
   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   /*
    * Alerte PMD car nous avons obligation de caster l'erreur afin de
    * pouvoir l'exploiter plus tard
    */
   public final void logReadError(final Exception exception) {
      LOGGER.warn("une erreur interne à l'application est survenue "
            + "lors du traitement de la capture de masse", exception);

      getCodesErreurListe().add(Constantes.ERR_BUL001);
      getIndexErreurListe().add(0);
      getExceptionErreurListe().add(new Exception(exception.getMessage()));

   }
}
