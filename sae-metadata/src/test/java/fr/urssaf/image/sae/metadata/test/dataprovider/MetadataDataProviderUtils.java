package fr.urssaf.image.sae.metadata.test.dataprovider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.constants.Constants;
import fr.urssaf.image.sae.metadata.test.model.MockMetadata;
import fr.urssaf.image.sae.metadata.test.model.MockMetadatas;
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.metadata.utils.XStreamHelper;

/**
 * 
 * @author classe fournissant les services de lecture des fichiers xml de test.
 * 
 * 
 */
public final class MetadataDataProviderUtils {

	/**
	 * Retourne une liste de métadonnée de type {@link SAEMetadata} à partir
	 * d'une structure XML
	 * 
	 * @param xmlFile
	 *            : Le nom du fichier xml
	 * @return ne liste de métadonnée à partir d'une structure XML
	 * @throws FileNotFoundException
	 *             Exception levée lorsque le fichier n'existe pas.
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public  static List<SAEMetadata> getSAEMetadata(final File xmlFile)
			throws FileNotFoundException {
		final List<SAEMetadata> saeMetadata = new ArrayList<SAEMetadata>();
		final MockMetadatas dataFromXml = getMetadataFormTestFile(xmlFile);
		for (MockMetadata metadata : Utils.nullSafeIterable(dataFromXml
				.getMetadatas())) {
			saeMetadata.add(new SAEMetadata(metadata.getLongCode(), metadata
					.getShortCode(), metadata.getValue()));
		}
		return saeMetadata;
	}

	/**
	 * Retourne une liste de métadonnée de type {@link UntypedMetadata} à partir
	 * d'une structure XML
	 * 
	 * @param xmlFile
	 *            : Le nom du fichier xml
	 * @return ne liste de métadonnée à partir d'une structure XML
	 * @throws FileNotFoundException
	 *             Exception levée lorsque le fichier n'existe pas.
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public  static List<UntypedMetadata> getUntypedMetadata(
			final File xmlFile) throws FileNotFoundException {
		final List<UntypedMetadata> untyped = new ArrayList<UntypedMetadata>();
		final MockMetadatas dataFromXml = getMetadataFormTestFile(xmlFile);
		for (MockMetadata metadata : Utils.nullSafeIterable(dataFromXml
				.getMetadatas())) {
			untyped.add(new UntypedMetadata(metadata.getLongCode(), metadata
					.getValue()));
		}
		return untyped;
	}

	/**
	 * 
	 * 
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 */
	private static MockMetadatas getMetadataFormTestFile(final File xmlFile)
			throws FileNotFoundException {
		// String filename =
		// getClass().getResource("/referentiel.xml").getPath();
		return XStreamHelper.parse(xmlFile, Constants.ENCODING,
				MockMetadatas.class, buildReadingXStream(MockMetadatas.class));
	}

	/**
	 * Construit le composant XStream utilisé en lecture.
	 * 
	 * @param xstrClass
	 *            : La classe à désérialiser
	 * @return le composant xstream
	 */
	private static XStream buildReadingXStream(final Class<?> xstrClass) {

		return XStreamHelper.newXStream(xstrClass);
	}

	/**
	 * Cette classe n'est pas faite pour être instanciée.
	 */
	private MetadataDataProviderUtils() {
		assert false;

	}

}
