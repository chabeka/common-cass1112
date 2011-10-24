package fr.urssaf.image.sae.anais.framework.service.exception;

/**
 * Classe d'exception hérite de {@link Exception}<br>
 * L'utilisateur n'a aucun droit sur l'application<br>
 * <br>
 * Cette exception peut être levée par l'appel de la méthode
 * <code>authentifierPourSaeParLoginPassword<code>
 * 
 * @see SaeAnaisService
 */
public class AucunDroitException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * initialisation du message de l'exception<br>
    * <br>
    * Message:<br>
    * <code>L'utilisateur n'a aucun droit sur l'application</code>
    */
   public AucunDroitException(){
      super("L'utilisateur n'a aucun droit sur l'application");
   }
}
