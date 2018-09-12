/**
 * 
 */
package fr.urssaf.image.sae.commons.exception;

/**
 * Erreur levée lorsqu'un paramètre n'est pas trouvé
 * 
 */
public class ParameterNotFoundException extends Exception {

   private static final long serialVersionUID = 5431010446702527384L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ParameterNotFoundException(String message) {
      super(message);
   }

}
