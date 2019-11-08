/**
 *  TODO (AC75095351) 
 */
package fr.urssaf.image.sae.commons.utils.cql;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.commons.utils.Row;


/**
 * (AC75095351) Classe permettant la récupération de données pour insert cql à partir de datasets thrifts
 * JAXB est utilisé pour obtenir les objets à partir des fichiers xml
 */
public class DataCqlUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataCqlUtils.class);

  @Deprecated
  /**
   * Méthode de désérialisation en list<Row> à partir d'un chemin de fichier
   * 
   * @param pathfile
   * @return liste des lignes Thrift
   */
  public static List<Row> deserialize(final String pathfile) {
    ColumnFamily cf = null;
    try {
      final File file = new File(pathfile);
      JAXBContext jaxbContext;
      jaxbContext = JAXBContext.newInstance(ColumnFamily.class);
      Unmarshaller jaxbUnmarshaller;
      jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      cf = (ColumnFamily) jaxbUnmarshaller.unmarshal(file);
    }
    catch (final Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
    System.out.println(cf);
    return cf.getRows();
  }

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
      JAXBContext jaxbContext;
      jaxbContext = JAXBContext.newInstance(Keyspace.class);
      Unmarshaller jaxbUnmarshaller;
      jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      keyspace = (Keyspace) jaxbUnmarshaller.unmarshal(file);
    }
    catch (final Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
    System.out.println(keyspace);
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
    Keyspace keyspace = null;
    try {
      final File file = new File(pathfile);
      LOGGER.warn("lengthFile=" + file.length());
      // final String xmlText = new String(Files.readAllBytes(Paths.get(pathfile)));
      // LOGGER.warn("xmlText:" + xmlText);
      JAXBContext jaxbContext;
      jaxbContext = JAXBContext.newInstance(Keyspace.class);
      LOGGER.warn("jaxbContext=" + jaxbContext);
      Unmarshaller jaxbUnmarshaller;
      jaxbUnmarshaller = jaxbContext.createUnmarshaller();

      /*
       * jaxbUnmarshaller.setEventHandler(
       * new ValidationEventHandler() {
       * @Override
       * public boolean handleEvent(final ValidationEvent event) {
       * if (event.getMessage().contains("élément inattendu")) {
       * return true;
       * } else {
       * throw new RuntimeException(event.getMessage(),
       * event.getLinkedException());
       * }
       * }
       * });
       */

      LOGGER.warn("jaxbUnmarshaller=" + jaxbUnmarshaller);
      keyspace = (Keyspace) jaxbUnmarshaller.unmarshal(file);
      LOGGER.warn("keyspace=" + keyspace);

      // LOGGER.warn("ColumnFamilies=" + keyspace.getColumnFamilies().getColumnFamily().size());
      if (keyspace != null && keyspace.getColumnFamilies() != null) {
        LOGGER.warn("keyspace exist");
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
      System.out.println(e.getMessage());
      return list;// Correction
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
