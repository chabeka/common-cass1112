package fr.urssaf.image.sae.metadata.referential.services;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Fournit un service de désérialisation de la base
 * 
 * @author akenore
 * 
 */
public interface XmlDataService {
	/**
	 * 
	 * @param xmlInputStream
	 *            : Le flux xml contenant les données
	 * @return Le Referentiel des métadonnées
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'est pas présent
	 */
	Map<String, MetadataReference> referentialReader(final InputStream xmlInputStream)
			throws FileNotFoundException;
	
	


}
