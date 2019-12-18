/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.trace.dao.IGenericTraceTypeDao;

/**
 * TODO (AC75095028) Description du type
 */
public class MigrationTrace {

  // public String keyspace_tu = "keyspace_tu";

  protected static final Date DATE = new Date();

  @Autowired
  protected IGenericTraceTypeDao genericdao;

  @Autowired
  protected CassandraCQLClientFactory ccfcql;

  // @Qualifier("CassandraClientFactory")
  @Autowired
  protected CassandraClientFactory ccfthrift;

  protected String getKeyFileDir() {

    String path = null;

    try (InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream("config/commons-config.properties")) {

      final Properties prop = new Properties();

      if (input == null) {
        System.out.println("Problème de chargement du fichier propertie");
        return "";
      }

      // load a properties file from class path, inside static method
      prop.load(input);
      System.out.println(prop.getProperty("sae.migration.cheminFichiersReprise"));
      // get the property value and print it out
      path = prop.getProperty("sae.migration.cheminFichiersReprise");

    }
    catch (final IOException ex) {
      ex.printStackTrace();
    }
    return path;
  }
}
