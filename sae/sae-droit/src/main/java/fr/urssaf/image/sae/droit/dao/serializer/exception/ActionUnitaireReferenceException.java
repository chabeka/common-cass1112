/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.serializer.exception;

/**
 * Exception levée lorsque la référence ActionUnitaire n'existe pas dans la
 * famille de colonnes DroitActionUnitaire
 * 
 */
public class ActionUnitaireReferenceException extends RuntimeException {

   private static final long serialVersionUID = 3833620672412920774L;

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'exception
    */
   public ActionUnitaireReferenceException(String message) {
      super(message);
   }

}
