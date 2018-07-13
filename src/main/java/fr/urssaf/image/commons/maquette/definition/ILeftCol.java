package fr.urssaf.image.commons.maquette.definition;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.urssaf.image.commons.maquette.tool.InfoBoxItem;


/**
 * L'interface que doit implémenter l'application métier pour fournir à la
 * maquette les informations de la colonne de gauche.
 *
 */
public interface ILeftCol
{
	
   
   /**
	 * Renvoie le nom de l'application
	 * 
	 * @param request la requête HTTP en cours
	 * @return le nom de l'application
	 */
	String getNomApplication(HttpServletRequest request) ;
	
	
	/**
	 * Renvoie la version de l'application
	 * 
	 * @param request la requête HTTP en cours
	 * @return la version de l'application
	 */
	String getVersionApplication(HttpServletRequest request) ;
	
	
	/**
	 * Renvoie le nom de l'utilisateur
	 * 
	 * @param request la requête HTTP en cours
	 * @return le nom de l'utilisateur
	 */
	String getNomUtilisateur(HttpServletRequest request) ;
	
	
	/**
	 * Renvoie le profil de droit de l'utilisateur
	 * 
	 * @param request la requête HTTP en cours
	 * @return le profil de droit de l'utilisateur
	 */
	String getRoleUtilisateur(HttpServletRequest request) ;

	
	/**
	 * Renvoie la fonction ou la méthode javascript permettant de se déconnecter.<br>
	 * <br>
	 * Le script javascript doit évidemment être inclus dans chaque page métier.
	 * 
	 * @param request la requête HTTP en cours
	 * @return le javascript de déconnexion
	 */
	String getLienDeconnexion(HttpServletRequest request) ;
	
	
	/**
	 * Renvoie la liste des infobox à rajouter aux 3 boîtes standards
	 * (information application, information utilisateur, déconnexion)
	 * 
	 * @param request la requête HTTP en cours
	 * @return la liste des infobox à rajouter aux 3 boîtes standards
	 */
	List<InfoBoxItem> getInfoBox(HttpServletRequest request) ;
	
}
