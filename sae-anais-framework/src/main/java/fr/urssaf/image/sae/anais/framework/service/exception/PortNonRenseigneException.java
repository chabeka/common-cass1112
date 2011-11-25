package fr.urssaf.image.sae.anais.framework.service.exception;

/**
 * Classe d'exception hérite de {@link IllegalArgumentException}<br>
 * Le port du serveur ANAIS n'est pas renseigné dans les paramètres de connexion
 * transmis<br>
 * <br>
 * Cette exception peut être levée par l'appel de la méthode
 * <code>authentifierPourSaeParLoginPassword<code>
 * 
 * @see SaeAnaisService
 */
public class PortNonRenseigneException extends IllegalArgumentException {

   private static final long serialVersionUID = 1L;

   /**
    * initialisation du message de l'exception<br>
    * <br>
    * Message:<br>
    * <code>Le port du serveur ANAIS doit être renseigné dans les paramètres de connexion</code>
    */
   public PortNonRenseigneException() {
      super(
            "Le port du serveur ANAIS doit être renseigné dans les paramètres de connexion");
   }

}
