package fr.urssaf.image.sae.ecde.exception;

/**
 * 
 * Classe mère des exceptions non runtime levées par sae-ecde
 * <br>
 * L'exception hérite de {@link Exception}
 * 
 * */

public class EcdeGeneralException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * implémentation du message par défaut
    * 
    * @param cause
    *           raison de l'exception levée
    */
   public EcdeGeneralException(String cause) {
      super(cause);
   }
   
   /**
    * implémentation du message par défaut
    * 
    * @param message a afficher
    * @param throwable
    *           raison de l'exception levée
    */
   public EcdeGeneralException(String message, Throwable throwable) {
      super(message, throwable);
   }
}
