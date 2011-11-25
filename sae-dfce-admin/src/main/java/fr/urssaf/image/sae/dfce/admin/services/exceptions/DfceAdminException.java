package fr.urssaf.image.sae.dfce.admin.services.exceptions;

/**
 * Classe de base des exceptions de Dfce supportant les exceptions
 * imbriquées.
 * 
 * 
 */
public class DfceAdminException extends Exception {

   /**
    * Identifiant unique qui caractérise l'excepion
    */
   private static final long serialVersionUID = -6505532287814387009L;

   /**
    * Construit un {@link DfceAdminException }
    */
   public DfceAdminException() {
      super();
   }

   /**
    * Construit un {@link DfceAdminException }
    * 
    * @param message
    *           Le message de l'erreur
    */
   public DfceAdminException(final String message) {
      super(message);
   }

   /**
    * Construit un {@link DfceAdminException }
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public DfceAdminException(final String message, final Throwable cause) {
      super(message, cause);
   }
}
