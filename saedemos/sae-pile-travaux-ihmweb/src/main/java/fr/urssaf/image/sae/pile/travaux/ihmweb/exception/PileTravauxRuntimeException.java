package fr.urssaf.image.sae.pile.travaux.ihmweb.exception;

/**
 * Exception Runtime générale pour l'application
 */
public class PileTravauxRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 6240022624845566300L;
   
   /**
    * Constructeur
    * @param cause cause mère
    */
   public PileTravauxRuntimeException(Throwable cause) {
      super(cause);
   }

}
