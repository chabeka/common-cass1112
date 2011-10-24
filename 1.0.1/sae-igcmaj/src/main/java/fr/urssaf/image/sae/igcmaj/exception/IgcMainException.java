package fr.urssaf.image.sae.igcmaj.exception;

/**
 * Classe d'exception lorsque une exception est levé lors du téléchargement des
 * CRL
 * 
 * 
 */
public class IgcMainException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   public static final String MESSAGE = "Une erreur s’est produite lors du téléchargement des CRL";

   /**
    * implémentation du message par défaut {@value #MESSAGE}
    * 
    * @param cause
    *           raison de l'exception levée
    */
   public IgcMainException(Throwable cause) {

      super(MESSAGE, cause);
   }

}
