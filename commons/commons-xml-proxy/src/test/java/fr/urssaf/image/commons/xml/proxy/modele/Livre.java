package fr.urssaf.image.commons.xml.proxy.modele;


/**
 * Un livre de la bibliothèque
 */
public interface Livre {

   
   /**
    * Le type
    * @return le type
    */
	String getType();
	
	
	/**
	 * Le style
	 * @return le style
	 */
	String getStyle();
	
	
	/**
	 * Le titre
	 * @return le titre
	 */
	Titre getTitre();
	
	
	/**
	 * L'auteur
	 * @return l'auteur
	 */
	Auteur getAuteur();
	
}
