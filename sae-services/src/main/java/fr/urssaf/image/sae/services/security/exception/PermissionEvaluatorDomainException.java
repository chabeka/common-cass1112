/**
 * 
 */
package fr.urssaf.image.sae.services.security.exception;

/**
 * Exception levée lors de l'évaluation de la permission, quand le domaine est
 * inattendu.
 * 
 */
public class PermissionEvaluatorDomainException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * constructeur
    * @param message message de l'exception
    */
   public PermissionEvaluatorDomainException(String message) {
      super(message);
   }
}
