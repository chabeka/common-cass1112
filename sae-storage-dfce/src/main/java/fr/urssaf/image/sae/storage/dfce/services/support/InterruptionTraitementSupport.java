package fr.urssaf.image.sae.storage.dfce.services.support;

/**
 * Service pour permettre d'interrompre un traitement
 * 
 * 
 */
public interface InterruptionTraitementSupport {

   /**
    * L'interruption du service DFCE est parfois nécessaire pour des questions
    * de maintenance, et programmée à des moments précis pour être redémarré
    * ensuite. Cette méthode permet de mettre en pause le
    * {@link java.lang.Thread} courant pendant cette période puis de se
    * reconnecter au service DFCE
    * 
    * @param startTime
    *           Heure de programmation du début de l'interruption. Doit être au
    *           format HH:mm:ss
    * @param delay
    *           Temps d'attente avant la première tentative de reconnexion en
    *           secondes
    * @param tentatives
    *           Nombre de tentatives de reconnexion
    */
   void interruption(String startTime, int delay, int tentatives);
}
