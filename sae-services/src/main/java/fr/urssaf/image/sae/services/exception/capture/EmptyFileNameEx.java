package fr.urssaf.image.sae.services.exception.capture;


/**
 * Classe d'exception pour signaler que<br>
 * le nom de fichier est vide ou rempli d'espaces.
 * 
 *
 */
public class EmptyFileNameEx  extends Exception {
   
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link EmptyFileNameEx} avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public EmptyFileNameEx(final String message, final Throwable cause) {
      super(message, cause);
   }
   
   /**
    * Construit une nouvelle {@link EmptyFileNameEx} avec un message.
    * 
    * @param message
    *           : Le message d'erreur
    */
   public EmptyFileNameEx(final String message) {
      super(message);
   }
   
}
