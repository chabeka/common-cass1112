package fr.urssaf.image.sae.webservices.exception;

/**
 * Classe d'encapsulation des exceptions non spécifiées pour la capture en masse<br>
 * <br>
 * L'exception étend {@link RuntimeException}
 * 
 * 
 */
public class CaptureMasseRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /***
    *
    * @param message
    *           message de l'exception
    * @param cause
    *           exception non spécifié encapsulée
    */
   public CaptureMasseRuntimeException(String message, Throwable cause) {
      super(message, cause);
   }

}
