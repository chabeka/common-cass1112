package fr.urssaf.image.sae.services.exception;

/**
 * Erreur levée lorsqu’une erreur se produit lors de l’ajout d’une pièce jointe
 * à un document.
 */
public class SAEDocumentAttachmentEx extends Exception {

   /**
    * Identifiant unique qui caractérise l'excepion
    */
   private static final long serialVersionUID = 1L;

   /**
    * Le message de l'exception
    */
   protected static final String MSG_EXCEPTION = "Une exception a eu lieu lors de l'ajout d'un document attaché";
   
   /**
    * Construit une nouvelle {@link SAEDocumentAttachmentEx}.
    */
   public SAEDocumentAttachmentEx() {
      super(MSG_EXCEPTION);
   }

   /**
    * Construit une nouvelle {@link SAEDocumentAttachmentEx} avec un
    * message.
    * 
    * @param message
    *           Le message de l'erreur
    */
   public SAEDocumentAttachmentEx(final String message) {
      super(message);
   }

   /**
    * le message de l'exception est {@value #MSG_EXCEPTION}
    * 
    * @param cause
    *           cause de l'exception
    */
   public SAEDocumentAttachmentEx(Throwable cause) {
      super(MSG_EXCEPTION, cause);
   }

   /**
    * Construit une nouvelle {@link SAEDocumentAttachmentEx} avec un
    * message et une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public SAEDocumentAttachmentEx(final String message,
         final Throwable cause) {
      super(message, cause);
   }

}
