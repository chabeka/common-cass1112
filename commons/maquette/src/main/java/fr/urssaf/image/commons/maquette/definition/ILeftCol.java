package fr.urssaf.image.commons.maquette.definition;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.urssaf.image.commons.maquette.tool.InfoBoxItem;

public interface ILeftCol 
{
	/**
	 * @desc permet de r�cup�rer le nom de l'application
	 * @return
	 */
	public String getNomApplication( HttpServletRequest hsr ) ;
	
	/**
	 * @desc permet de r�cup�rer la version de l'application
	 * @return
	 */
	public String getVersionApplication( HttpServletRequest hsr ) ;
	
	/**
	 * @desc permet de r�cup�rer le nom de l'utilisateur
	 * @return
	 */
	public String getNomUtilisateur( HttpServletRequest hsr ) ;
	
	/**
	 * @desc permet de r�cup�rer le role de l'application
	 * @return
	 */
	public String getRoleUtilisateur( HttpServletRequest hsr ) ;
	
	/**
	 * @desc permet de r�cup�rer la fonction ou la m�thode javascript permettant de se d�connecter
	 * 		 le script javascript doit �videmment �tre inclus dans chaque page m�tier
	 * @return
	 */
	public String getLienDeconnexion( HttpServletRequest hsr ) ;
	
	/**
	 * @desc permet de r�cup�rer la liste des infobox � ajouter aux 3 standards
	 * @return
	 */
	public List<InfoBoxItem> getInfoBox( HttpServletRequest hsr ) ;
}
