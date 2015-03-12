/**
 * 
 */
package fr.urssaf.image.sae.droit.exception;

/**
 * Exception levée lorsque plusieurs domaine distincts 
 * sont trouvées dans les Prmds lors de l'archivage d'un document
 * 
 */
public class InvalidPagmsCombinaisonException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'erreur
    */
   public InvalidPagmsCombinaisonException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'erreur
    * @param cause
    *           cause mère
    */
   public InvalidPagmsCombinaisonException(String message, Throwable cause) {
      super(message, cause);
   }
}
