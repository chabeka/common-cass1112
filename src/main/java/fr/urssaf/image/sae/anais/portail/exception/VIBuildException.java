package fr.urssaf.image.sae.anais.portail.exception;

/**
 * Exception levée lors d'un problème de construction d'un VI
 */
public class VIBuildException extends Exception {

   private static final long serialVersionUID = 5399974886717831471L;

   /**
    * Constructeur
    * 
    * @param message
    *           le message de l'exception
    */
   public VIBuildException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param cause
    *           la cause mère
    */
   public VIBuildException(Throwable cause) {
      super(cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message de l'exception
    * @param cause
    *           la cause mère
    */
   public VIBuildException(String message, Throwable cause) {
      super(message, cause);
   }

}
