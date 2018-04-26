package fr.urssaf.image.sae.storage.bouchon.services.util;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import fr.urssaf.image.sae.storage.bouchon.data.model.DocPdf;


/**
 * Cette classe contient des méthodes utilitaires
 * 
 * @author akenore
 * 
 */
public final class Utils {
	  /** Le composant pour les traces */
	private static final Logger LOGGER = Logger.getLogger(Utils.class);
	private static final Charset ENCODING = Charset.forName("UTF-8");
	/**
	 * Simplifie l'écriture des boucles foreach quand l'argument peut être
	 * {@code null}.
	 * 
	 * @param <T>
	 *            le type des éléments
	 * @param anIterable
	 *            les éléments à parcourir
	 * @return les éléments, ou une collection vide si l'argument était null
	 */
	@SuppressWarnings("PMD.OnlyOneReturn")
	public static <T> Iterable<T> nullSafeIterable(final Iterable<T> anIterable) {
		if (anIterable == null) {
			return Collections.emptyList();
		} else {
			return anIterable;
		}
	}

	/**
	 * Simplifie l'écriture des map
	 * 
	 * @param map
	 *            le type des éléments
	 * @param <K>
	 *            : type
	 * @param <V>
	 *            : type
	 * @return les éléments, ou une map vide si l'argument était null
	 */
	@SuppressWarnings("PMD.OnlyOneReturn")
	public static <K, V> Map<K, V> nullSafeMap(final Map<K, V> map) {
		if (map == null) {
			return Collections.emptyMap();
		} else {
			return map;
		}
	}

	/**
	 * Retourne le contenu d'un fichier
	 * 
	 * 
	 * 
	 * @return le contenu d'un fichier
	 */
	
	public static byte[] fileContent() {
		return Base64.decodeBase64(DocPdf.getDoc().getBytes(
				ENCODING));
	}

	/**
	 * 
	 * @param map
	 *            : une map
	 * @return Une chaîne de caractère
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public static String mapToString(final Map<?, ?> map) {
		final StringBuilder stringBuilder = new StringBuilder("[");
		String sep = "";
		for (Object object : map.keySet()) {
			stringBuilder.append(sep).append(object.toString()).append("=")
					.append(map.get(object).toString());
			sep = "|";
		}
		return stringBuilder.append("]").toString();

	}

	/**
	 * Retourne un strean en lieu et place d'une chaîne de caractère
	 * 
	 * @param xlmlFlux
	 *            : la chaîne
	 * @return un strean en lieu et place d'une chaîne de caractère
	 */
	public static InputStreamReader getFileFromClassPath(final String xlmlFlux) {
		final byte[] bytes = xlmlFlux.getBytes(ENCODING);
		final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		return new InputStreamReader(bais);
	}

	/** Cette classe n'est pas faite pour être instanciée. */
	private Utils() {
		assert false;
	}

	/**
	 * 
	 * @param data
	 *            : la donnée
	 * @return La valeur Hexa correspondant au tableau
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private static String convertToHex(final byte[] data) {
		final StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
				{	buf.append((char) ('0' + halfbyte));}
				else
				{	buf.append((char) ('a' + (halfbyte - 10)));}
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}
/**
 * 
 * @param fileContent : le contenu du fichier
 * @param encoding :  l'encoding
 * @return le Sha
 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public static String computeSha(final String fileContent,final String encoding) {
		MessageDigest messageDigest = null;
		byte[] shahash = new byte[40];
		try {
			messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(fileContent.getBytes(encoding), 0, fileContent.length());
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Pb d'encoding" + e.toString());	
			
		}catch (NoSuchAlgorithmException e1) {
			LOGGER.error("Pb d'algorithme sha1" + e1.toString());
			
		}
		shahash = messageDigest.digest();
		
		return convertToHex(shahash);

	}
	
	
}
