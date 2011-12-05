package fr.urssaf.image.sae.webservices.service.support;

/**
 * Support pour le lancement d'un traitement.
 * 
 * 
 */
public interface LauncherSupport {

   /**
    * L'appel de cette méthode lance le traitement
    * 
    * @param parameters
    *           paramètres pour le lancement du traitement
    */
   void launch(Object... parameters);

   /**
    * Vérifie si le traitement est en cours d'exécution
    * 
    * @return <code>true</code> si le traitement est en cours d'exécution
    *         <code>false</code> sinon
    */
   boolean isLaunched();
}
