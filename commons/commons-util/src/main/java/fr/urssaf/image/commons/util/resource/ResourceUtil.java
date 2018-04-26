package fr.urssaf.image.commons.util.resource;

import java.net.URISyntaxException;

/**
 * Fonctions utilitaires pour la manipulation des ressources contenues
 * dans un projet Java.
 *
 */
public final class ResourceUtil {
	
	private ResourceUtil(){
		
	}

	/**
	 * Renvoie le chemin complet d'un fichier de ressource<br>
	 * <br>
	 * Cette méthode peut être utile, par exemple, pour les tests unitaires, lorsqu'une
	 * fonctionnalité requiert qu'on lui passe un chemin complet de fichier, et que pour
	 * les tests unitaires, on a stocké des fichiers de test dans src/tests/resources. 
	 * 
	 * @param arg
	 *            l'objet en cours
	 * @param resourcePath
	 *            le chemin de la ressource au sein d'un répertoire de ressource
	 *            (exemple, si on a un fichier dans src\main\resources\toto.xsd,
	 *            il faut passer la chaîne "/toto.xsd"
	 * @return le chemin complet du fichier ressource
	 * 
	 * @throws URISyntaxException exception levée par {@link java.net.URL#toURI}
	 */
	public static String getResourceFullPath(Object arg, String resourcePath)
			throws URISyntaxException {
		return arg.getClass().getResource(resourcePath).toURI().getPath();
	}

}
