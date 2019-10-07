/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.metadata.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.Column;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * (AC75095351) Classe spécifique aux Metadata pour extraction des données à partir de datasets
 */
public class MetadataUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(MetadataUtils.class);

  /**
   * Conversion des données thrift en objets MetadataReference
   * 
   * @param list
   * @return liste des MetadataReference
   */
  public static List<MetadataReference> convertRowsToMetadata(final List<Row> list) {
    final List<MetadataReference> listMetadata = new ArrayList<>();
    int i = 0;
    for (final Row row : list) {

      final Map<String, String> map = new HashMap<>();
      if (row != null && row.getKey() != null) {
        for (final Column column : row.getColumns()) {
          if (column != null && column.getName() != null && column.getValue() != null) {

            map.put(column.getName(), DataCqlUtils.cleanValue(column.getValue()));
          }
        }
        final MetadataReference metadataReference = getMetadata(row.getKey(), map);
        listMetadata.add(metadataReference);
      }
      i++;
    }

    return listMetadata;
  }

  /**
   * Création d'un objet MetadataReference à partir des données thrift
   * 
   * @param key
   * @param map
   * @return MetadataReference
   */
  private static MetadataReference getMetadata(final String key, final Map<String, String> map) {
    final MetadataReference metadataReference = new MetadataReference();
    metadataReference.setLongCode(key);
    if (map.get("arch") != null) {
      metadataReference.setArchivable(Integer.parseInt(map.get("arch")) == 1);
    } else {
      metadataReference.setArchivable(false);
    }
    if (map.get("cons") != null) {
      metadataReference.setConsultable(Integer.parseInt(map.get("cons")) == 1);
    } else {
      metadataReference.setConsultable(false);
    }
    if (map.get("defCons") != null) {
      metadataReference.setDefaultConsultable(Integer.parseInt(map.get("defCons")) == 1);
    } else {
      metadataReference.setDefaultConsultable(false);
    }
    if (map.get("descr") != null) {
      metadataReference.setDescription(map.get("descr"));
    } else {
      metadataReference.setDescription("");
    }
    if (map.get("int") != null) {
      metadataReference.setInternal(Integer.parseInt(map.get("int")) == 1);
    } else {
      metadataReference.setInternal(false);
    }
    if (map.get("label") != null) {
      metadataReference.setLabel(map.get("label"));
    } else {
      metadataReference.setLabel("");
    }
    if (map.get("length") != null) {
      metadataReference.setLength(Integer.parseInt(map.get("length")));
    } else {
      metadataReference.setLength(0);
    }
    if (map.get("pattern") != null) {
      metadataReference.setPattern(map.get("pattern"));
    } else {
      metadataReference.setPattern("");
    }
    if (map.get("reqArch") != null) {
      metadataReference.setRequiredForArchival(Integer.parseInt(map.get("reqArch")) == 1);
    } else {
      metadataReference.setRequiredForArchival(false);
    }
    if (map.get("reqStor") != null) {
      metadataReference.setRequiredForStorage(Integer.parseInt(map.get("reqStor")) == 1);
    } else {
      metadataReference.setRequiredForStorage(false);
    }
    if (map.get("search") != null) {
      metadataReference.setSearchable(Integer.parseInt(map.get("search")) == 1);
    } else {
      metadataReference.setSearchable(false);
    }
    if (map.get("sCode") != null) {
      metadataReference.setShortCode(map.get("sCode"));
    } else {
      metadataReference.setShortCode("");
    }
    if (map.get("type") != null) {
      metadataReference.setType(map.get("type"));
    } else {
      metadataReference.setType("");
    }
    if (map.get("hasDict") != null) {
      metadataReference.setHasDictionary(Integer.parseInt(map.get("hasDict")) == 1);
    } else {
      metadataReference.setHasDictionary(false);
    }
    if (map.get("dictName") != null) {
      metadataReference.setDictionaryName(map.get("dictName"));
    } else {
      metadataReference.setDictionaryName("");
    }
    if (map.get("index") != null) {
      metadataReference.setIsIndexed(Integer.parseInt(map.get("index")) == 1);
    } else {
      metadataReference.setIsIndexed(false);
    }
    if (map.get("leftTrim") != null) {
      metadataReference.setLeftTrimable(Integer.parseInt(map.get("leftTrim")) == 1);
    } else {
      metadataReference.setLeftTrimable(false);
    }
    if (map.get("rightTrim") != null) {
      metadataReference.setRightTrimable(Integer.parseInt(map.get("rightTrim")) == 1);
    } else {
      metadataReference.setRightTrimable(false);
    }
    if (map.get("transf") != null) {
      metadataReference.setTransferable(Integer.parseInt(map.get("transf")) == 1);
    } else {
      metadataReference.setTransferable(false);
    }
    if (map.get("dispo") != null) {
      metadataReference.setClientAvailable(Integer.parseInt(map.get("dispo")) == 1);
    } else {
      metadataReference.setClientAvailable(false);
    }
    if (map.get("update") != null) {
      metadataReference.setModifiable(Integer.parseInt(map.get("update")) == 1);
    } else {
      metadataReference.setModifiable(false);
    }

    return metadataReference;
  }
}
