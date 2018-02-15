/**
 * 
 */
package fr.urssaf.image.sae.commons.exception;

/**
 * Exception runtime levée lors des opérations sur les paramètres
 * 
 */
public class ParameterRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 8474761206129757663L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ParameterRuntimeException(String message) {
      super(message);
   }

   /**
    * constructeur
    * 
    * @param message
    *           message d'erreur
    * @param exception
    *           erreur d'origine
    */
   public ParameterRuntimeException(String message, Exception exception) {
      super(message, exception);
   }

}
