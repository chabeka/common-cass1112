package fr.urssaf.image.sae.storage.exception;

/**
 * Classe de base des exceptions de stockage supportant les exceptions
 * imbriquées
 * 
 * <li>
 * Attribut serialVersionUID Caractérise l'erreur</li>
 */
public class StorageRuntimeException extends RuntimeException {

   /**
    * Identifiant unique qui caractérise l'excepion
    */
   private static final long serialVersionUID = -6505532287814387009L;

   /**
    * Constructeur basique
    */
   public StorageRuntimeException() {
      super();
   }

   /**
    * Constructeur
    * 
    * @param message
    *           Le message de l'erreur
    */
   public StorageRuntimeException(final String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param message :
    *           Le message d'erreur
    * @param cause :
    *           La cause de l'erreur
    */
   public StorageRuntimeException(final String message, final Throwable cause) {
      super(message, cause);
   }
}
