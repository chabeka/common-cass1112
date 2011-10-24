package fr.urssaf.image.sae.ecde.exception;


/**
 * Lorsqu'il y a un problème lors de la vérification de XML à partir
 * <br>du schéma XSD correspondant.
 * 
 *
 */
public class EcdeXsdException extends EcdeGeneralException {
   
   private static final long serialVersionUID = 1L;

   /**
    * Constructor
    * 
    * @param message message a afficher
    * @param throwable cause
    */
   public EcdeXsdException(String message, Throwable throwable) {
      super(message, throwable);
   }
}
