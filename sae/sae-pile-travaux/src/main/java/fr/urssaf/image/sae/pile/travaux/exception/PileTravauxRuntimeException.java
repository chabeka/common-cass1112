package fr.urssaf.image.sae.pile.travaux.exception;

/**
 * Exception Runtime générale pour le composant sae-pile-travaux
 */
public class PileTravauxRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 8335158719526445621L;

   /**
    * Constructeur
    * 
    * @param message
    *           le message de l'exception
    */
   public PileTravauxRuntimeException(String message) {
      super(message);
   }

}
