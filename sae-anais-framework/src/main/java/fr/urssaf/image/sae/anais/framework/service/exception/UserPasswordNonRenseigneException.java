package fr.urssaf.image.sae.anais.framework.service.exception;

/**
 * Classe d'exception heritant de {@link IllegalArgumentException}<br>
 * Le mot de passe de l'utilisateur n’est pas renseigné<br>
 * <br>
 * Cette exception peut être levée par l'appel de la méthode
 * <code>authentifierPourSaeParLoginPassword<code>
 * 
 * @see SaeAnaisService
 * 
 */
public class UserPasswordNonRenseigneException extends IllegalArgumentException {

   
   private static final long serialVersionUID = 1L;

   /**
    * initialisation du message de l'exception<br>
    * <br>
    * Message: <code>Le mot de passe de l'utilisateur doit être renseigné</code>
    */
   public UserPasswordNonRenseigneException() {
      super("Le mot de passe de l'utilisateur doit être renseigné");

   }

}
