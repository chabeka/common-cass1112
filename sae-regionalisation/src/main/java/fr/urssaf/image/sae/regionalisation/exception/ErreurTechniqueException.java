package fr.urssaf.image.sae.regionalisation.exception;

/**
 * Erreur levée lors d'une erreur technique
 * 
 * 
 */
public class ErreurTechniqueException extends RuntimeException {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param cause
    *           cause de l'exception
    */
   public ErreurTechniqueException(Throwable cause) {
      super(cause);
   }

}
