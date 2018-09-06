/**
 * 
 */
package fr.urssaf.image.sae.commons.exception;


/**
 * Erreur levée lors d'un problème de traitement de flux XML
 * 
 */
public class StaxRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 8837797145872909058L;

   /**
    * Constructeur
    * @param exception exception mère
    */
   public StaxRuntimeException(Exception exception) {
      super(exception);
   }

}
