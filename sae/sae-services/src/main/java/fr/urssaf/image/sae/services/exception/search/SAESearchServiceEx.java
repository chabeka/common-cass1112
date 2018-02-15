package fr.urssaf.image.sae.services.exception.search;

/**
 * Exception à utiliser pour les erreurs de dans la partie recherche.<BR/>
 */
public class SAESearchServiceEx extends Exception {

   /**
    * L'identifiant unique de l'exception
    */
   private static final long serialVersionUID = -298210295473447438L;

   /**
    * Construit une nouvelle {@link SAESearchServiceEx }.
    */
   public SAESearchServiceEx() {
      super();
   }

   /**
    * Construit une nouvelle {@link SAESearchServiceEx } avec un message et une
    * cause données.
    * 
    * @param message
    *           : Message de l'erreur
    * @param cause
    *           : Cause de l'erreur
    */
   public SAESearchServiceEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link SAESearchServiceEx } avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public SAESearchServiceEx(final String message) {
      super(message);
   }
}
