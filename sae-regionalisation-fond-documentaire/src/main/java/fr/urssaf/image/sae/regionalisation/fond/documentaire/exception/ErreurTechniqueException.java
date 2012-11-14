/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.exception;


/**
 * Erreur soulevée lors de toute erreur technique
 * 
 */
public class ErreurTechniqueException extends RuntimeException {

   private static final long serialVersionUID = -3345271951202394005L;

   /**
    * Constructeur
    * 
    * @param message
    *           erreur mère
    */
   public ErreurTechniqueException(String message) {
      super(message);
   }

   /**
    * constructeur
    * 
    * @param exception
    *           exception mère
    */
   public ErreurTechniqueException(Exception exception) {
      super(exception);
   }

}
