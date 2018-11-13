package fr.urssaf.image.sae.storage.dfce.data.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.nio.charset.Charset;

import com.google.common.io.Files;
import com.thoughtworks.xstream.XStream;

/**
 * Factorise du code relatif à l'utilisation de XStream.
 */
public final class XStreamHelper {

	/**
	 * Construit une instance de XStream capable de sérialiser / désérialiser un
	 * ensemble de classes annotées.
	 * 
	 * @param modelClasses
	 *            les classes
	 * @return l'instance
	 */
	public static XStream newXStream(final Class<?>... modelClasses) {
		final XStream xstream = new XStream();
		xstream.processAnnotations(modelClasses);
		return xstream;
	}

	/**
	 * Parse un fichier XML et renvoie l'objet qu'il représente.
	 * 
	 * @param <T>
	 *            le type d'objet
	 * @param xmlFile
	 *            le fichier
	 * @param xmlCharset
	 *            l'encodage du fichier
	 * @param resultClass
	 *            la classe du type d'objet
	 * @param xStream
	 *            l'instance utilisée pour le parsing
	 * @return l'objet
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'ai pas trouvé
	 * 
	 */
	public static <T> T parse(final File xmlFile, final Charset xmlCharset,
			final Class<T> resultClass, final XStream xStream)
			throws FileNotFoundException {
		final Reader reader = Files.newReader(xmlFile, xmlCharset);
		return resultClass.cast(xStream.fromXML(reader));

	}

	/** Cette classe n'est pas connue pour [être instanciée. */
	private XStreamHelper() {
		assert false;
	}
}
