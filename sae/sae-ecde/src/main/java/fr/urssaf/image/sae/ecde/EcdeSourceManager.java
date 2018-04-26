/**
 * 
 */
package fr.urssaf.image.sae.ecde;

import java.io.File;

import fr.urssaf.image.sae.ecde.exception.EcdeBadFileException;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;

/**
 * Manager pour le service Ecde.<br>
 * Il permettra en autre de recharger le fichier XML contenant les relations<br> 
 * des ECDESOURCES.<br>
 * A savoir, DNS et point de montage NTFS.
 * 
 */
public interface EcdeSourceManager {
   /**
    * Rechargement des fichiers XML contenant les relations ECDESources.
    * 
    * @param sourcesPath chemin du fichier contenant les sources
    * @return liste d'objet ECDESources convertis a partir du fichier XML
    * @throws EcdeBadFileException erreur en cas de mauvaise lecture de fichier.
    */
    EcdeSources load(File sourcesPath) throws EcdeBadFileException;
}
