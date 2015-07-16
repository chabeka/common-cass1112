package fr.urssaf.image.sae.services.exception;

/**
 * Erreur levée lorsqu’une erreur se produit lors de l’ajout d’une note à un document.
 */
public class SAEDocumentNoteException extends Exception {

   
 
   
   /**
    * Identifiant unique qui caractérise l'excepion
    */
   private static final long serialVersionUID = 1L;


   /**
    * Construit une nouvelle {@link SAEDocumentNoteException}.
    */
   public SAEDocumentNoteException() {
      super();
   }

   /**
    * Construit une nouvelle {@link SAEDocumentNoteException} avec un
    * message.
    * 
    * @param message
    *           Le message de l'erreur
    */
   public SAEDocumentNoteException(final String message) {
      super(message);
   }

   /**
    * Construit une nouvelle {@link SAEDocumentNoteException} avec un
    * message et une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public SAEDocumentNoteException(final String message,
         final Throwable cause) {
      super(message, cause);
   }




}
