package fr.urssaf.image.sae.igc.exception;

/**
 * Une erreur s'est produite lors du téléchargement des éléments de l'IGC.
 * 
 * 
 */
public class IgcDownloadException extends Exception {

   private static final long serialVersionUID = 1L;

   public static final String MESSAGE = "Une erreur s'est produite lors du téléchargement des éléments de l'IGC";

   /**
    * implémentation du message par défaut {@value #MESSAGE}
    * 
    * @param cause
    *           raison de l'exception levée
    */
   public IgcDownloadException(Throwable cause) {
      super(MESSAGE, cause);

   }
}
