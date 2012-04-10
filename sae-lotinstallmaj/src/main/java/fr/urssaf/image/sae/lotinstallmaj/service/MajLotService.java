package fr.urssaf.image.sae.lotinstallmaj.service;


/**
 * Pour chaque lot, il y aura une classe principale<br>
 * à lancer pour déclencher des opérations de maj, classe qui devra<br>
 * implémenter cette interface. 
 *
 */
public interface MajLotService {

   /**
    * Réalise les opérations de mise à jour.
    * 
    * @param nomOperation le nom de l'opération à réaliser
    * @param argSpecifiques les arguments de la ligne de commande spécifiques pour l'opération à réaliser
    */
   void demarre(String nomOperation, String[] argSpecifiques);
}
