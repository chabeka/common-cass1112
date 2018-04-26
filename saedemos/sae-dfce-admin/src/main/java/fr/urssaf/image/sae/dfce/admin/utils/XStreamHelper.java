package fr.urssaf.image.sae.dfce.admin.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.nio.charset.Charset;

import com.google.common.io.Files;
import com.thoughtworks.xstream.XStream;

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

	/** Cette classe n'est pas con�ue pour �tre instanci�e. */
	private XStreamHelper() {
		assert false;
	}
}
