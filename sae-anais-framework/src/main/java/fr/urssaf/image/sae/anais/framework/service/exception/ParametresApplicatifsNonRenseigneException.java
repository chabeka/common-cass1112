package fr.urssaf.image.sae.anais.framework.service.exception;

/**
 * Classe d'exception hérite de {@link IllegalArgumentException}<br>
 * Les paramètres du compte applicatif ne sont pas renseignés alors qu'aucun
 * profil de compte applicatif n'a été spécifié<br>
 * <br>
 * Cette exception peut être levée par l'appel de la méthode
 * <code>authentifierPourSaeParLoginPassword<code>
 * 
 * @see SaeAnaisService
 */
public class ParametresApplicatifsNonRenseigneException extends
      IllegalArgumentException {

   private static final long serialVersionUID = 1L;

   /**
    * initialisation du message de l'exception<br>
    * <br>
    * Message:<br>
    * <code>Les paramètres du compte applicatif ne sont pas renseignés alors qu'aucun profil de compte applicatif n'a été spécifié</code>
    */
   public ParametresApplicatifsNonRenseigneException() {
      super(
            "Les paramètres du compte applicatif ne sont pas renseignés alors qu'aucun profil de compte applicatif n'a été spécifié");
   }

}
