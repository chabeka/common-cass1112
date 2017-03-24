package fr.urssaf.image.commons.maquette.definition;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.urssaf.image.commons.maquette.exception.MenuException;
import fr.urssaf.image.commons.maquette.tool.MenuItem;


/**
 * L'interface que doit implémenter l'application métier pour fournir à la 
 * maquette le contenu du menu principal
 *
 */
public interface IMenu {
	
   
   /**
    * Renvoie le menu du haut spécifique ou non pour la requête HTTP en cours
    * 
    * @param request la requête HTTP en cours
    * @return le menu
    * 
    * @throws MenuException si une erreur se produit lors de la génération du menu 
    */
   List<MenuItem> getMenu(HttpServletRequest request) throws MenuException ;
	
   
   /**
    * Renvoie le fil d'ariane spécifique à la requête HTTP en cours
    * 
    * @param request la requête HTTP en cours
    * @return le fil d'ariane
    */
	String getBreadcrumb(HttpServletRequest request) ;
	
}
