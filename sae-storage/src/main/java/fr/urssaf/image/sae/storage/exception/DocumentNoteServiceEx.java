package fr.urssaf.image.sae.storage.exception;

/**
 * Erreur levée lorsqu'une erreur se produit lors de l'ajout d'une note à un
 * document
 * 
 */
public class DocumentNoteServiceEx extends Exception {
   private String codeError;

   /**
    * Identifiant unique qui caractérise l'excepion
    */
   private static final long serialVersionUID = -6505532287814387009L;

   /**
    * Construit une nouvelle {@link RetrievalServiceEx }.
    */
   public DocumentNoteServiceEx() {
      super();
   }

   /**
    * Construit une nouvelle {@link DocumentNoteServiceEx } avec un message.
    * 
    * @param message
    *           Le message de l'erreur
    */
   public DocumentNoteServiceEx(final String message) {
      super(message);
   }

   /**
    * Construit une nouvelle {@link DocumentNoteServiceEx } avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public DocumentNoteServiceEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link DocumentNoteServiceEx } avec un message ,une
    * cause données et un code erreur donné .
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    * @param codeErreur
    *           : Le code d'erreur
    */
   public DocumentNoteServiceEx(final String codeErreur, final String message,
         final Throwable cause) {
      super(message, cause);
      setCodeError(codeErreur);
   }

   /**
    * @param codeError
    *           : Le code erreur.
    */
   public final void setCodeError(final String codeError) {
      this.codeError = codeError;
   }

   /**
    * @return Le code erreur.
    */
   public final String getCodeError() {
      return codeError;
   }
}
