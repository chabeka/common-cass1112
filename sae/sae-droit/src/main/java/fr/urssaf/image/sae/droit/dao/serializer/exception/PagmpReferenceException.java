/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.serializer.exception;

/**
 * Exception levée lorsque la référence PAGMp n'existe pas dans la famille de
 * colonnes DroitPagmp
 * 
 */
public class PagmpReferenceException extends RuntimeException {

   private static final long serialVersionUID = 4855685465214197899L;

   /**
    * constructeur
    * 
    * @param message
    *           message de l'exception
    * @param cause
    *           cause mère
    */
   public PagmpReferenceException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * constructeur
    * 
    * @param message
    *           message de l'exception
    */
   public PagmpReferenceException(String message) {
      super(message);
   }

}
