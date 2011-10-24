package fr.urssaf.image.sae.storage.dfce.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.model.connection.StorageConnectionParameter;
import fr.urssaf.image.sae.storage.model.connection.StorageHost;
/**
 * Cette classe contient des méthodes utilitaires
 * 
 * @author akenore
 * 
 */
public final class Utils {
   /**
    * Simplifie l'écriture des boucles foreach quand l'argument peut être
    * {@code null}.
    * 
    * @param <T>
    *           le type des éléments
    * @param anIterable
    *           les éléments à parcourir
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
    *           le type des éléments
    * @param <K>
    *           : type
    * @param <V>
    *           : type
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

   /** Cette classe n'est pas faite pour être instanciée. */
   private Utils() {
      assert false;
   }

   /**
    * Convertie une chaîne en Date
    * 
    * @param date
    *           : La chaîne à convertir.
    * @return une date à partir d'une chaîne.
    * @throws ParseException
    *            Exception lorsque le parsing de la chaîne ne se déroule pas
    *            bien.
    */
   public static Date formatStringToDate(final String date)
         throws ParseException {
      Date newDate = new Date();
      if (date != null) {
         SimpleDateFormat formatter = new SimpleDateFormat(
               Constants.DATE_PATTERN, Constants.DEFAULT_LOCAL);
         formatter.setLenient(false);
         newDate = formatter.parse(date);
         if (formatter.parse(date, new ParsePosition(0)) == null) {
            formatter = new SimpleDateFormat(Constants.DATE_PATTERN,
                  Constants.DEFAULT_LOCAL);
            newDate = formatter.parse(date);
         }
      }
      return newDate;
   }

   /**
    * Convertit une date en chaîne
    * 
    * @param date
    *           : La date à convertir
    * @return chaîne à partir d'une date
    * @throws ParseException
    *            Exception lorsque le parsing de la chaîne ne se déroule pas
    *            bien.
    */
   public static String formatDateToString(final Date date)
         throws ParseException {
      String newDate = Constants.BLANK;
      if (date != null) {
         final SimpleDateFormat formatter = new SimpleDateFormat(
               Constants.DATE_PATTERN, Constants.DEFAULT_LOCAL);
         newDate = formatter.format(date);
      }
      return newDate;
   }
   
   /**
	 * Permet de trouver le bon type dans l'enumération des types.
	 * 
	 * @param shortCode
	 *            : Le code court cherché
	 * @return Le type métier correspondant au type cherché.
	 */
	@SuppressWarnings("PMD.OnlyOneReturn")
	public static StorageTechnicalMetadatas technicalMetadataFinder(
			final String shortCode) {
		for (StorageTechnicalMetadatas technical : StorageTechnicalMetadatas.values()) {
			if (technical.getShortCode().equals(shortCode)) {
				return technical;
			}
		}
		return StorageTechnicalMetadatas.NOVALUE;
	}
	
	/**
	 * Permet de trouver le bon type dans l'enumération des des métadonnées
	 * techniques à exclure.
	 * 
	 * @param longCode
	 *            : Le code long cherché
	 * @return True si la métadonnée est à exclure.
	 */
		public static boolean excludeTechnicalMetadataFinder(final String longCode) {
		return technicalMetadataFinder(longCode) != StorageTechnicalMetadatas.NOVALUE;
	}
		/**
		 * Permet de construire l'url de connection.
		 * 
		 * @param storageConnectionParameter
		 *            : Les paramètres de connexion à la base de stockage
		 * @return l'url de connexion à la base de stockage
		 * @throws ConnectionServiceEx
		 *             Exception lorsque la construction de l'url n'aboutie pas.
		 */
		@SuppressWarnings({ "PMD.LongVariable", "PMD.DataflowAnomalyAnalysis" })
		public static String buildUrlForConnection(
				final StorageConnectionParameter storageConnectionParameter)
				throws ConnectionServiceEx {
			String url = Constants.BLANK;
			String protocol = Constants.HTTP;
			final StorageHost storageHost = storageConnectionParameter
					.getStorageHost();
			try {
				if (storageConnectionParameter.getStorageHost().isSecure()) {
					protocol = Constants.HTTPS;
				}
				final URL urlConnection = new URL(protocol,
						storageHost.getHostName(), storageHost.getHostPort(),
						storageHost.getContextRoot());
				url = urlConnection.toString();
			} catch (MalformedURLException except) {
				throw new ConnectionServiceEx(
						StorageMessageHandler.getMessage(Constants.CNT_CODE_ERROR),
						except.getMessage(), except);
			} catch (Exception except) {
				throw new ConnectionServiceEx(
						StorageMessageHandler.getMessage(Constants.CNT_CODE_ERROR),
						except.getMessage(), except);
			}
			return url;
		}
	
		
}
