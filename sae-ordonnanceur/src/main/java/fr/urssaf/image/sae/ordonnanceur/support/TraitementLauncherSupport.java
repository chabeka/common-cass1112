package fr.urssaf.image.sae.ordonnanceur.support;

import org.springframework.batch.core.JobInstance;

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
   void lancerTraitement(JobInstance traitement);

}
