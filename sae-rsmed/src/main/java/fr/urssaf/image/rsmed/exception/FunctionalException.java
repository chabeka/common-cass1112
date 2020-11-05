/**
 * 
 */
package fr.urssaf.image.rsmed.exception;


/**
 * Erreur levée lors d'un problème de traitement de flux XML
 * 
 */
public class FunctionalException extends RuntimeException {

   private static final long serialVersionUID = 8837797145872909058L;

   /**
    * Constructeur
    * @param exception exception mère
    */
   public FunctionalException(Exception exception) {
      super(exception);
   }

}
