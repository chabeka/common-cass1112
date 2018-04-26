package fr.urssaf.image.sae.pile.travaux.exception;


/**
 * Exception levée lorsque la tentative de lock 
 * lors de la tentative de réservation d'un job n'aboutit
 * pas assez rapidement
 *
 */
public class LockTimeoutException extends Exception {

   /**
    * Constructeur
    * @param message  Le message d'erreur
    */
   public LockTimeoutException(String message) {
      super(message);
   }

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   
}
