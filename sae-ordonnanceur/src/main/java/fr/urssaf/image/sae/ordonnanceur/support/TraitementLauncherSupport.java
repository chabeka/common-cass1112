package fr.urssaf.image.sae.ordonnanceur.support;

import fr.urssaf.image.sae.pile.travaux.model.JobQueue;

/**
 * Support pour lancer les processus des traitements de masse
 * 
 * 
 */
public interface TraitementLauncherSupport {

   /**
    * Méthode permettant de lancer un processus pour exécuter un traitement de
    * masse
    * 
    * @param traitement
    *           traitement à lancer
    */
   void lancerTraitement(JobQueue traitement);

}
