/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.serializer.exception;

/**
 * Exception levée lorsque la référence PRMD n'existe pas dans la famille de
 * colonnes DroitPrmd
 * 
 */
public class PrmdReferenceException extends RuntimeException {

   private static final long serialVersionUID = -5354435074010883178L;

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'exception
    */
   public PrmdReferenceException(String message) {
      super(message);
   }

}
