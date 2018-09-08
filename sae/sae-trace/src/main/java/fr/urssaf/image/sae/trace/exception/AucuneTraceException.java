/**
 * 
 */
package fr.urssaf.image.sae.trace.exception;

/**
 * Erreur levée lorsqu'une recherche est effectuée et qu'elle ne retourne aucun
 * résultat
 * 
 */
public class AucuneTraceException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    */
   public AucuneTraceException(String message) {
      super(message);
   }

}
