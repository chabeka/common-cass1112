/**
 *  TODO (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.format.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.Column;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;

/**
 * TODO (AC75095351) Classe pour extraction de FormatFichiers à partir dataset thrift
 */
public class FormatFichierUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(FormatFichierUtils.class);

  private final static String TYPE_MIME = "typeMime";

  private final static String EXTENSION = "extension";

  private final static String DESCRIPTION = "description";

  private final static String AUTORISE_GED = "autoriseGED";

  private final static String VISUALISABLE = "visualisable";

  private final static String VALIDATOR = "validator";

  private final static String IDENTIFICATEUR = "identifieur";

  private final static String CONVERTISSEUR = "convertisseur";

  /**
   * Conversion de lignes thrift en formatFichiers
   * 
   * @param liste
   *          de lignes thrifts
   * @return liste de formatFichier
   */
  public static List<FormatFichier> convertRowsToFormatFichier(final List<Row> list) {
    final List<FormatFichier> listFormatFichier = new ArrayList<>();
    int i = 0;
    final String value = "";
    for (final Row row : list) {
      final Map<String, String> map = new HashMap<>();
      if (row != null && row.getKey() != null) {
        for (final Column column : row.getColumns()) {
          if (column != null && column.getName() != null && column.getValue() != null) {

            map.put(column.getName(), DataCqlUtils.cleanValue(column.getValue()));
          }
        }
        final FormatFichier formatFichier = getFormatFichier(row.getKey(), map);
        listFormatFichier.add(formatFichier);
      }
      i++;
    }

    return listFormatFichier;
  }

  /**
   * Conversion en formatFichier à partir de données thrift
   * 
   * @param key
   * @param map
   * @return FormatFichier
   */
  private static FormatFichier getFormatFichier(final String key, final Map<String, String> map) {
    final FormatFichier formatFichier = new FormatFichier();
    formatFichier.setIdFormat(key);
    if (map.get(TYPE_MIME) != null) {
      formatFichier.setTypeMime(map.get(TYPE_MIME));
    } else {
      formatFichier.setTypeMime(null);
    }
    if (map.get(EXTENSION) != null) {
      formatFichier.setExtension(map.get(EXTENSION));
    } else {
      formatFichier.setExtension(null);
    }
    if (map.get(DESCRIPTION) != null) {
      formatFichier.setDescription(map.get(DESCRIPTION));
    } else {
      formatFichier.setDescription(null);
    }
    if (map.get(AUTORISE_GED) != null) {
      formatFichier.setAutoriseGED(Integer.parseInt(map.get(AUTORISE_GED)) == 1);
    } else {
      formatFichier.setAutoriseGED(false);
    }
    if (map.get(VISUALISABLE) != null) {
      formatFichier.setVisualisable(Integer.parseInt(map.get(VISUALISABLE)) == 1);
    } else {
      formatFichier.setVisualisable(false);
    }
    if (map.get(VALIDATOR) != null) {
      formatFichier.setValidator(map.get(VALIDATOR));
    } else {
      formatFichier.setValidator(null);
    }

    if (map.get(IDENTIFICATEUR) != null) {
      formatFichier.setIdentificateur(map.get(IDENTIFICATEUR));
    } else {
      formatFichier.setIdentificateur(null);
    }
    if (map.get(CONVERTISSEUR) != null) {
      formatFichier.setConvertisseur(map.get(CONVERTISSEUR));
    } else {
      formatFichier.setConvertisseur(null);
    }

    return formatFichier;
  }
}
