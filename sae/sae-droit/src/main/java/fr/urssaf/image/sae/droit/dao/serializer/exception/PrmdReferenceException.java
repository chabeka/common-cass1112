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
    * constructeur
    * 
    * @param message
    *           message de l'exception
    * @param cause
    *           cause mère
    */
   public PrmdReferenceException(String message, Throwable cause) {
      super(message, cause);
   }

}
