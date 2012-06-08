package fr.urssaf.image.sae.anais.portail.exception;

/**
 * Exception runtime de l'application
 */
public class PortailRuntimeException extends RuntimeException {

   private static final long serialVersionUID = -8753111619097949997L;

   /**
    * Constructeur
    * 
    * @param message
    *           le message de l'exception
    */
   public PortailRuntimeException(String message) {
      super(message);
   }

}
