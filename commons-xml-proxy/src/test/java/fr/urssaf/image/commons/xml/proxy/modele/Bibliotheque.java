package fr.urssaf.image.commons.xml.proxy.modele;

/**
 * Le noeud racine du XML : la bibliothèque
 */
public interface Bibliotheque {

	
   /**
    * Renvoie la liste des livres
    * @return la liste des livres
    */
   Livre[] getLivre();
	
}
