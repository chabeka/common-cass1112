/**
 * 
 */
package fr.urssaf.image.sae.trace.exception;

/**
 * Erreur levée lorsqu'un paramètre n'est pas trouvé
 * 
 */
public class ParameterNotFoundException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    */
   public ParameterNotFoundException(String message) {
      super(message);
   }

}
