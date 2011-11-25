package fr.urssaf.image.sae.anais.framework.service.exception;

/**
 * Classe d'exception heritant de {@link IllegalArgumentException}<br>
 * L'identifiant de l’utilisateur n'est pas renseigné<br>
 * <br>
 * Cette exception peut être levée par l'appel de la méthode
 * <code>authentifierPourSaeParLoginPassword<code>
 * 
 * @see SaeAnaisService
 */
public class UserLoginNonRenseigneException extends IllegalArgumentException {

   private static final long serialVersionUID = 1L;

   /**
    * initialisation du message de l'exception<br>
    * <br>
    * Message: <code>L'identifiant de l'utilisateur doit être renseigné</code>
    */
   public UserLoginNonRenseigneException() {
      super("L'identifiant de l'utilisateur doit être renseigné");
   }

}
