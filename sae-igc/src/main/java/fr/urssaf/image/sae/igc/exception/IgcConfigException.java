package fr.urssaf.image.sae.igc.exception;

/**
 * Une erreur s'est produite lors de la lecture ou de la vérification de la
 * configuration de l'IGC.
 * 
 * 
 */
public class IgcConfigException extends Exception {

   private static final long serialVersionUID = 1L;

   public static final String MESSAGE = "Une erreur s'est produite lors de la lecture ou de la vérification de la configuration de l'IGC";

   /**
    * implémenation d'un message spécifique
    * 
    * @param message
    *           message de l'exception levée
    */
   public IgcConfigException(String message) {
      super(message);
   }

   /**
    * implémentation du message par défaut {@value #MESSAGE}
    * 
    * @param cause
    *           raison de l'exception levée
    */
   public IgcConfigException(Throwable cause) {
      super(MESSAGE, cause);
   }

}
