package fr.urssaf.image.sae.services.exception.capture;

import fr.urssaf.image.sae.services.exception.SAEServiceException;

/**
 * Exception à utiliser pour les erreurs de dans la partie capture.<BR/>
 */
public class SAECaptureServiceEx extends SAEServiceException {

   private static final long serialVersionUID = 1L;

   private static final String MSG_EXCEPTION = "Une exception a eu lieu dans la capture";

   /**
    * le message de l'exception est {@value #MSG_EXCEPTION}
    * 
    */
   public SAECaptureServiceEx() {
      super(MSG_EXCEPTION);
   }

   /**
    * le message de l'exception est {@value #MSG_EXCEPTION}
    * 
    * @param cause
    *           cause de l'exception
    */
   public SAECaptureServiceEx(Throwable cause) {
      super(MSG_EXCEPTION, cause);
   }

   /**
    * Construit une nouvelle {@link SAECaptureServiceEx } avec un message et une
    * cause données.
    * 
    * @param message
    *           : Message de l'erreur
    * @param cause
    *           : Cause de l'erreur
    */
   public SAECaptureServiceEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link SAECaptureServiceEx } avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public SAECaptureServiceEx(final String message) {
      super(message);
   }

   /**
    * Construit une nouvelle {@link SAECaptureServiceEx } avec un message ,une
    * cause données et un code erreur donné .
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    * @param codeErreur
    *           : Le code d'erreur
    */
   public SAECaptureServiceEx(final String codeErreur, final String message,
         final Throwable cause) {
      super(message, cause);
      setCodeError(codeErreur);
   }
}
