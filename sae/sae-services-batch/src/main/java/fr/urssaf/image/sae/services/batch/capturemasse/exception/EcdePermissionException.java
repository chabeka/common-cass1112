/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.exception;

/**
 * Exception dédiée aux problèmes d'accès de répertoire Ecde
 * 
 */
public class EcdePermissionException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param cause
    *           l'exception mère
    */
   public EcdePermissionException(Throwable cause) {
      super(cause);
   }

}
