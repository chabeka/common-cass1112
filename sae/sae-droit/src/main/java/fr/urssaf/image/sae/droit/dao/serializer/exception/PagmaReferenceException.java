/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.serializer.exception;

/**
 * Exception levée quand la référence PAGMa n'existe pas dans la famille de
 * colonnes DroitPagma
 * 
 */
public class PagmaReferenceException extends RuntimeException {

   private static final long serialVersionUID = 2296714377786678967L;

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'exception
    * @param cause
    *           cause mère
    */
   public PagmaReferenceException(String message, Throwable cause) {
      super(message, cause);
   }
   
   


}
