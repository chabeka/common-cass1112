package fr.urssaf.image.sae.storage.dfce.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;

/**
 * Cette classe contient des méthodes utilitaires
 * 
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
   * Permet de trouver le bon type dans l'enumération des types.
   * 
   * @param shortCode
   *           : Le code court cherché
   * @return Le type métier correspondant au type cherché.
   */
  public static StorageTechnicalMetadatas technicalMetadataFinder(
                                                                  final String shortCode) {
    for (final StorageTechnicalMetadatas technical : StorageTechnicalMetadatas
        .values()) {
      if (technical.getShortCode().equals(shortCode)) {
        return technical;
      }
    }
    return StorageTechnicalMetadatas.NOVALUE;
  }


}
