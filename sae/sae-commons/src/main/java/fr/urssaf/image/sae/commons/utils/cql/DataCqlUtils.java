/**
 *  TODO (AC75095351) 
 */
package fr.urssaf.image.sae.commons.utils.cql;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.sae.commons.utils.Row;


/**
 * (AC75095351) Classe permettant la récupération de données pour insert cql à partir de datasets thrifts
 * JAXB est utilisé pour obtenir les objets à partir des fichiers xml
 */
public class DataCqlUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataCqlUtils.class);



  /**
   * Méthode de désérialisation du keyspace à partir d'un chemin de fichier
   * 
   * @param pathfile
   * @return Keyspace
   */
  public static Keyspace deserializeKeyspace(final String pathfile) {
    Keyspace keyspace = null;
    try {
      final File file = new File(pathfile);
      LOGGER.debug("lengthFile=" + file.length());
      final XStream xstream = new XStream();
      xstream.alias("keyspace", Keyspace.class);
      xstream.alias("columnFamily", ColumnFamily.class);
      xstream.alias("columnFamilies", ColumnFamilies.class);
      xstream.addImplicitCollection(ColumnFamilies.class, "columnFamilies");
      xstream.alias("row", Row.class);
      xstream.addImplicitCollection(ColumnFamily.class, "rows");
      xstream.alias("column", Column.class);
      xstream.addImplicitCollection(Row.class, "columns");
      final InputStream is = new FileInputStream(file);
      keyspace = (Keyspace) xstream.fromXML(is);
    }
    catch (final Exception e) {
      LOGGER.error("Erreur: " + e.getMessage());
      return null;
    }
    // LOGGER.debug("keyspace= " + keyspace);
    return keyspace;
  }

  /**
   * Méthode de désérialisation du keyspace à partir d'un chemin de fichier et du nom de la columnFamily
   * 
   * @param pathfile
   * @param columnFamilyName
   * @return liste des lignes Thrift
   */
  public static List<Row> deserializeColumnFamilyToRows(final String pathfile, final String columnFamilyName) {
    List<Row> list = new ArrayList<>();
    try {
      final Keyspace keyspace = deserializeKeyspace(pathfile);
      // LOGGER.debug("keyspace=" + keyspace);
      if (keyspace != null && keyspace.getColumnFamilies() != null) {
        // LOGGER.debug("keyspace exist");
        for (final ColumnFamily columnFamily : keyspace.getColumnFamilies().getColumnFamily()) {
          if (columnFamily.getName().equals(columnFamilyName)) {
            list = columnFamily.getRows();
            break;
          }
        }
      } else {
        if (keyspace == null) {
          LOGGER.warn("keyspace null");
        } else {
          LOGGER.warn("keyspace.getColumnFamilies()  null");
        }

      }
    }
    catch (final Exception e) {
      LOGGER.error("Erreur: " + e.getMessage());
      return list;
    }

    return list;
  }

  /**
   * Permet de faire les conversions nécessaires à l'affectation/données fichier xml
   * 
   * @param value
   * @return value modifiée si besoin
   */
  public static String cleanValue(String value) {
    if (value.equals("bytes(01)")) {
      value = "1";
    } else if (value.equals("bytes(00)")) {
      value = "0";
    } else if (value.startsWith("integer(")) {
      value = value.substring(8, value.length() - 1);
    }
    return value;
  }
}
