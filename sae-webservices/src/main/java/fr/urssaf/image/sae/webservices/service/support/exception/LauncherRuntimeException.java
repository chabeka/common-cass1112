package fr.urssaf.image.sae.webservices.service.support.exception;

/**
 * Cette exception permet d'encapsuler les exceptions non
 * {@link RuntimeException} levées dans l'implémentation des support pour les
 * traitements<br>
 * <br>
 * L'exception hérite de {@link RuntimeException}
 * 
 * 
 */
public class LauncherRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param message
    *           message de l'exception
    * @param cause
    *           cause de l'exception
    */
   public LauncherRuntimeException(String message, Throwable cause) {

      super(message, cause);
   }

}
