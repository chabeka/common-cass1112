/**
 *  TODO (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.trace.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.Column;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;

/**
 * TODO (AC75095351) Description du type
 */
public class TraceDestinataireCqlUtils {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(TraceDestinataireCqlUtils.class);

  public static List<TraceDestinataire> convertRowsToTraceDestinataires(final List<Row> list) {
    final List<TraceDestinataire> listTraceDestinataires = new ArrayList<>();

    try {
      for (final Row row : list) {

        if (row != null && row.getKey() != null) {
          for (final Column column : row.getColumns()) {
            if (column != null && column.getName() != null && column.getValue() != null) {
              // Conversion de la value xml en Map<String, List<String>>
              final Map<String, List<String>> map = new HashMap<>();
              final List<String> listValue = ListSerializer.get().fromBytes(column.getValue().getBytes("UTF-8"));
              map.put(column.getName(), listValue);
              final TraceDestinataire traceDestinataire = getTraceDestinataire(row.getKey(), map);
              listTraceDestinataires.add(traceDestinataire);
            }
          }
        }
      }
    }
    catch (final UnsupportedEncodingException e) {
      LOGGER.error("Une erreur s'est produite lors de la conversion des  TraceDestinataires", e.getMessage());
    }

    return listTraceDestinataires;
  }

  private static TraceDestinataire getTraceDestinataire(final String key, final Map<String, List<String>> map) {
    final TraceDestinataire traceDestinataire = new TraceDestinataire();
    traceDestinataire.setCodeEvt(key);
    traceDestinataire.setDestinataires(map);

    return traceDestinataire;
  }
}
