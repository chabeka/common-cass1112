/**
 *
 */
package fr.urssaf.image.commons.cassandra.helper;

import java.io.File;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

import fr.urssaf.image.commons.cassandra.exception.CassandraConfigurationException;

/**
 *
 *
 */
public class CassandraCQLClientFactoryTest {

  @Test(expected = CassandraConfigurationException.class)
  public void createTestFailureConfigNull() {
    CassandraCQLClientFactory ccf = null;
    try {

      ccf = new CassandraCQLClientFactory(null);
      Assert.fail("une exception CassandraConfigurationException est attendue");
    }
    catch (final InterruptedException e) {

    }
    finally {
      if (ccf != null) {
        try {
          ccf.destroy();
        }
        catch (final Exception e) {
          Assert.fail("Destruction de cassandra impossible : " + e.getMessage());
        }
      }
    }

  }

  @Test
  public void createTestCheminFichierConfigInexistant() {

    final ClassLoader classLoader = getClass().getClassLoader();
    final File file = new File(classLoader.getResource("conf/conf_test.properties").getFile());
    final FileSystemResource ress = new FileSystemResource(file);
    CassandraCQLClientFactory ccf = null;
    try {

      ccf = new CassandraCQLClientFactory(ress);
      Assert.assertNotNull("CassandraCQLClientFactory doit être non null", ccf);
      Assert.assertNull("La session est null", ccf.getSession());
    }
    catch (final InterruptedException e) {
      Assert.fail("une exception CassandraConfigurationException a été detecté");
    }
    finally {
      if (ccf != null) {
        try {
          ccf.destroy();
        }
        catch (final Exception e) {
          Assert.fail("Destruction de cassandra impossible : " + e.getMessage());
        }
      }
    }
  }

  @Test
  public void createTestConfigSuccess() {

    final ClassLoader classLoader = getClass().getClassLoader();
    final File file = new File(classLoader.getResource("conf/commons-config_test.properties").getFile());
    final FileSystemResource ress = new FileSystemResource(file);
    CassandraCQLClientFactory ccf = null;
    try {

      ccf = new CassandraCQLClientFactory(ress);
      Assert.assertNotNull("CassandraCQLClientFactory doit être non nul", ccf);
      Assert.assertNotNull("La session doit être non nul", ccf.getSession());
      Assert.assertNotNull("Le cluster doit être non nul", ccf.getCluster());
      Assert.assertNotNull("Le server est non nul", ccf.getServer());
      if (ccf.getStartLocal()) {
        Assert.assertTrue("Le nom du keyspace est incorrect", "KEYSPACE_TU".equals(ccf.getKeyspace()));
      }
    }
    catch (final Exception e) {
      Assert.fail("une exception CassandraConfigurationException a été detecté");
    }
    finally {
      if (ccf != null) {
        try {
          ccf.destroy();
        }
        catch (final Exception e) {
          Assert.fail("Destruction de cassandra impossible : " + e.getMessage());
        }
      }
    }
  }

  @Test
  public void createTestYAMLFileCreation() {
    final String tmpDir = EmbeddedCassandraServerHelper.DEFAULT_TMP_DIR;
    final String yamlFile = "/" + EmbeddedCassandraServerHelper.DEFAULT_CASSANDRA_YML_FILE;
    final File file = new File(tmpDir + yamlFile);
    if (file.exists()) {
      file.delete();
    }
    Assert.assertTrue("Le fichier de confi yaml ne doit pas exister", !file.exists());

    CassandraCQLClientFactory ccf = null;
    try {
      final ClassLoader classLoader = getClass().getClassLoader();
      final File ressFile = new File(classLoader.getResource("conf/commons-config_test.properties").getFile());
      final FileSystemResource ress = new FileSystemResource(ressFile);
      ccf = new CassandraCQLClientFactory(ress);
      ccf.getServer().resetData();
      final File fileReseted = new File(tmpDir + yamlFile);
      Assert.assertTrue("", fileReseted.exists());
    }
    catch (final Exception e) {
      Assert.fail("Exception non prévu : " + e.getMessage());
    }
    finally {
      if (ccf != null) {
        try {
          ccf.destroy();
        }
        catch (final Exception e) {
          Assert.fail("Destruction de cassandra impossible : " + e.getMessage());
        }
      }
    }
  }
}
