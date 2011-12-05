package fr.urssaf.image.sae.anais.framework.service.exception;

/**
 * Classe d'exception hérite de {@link IllegalArgumentException}<br>
 * L'adresse IP ou le nom d’hôte du serveur ANAIS n'est pas renseigné dans les
 * paramètres de connexion <br><br>
 * Cette exception peut être levée par l'appel de la méthode
 * <code>authentifierPourSaeParLoginPassword<code>
 * 
 * @see SaeAnaisService
 */
public class HoteNonRenseigneException extends IllegalArgumentException {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * initialisation du message de l'exception<br><br>
    * Message:<br>
    * <code>L'adresse IP ou le nom d'hôte du serveur ANAIS doit être renseigné dans les paramètres de connexion</code>
    */
   public HoteNonRenseigneException() {
      super(
            "L'adresse IP ou le nom d'hôte du serveur ANAIS doit être renseigné dans les paramètres de connexion");
   }

}
