package fr.urssaf.image.sae.services.exception.consultation;

/**
 * Exception levée lorsque que le paramétrage pour le découpage du fichier est
 * incorrect.
 * 
 * 
 */
public class SAEConsultationAffichableParametrageException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle
    * {@link SAEConsultationAffichableParametrageException} avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public SAEConsultationAffichableParametrageException(final String message,
         final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle
    * {@link SAEConsultationAffichableParametrageException} avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public SAEConsultationAffichableParametrageException(final String message) {
      super(message);
   }

   /**
    * Construit une nouvelle
    * {@link SAEConsultationAffichableParametrageException} avec l'erreur source
    * 
    * @param cause
    *           l'erreur source
    */
   public SAEConsultationAffichableParametrageException(Throwable cause) {
      super(cause);
   }
}
