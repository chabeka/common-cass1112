package fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.exception;

import java.text.MessageFormat;

import org.springframework.util.Assert;

import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;

/**
 * Exception levée lorsque la reprise d'un traitement après une interruption a
 * échoué
 * 
 * 
 */
public class InterruptionTraitementException extends Exception {

   private static final long serialVersionUID = 1L;

   private static final String EXCEPTION_MESSAGE = "Après une déconnexion DFCE programmée à {0} il est impossible de reprendre le traitement après {1} secondes et {2} tentatives.";

   private final InterruptionTraitementConfig interruption;

   /**
    * 
    * @param interruption
    *           configuration de l'arrêt du traitement
    * @param cause
    *           cause de l'exception
    */
   public InterruptionTraitementException(
         final InterruptionTraitementConfig interruption, final Throwable cause) {

      super(cause);
      Assert.notNull(interruption, "'interruption' is required");
      this.interruption = interruption;

   }
}
