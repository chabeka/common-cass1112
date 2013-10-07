package fr.urssaf.image.sae.metadata.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import com.google.common.io.Files;
import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.sae.metadata.constants.Constants;

/**
 * Factorise du code relatif � l'utilisation de XStream.
 * 
 * @author akenore
 */
public final class XStreamHelper {

	/**
	 * Construit une instance de XStream capable de sérialiser / désérialiser un
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
	 * Parse un fichier XML et renvoie l'objet qu'il représente.
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
	 *            l'instance utilisée pour le parsing
	 * @return l'objet
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'ai pas trouvé
	 * 
	 */
	public static <T> T parse(final File pXmlFile, final Charset pXmlCharset,
			final Class<T> pResultClass, final XStream pXStream)
			throws FileNotFoundException {
		final Reader reader = Files.newReader(pXmlFile, pXmlCharset);
		return pResultClass.cast(pXStream.fromXML(reader));

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
	 * @return les données à partir d'un fichier xml
	 */

	public static <T> T loadDataProcess(final InputStreamReader dataFile,
			final Class<T> clazz) {
		return parse(dataFile,Constants.ENCODING, clazz,
				newXStream(clazz));
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
	/** Cette classe n'est pas con�ue pour �tre instanci�e. */
	private XStreamHelper() {
		assert false;
	}
}
