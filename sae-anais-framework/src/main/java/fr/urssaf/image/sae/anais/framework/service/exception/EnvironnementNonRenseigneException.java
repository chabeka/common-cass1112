package fr.urssaf.image.sae.anais.framework.service.exception;

/**
 * Classe d'exception hérite de {@link IllegalArgumentException}<br>
 * L'environnement (Production / Validation / Développement) n'est pas renseigné<br>
 * <br>
 * Cette exception peut être levée par l'appel de la méthode
 * <code>authentifierPourSaeParLoginPassword<code>
 * 
 * @see SaeAnaisService
 */
public class EnvironnementNonRenseigneException extends
      IllegalArgumentException {

   private static final long serialVersionUID = 1L;

   /**
    * initialisation du message de l'exception<br>
    * <br>
    * Message:<br>
    * <code>L'environnement (Développement / Validation  / Production) doit être renseigné</code>
    */
   public EnvironnementNonRenseigneException() {
      super(
            "L'environnement (Développement / Validation  / Production) doit être renseigné");
   }

}
