package fr.urssaf.image.sae.storage.bouchon.services.util;

import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.lang.Validate;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author akenore
 * 
 */
public final class XstreamHelper {
	// /** Le composant pour les traces */
	// private static final Logger LOGGER =
	// Logger.getLogger(XstreamHelper.class);

	/**
	 * Construit une instance de XStream capable desérialiser/désérialiser un
	 * ensemble de classes annotées.
	 * 
	 * @param pModelClasses
	 *            les classes
	 * @return l'instance
	 */
	public static XStream newXStream(final Class<?>... pModelClasses) {
		final XStream xstream = new XStream();
		xstream.processAnnotations(pModelClasses);
		return xstream;
	}

	/**
	 * Parse un fichier XML et renvoie l'objet qu'il repr�sente.
	 * 
	 * @param <T>
	 *            le type d'objet
	 * @param pXmlFile
	 *            le fichier
	 * @param pXmlCharset
	 *            l'encodage du fichier
	 * @param pResultClass
	 *            la classe du type d'objet
	 * @param pXStream
	 *            l'instance utilis�e pour le parsing
	 * @return l'objet
	 * 
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public static   <T> T parse(final InputStreamReader pXmlFile,
			final Charset pXmlCharset, final Class<T> pResultClass,
			final XStream pXStream) {
		return pResultClass.cast(pXStream.fromXML(pXmlFile));
	}

	/**
	 * Cette méthode permet de charger les données à partir d'un fichier xml
	 * 
	 * @param <T>
	 *            : Le type
	 * @param dataFile
	 *            : Le file de données
	 * @param clazz
	 *            : La classe
	 * @param message
	 *            : Le message
	 * @return les données à partir d'un fichier xml
	 */

	public static <T> T loadDataProcess(final InputStreamReader dataFile,
			final Class<T> clazz, final String message) {
		Validate.notNull(dataFile, message);

		return XstreamHelper.parse(dataFile, Charset.forName("UTF-8"), clazz,
				XstreamHelper.newXStream(clazz));
	}

	/** Cette classe n'est pas con�ue pour être instanciée. */
	private XstreamHelper() {
		assert false;
	}
}
