package fr.urssaf.image.sae.storage.dfce.services.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import fr.urssaf.image.sae.storage.dfce.data.model.DesiredMetaData;
import fr.urssaf.image.sae.storage.dfce.data.model.LuceneQueries;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;

/**
 * Fournit un service de désérialisation de la base
 * 
 * @author akenore
 * 
 */
public interface XmlDataService {
	/**
	 * 
	 * @param xmlFile
	 *            : Le fichier xml contenant les méta données
	 * @return Le model des méta données
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'est pas présent
	 */
	DesiredMetaData desiredMetaDataReader(final File xmlFile)
			throws FileNotFoundException;

	/**
	 * Fournit un document sae désérialisé
	 * 
	 * @param xmlFile
	 *            : le chemin du fichier Xml du document
	 * @return un document sae désérialisé
	 * @throws FileNotFoundException
	 *             lorsque le fichier n'existe pas
	 */
	SaeDocument saeDocumentReader(final File xmlFile)
			throws FileNotFoundException;

	/**
	 * Fournit une liste de documents sae désérialisée
	 * 
	 * @param xmlFiles
	 *            : le chemin du fichier Xml du document
	 * @return une liste de documents sae désérialisé
	 * @throws FileNotFoundException
	 *             lorsque le fichier n'existe pas
	 */
	List<SaeDocument> saeDocumentsReader(final File... xmlFiles)
			throws FileNotFoundException;

	/**
	 * 
	 * @param xmlFile
	 *            : : le chemin du fichier Xml du document.
	 * @return La liste des requêtes.
	 * @throws FileNotFoundException
	 *             lorsque le fichier n'existe pas
	 */
	LuceneQueries queriesReader(final File xmlFile)
			throws FileNotFoundException;
}
