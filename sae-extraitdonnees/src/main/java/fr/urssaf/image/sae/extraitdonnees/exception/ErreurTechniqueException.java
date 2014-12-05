package fr.urssaf.image.sae.extraitdonnees.exception;

/**
 * Erreur soulevée lors de toute erreur technique
 */
public class ErreurTechniqueException extends RuntimeException {

   private static final long serialVersionUID = -7728434956751497048L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    * 
    */
   public ErreurTechniqueException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param exception
    *           exception mère
    */
   public ErreurTechniqueException(Exception exception) {
      super(exception);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    * @param cause
    *           exception mère
    */
   public ErreurTechniqueException(String message, Throwable cause) {
      super(message, cause);
   }

}
