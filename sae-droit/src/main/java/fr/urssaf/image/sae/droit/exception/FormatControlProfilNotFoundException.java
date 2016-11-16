/**
 * 
 */
package fr.urssaf.image.sae.droit.exception;

/**
 * Le profil de controle n'existe pas dans la colonne family
 * DroitFormatControlProfil.
 * 
 */
public class FormatControlProfilNotFoundException extends Exception {

   private static final long serialVersionUID = -2787691464718590500L;

   /**
    * constructeur
    * 
    * @param message
    *           message de l'exception
    */
   public FormatControlProfilNotFoundException(String message) {
      super(message);
   }

   /**
    * constructeur
    * 
    * @param message
    *           message de l'exception
    * @param cause
    *           cause de l'exception
    */
   public FormatControlProfilNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * constructeur
    * 
    * @param cause
    *           cause de l'exception
    */
   public FormatControlProfilNotFoundException(Throwable cause) {
      super(cause);
   }
}
