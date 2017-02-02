/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.service.ecde.file;

import java.util.List;

import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeSource;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeSources;

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
    * @return liste d'objet ECDESources convertis a partir du fichier XML
    * @throws Exception
    *            erreur en cas de mauvaise lecture de fichier.
    */
   EcdeSources load() throws Exception;

   /**
    * Génération du fichier à partir de la liste fournie en paramètre
    * 
    * @param sources
    * @throws Exception
    *            en cas d'erreur
    */
   void generate(List<EcdeSource> sources) throws Exception;
}
