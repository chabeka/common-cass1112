package fr.urssaf.image.sae.mapping.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import fr.urssaf.image.sae.bo.model.SAEMetadataType;
import fr.urssaf.image.sae.mapping.constants.Constants;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Cette classe contient des méthodes utilitaires
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
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
    * Convertie une chaîne en Date UTC
    * 
    * @param date
    *           : La chaîne à convertir.
    * @return une date à partir d'une chaîne.
    * @throws ParseException
    *            Exception lorsque le parsing de la chaîne ne se déroule pas
    *            bien.
    */
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public static Date stringToDate(final String date) throws ParseException {
      Date newDate = new Date();
      if (date != null) {
         SimpleDateFormat formatter = new SimpleDateFormat(
               Constants.DATE_PATTERN, Constants.DEFAULT_LOCAL);
         formatter.setLenient(false);
         formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
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
    * Convertie une chaîne en Datetime UTC
    * 
    * @param date
    *           : La chaîne à convertir.
    * @return une date à partir d'une chaîne.
    * @throws ParseException
    *            Exception lorsque le parsing de la chaîne ne se déroule pas
    *            bien.
    */
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public static Date stringToDatetime(final String date) throws ParseException {
      Date newDate = new Date();
      if (date != null) {
         SimpleDateFormat formatter = new SimpleDateFormat(
               Constants.DATETIME_PATTERN, Constants.DEFAULT_LOCAL);
         formatter.setLenient(false);
         //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
         newDate = formatter.parse(date);
         if (formatter.parse(date, new ParsePosition(0)) == null) {
            formatter = new SimpleDateFormat(Constants.DATETIME_PATTERN,
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
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public static String dateToString(final Date date) throws ParseException {
      String newDate = Constants.BLANK;
      if (date != null) {
         final SimpleDateFormat formatter = new SimpleDateFormat(
               Constants.DATE_PATTERN, Constants.DEFAULT_LOCAL);
         newDate = formatter.format(date);
      }
      return newDate;
   }
   
   /**
    * Convertit un datetime en chaîne
    * 
    * @param date
    *           : La date à convertir
    * @return chaîne à partir d'une date
    * @throws ParseException
    *            Exception lorsque le parsing de la chaîne ne se déroule pas
    *            bien.
    */
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public static String datetimeToString(final Date date) throws ParseException {
      String newDate = Constants.BLANK;
      if (date != null) {
         final SimpleDateFormat formatter = new SimpleDateFormat(
               Constants.DATETIME_PATTERN, Constants.DEFAULT_LOCAL);
         newDate = formatter.format(date);
      }
      return newDate;
   }

   /**
    * Permet de convertir un valeur de la métadonnée métier typée en chaîne de
    * caractère
    * 
    * @param metadataValue
    *           : la valeur de la métadonnée.
    * @return une chaîne de caractère.
    * @param reference
    *           : La métadonnée du referentiel.
    * @throws ParseException
    *            Exception levée lorsque la convertion n'aboutie pas
    * @throws ParseException
    */
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public static String convertToString(final Object metadataValue,
         final MetadataReference reference) throws ParseException {
      final SAEMetadataType saeType = typeFinder(reference.getType());
      String target = null;
      switch (saeType) {
      case DATE:
         if (!"".equals(metadataValue)) {
            target = dateToString((Date) metadataValue);
         }
         break;
      case DATETIME:
         if (!"".equals(metadataValue)) {
            target = datetimeToString((Date) metadataValue);
         }
         break;
      case UUID:
      case INTEGER:
      case BOOLEAN:
      case STRING:
      default:
         target = String.valueOf(metadataValue);
         break;
      }
      return target;
   }

   /**
    * Permet de trouver le bon type dans l'enumération des types
    * 
    * @param type
    *           : Le type cherché
    * @return Le type métier correspondant au type cherché.
    */
   @SuppressWarnings("PMD.OnlyOneReturn")
   private static SAEMetadataType typeFinder(final String type) {
      for (SAEMetadataType saeType : SAEMetadataType.values()) {
         if (saeType.getType().equalsIgnoreCase(type)) {
            return saeType;
         }
      }
      return null;
   }

   /**
    * Permet de convertir les valeur des metadonnées {@link fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata} en
    * objet.
    * 
    * @param untypedValue
    *           : La valeur d'une métadonnée
    * @param reference
    *           : La métadonnée cu referentiel.
    * @return une objet
    * @throws ParseException
    *            Exception lever lorsque le parsing de la date n'aboutie pas.
    */
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public static Object conversionToObject(final String untypedValue,
         final MetadataReference reference) throws ParseException {
      final SAEMetadataType saeType = typeFinder(reference.getType());

      Object value = null;
      switch (saeType) {
      case DATE:
         value = Utils.stringToDate(untypedValue);
         break;
      case DATETIME:
         value = Utils.stringToDatetime(untypedValue);
         break;
      case INTEGER:
         value = Integer.valueOf(untypedValue);
         break;
      case BOOLEAN:
         value = Boolean.valueOf(untypedValue);
         break;
      case LONG:
         value = Long.valueOf(untypedValue);
         break;
      case UUID:
         value = UUID.fromString(untypedValue);
         break;
      default:
         value = untypedValue;
         break;
      }
      return value;
   }

}
