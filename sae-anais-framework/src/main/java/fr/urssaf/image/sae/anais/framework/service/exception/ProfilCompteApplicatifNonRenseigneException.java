package fr.urssaf.image.sae.anais.framework.service.exception;

/**
 * Classe d'exception hérite de {@link IllegalArgumentException}<br>
 * Le profil du compte applicatif n'est pas renseigné<br>
 * <br>
 * Cette exception peut être levée par l'appel de la méthode
 * <code>authentifierPourSaeParLoginPassword<code>
 * 
 * @see SaeAnaisService
 */
public class ProfilCompteApplicatifNonRenseigneException extends
      IllegalArgumentException {

   private static final long serialVersionUID = 1L;

   /**
    * initialisation du message de l'exception<br>
    * <br>
    * Message: <code>Le profil du compte applicatif n'est pas renseigné</code>
    */
   public ProfilCompteApplicatifNonRenseigneException() {
      super("Le profil du compte applicatif n'est pas renseigné");
   }

}
