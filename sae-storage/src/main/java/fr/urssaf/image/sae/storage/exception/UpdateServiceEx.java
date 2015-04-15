/**
 * 
 */
package fr.urssaf.image.sae.storage.exception;


/**
 * Classe d'exception pour les mises à jour
 * 
 */
public class UpdateServiceEx extends Exception {

   /**
    * Constructeur
    * 
    * @param exception
    *           l'exception mère
    */
   public UpdateServiceEx(Exception exception) {
      super(exception);
   }

   private static final long serialVersionUID = -1305786977948095984L;

}
