package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.cassandra.config.ConfigurationException;
import org.cassandraunit.CassandraUnit;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;

public class SAECassandraUpdaterTest {

   @BeforeClass
   public static void init() throws Exception, IOException,
         InterruptedException, ConfigurationException {
      // On démarre un serveur cassandra local
      EmbeddedCassandraServerHelper.startEmbeddedCassandra();
   }

   @Test
   public void updateToVersion1() {
      CassandraConfig config = new CassandraConfig();
      config.setHosts(CassandraUnit.host);
      config.setKeyspaceName("SAE");
      SAECassandraUpdater updater = new SAECassandraUpdater(config);
      updater.updateToVersion1();
      Assert.assertEquals(1, updater.getDatabaseVersion());
   }

   @Test
   public void updateToVersion2() {
      CassandraConfig config = new CassandraConfig();
      config.setHosts(CassandraUnit.host);
      config.setKeyspaceName("SAE");
      SAECassandraUpdater updater = new SAECassandraUpdater(config);
      updater.updateToVersion2();
      Assert.assertEquals(2, updater.getDatabaseVersion());
   }
   
   @Test
   public void updateToVersion3() {
      CassandraConfig config = new CassandraConfig();
      config.setHosts(CassandraUnit.host);
      config.setKeyspaceName("SAE");
      SAECassandraUpdater updater = new SAECassandraUpdater(config);
      updater.updateToVersion3();
      Assert.assertEquals(3, updater.getDatabaseVersion());
   }
   
   /*@Test
   public void updateToVersion110() {
      CassandraConfig config = new CassandraConfig();
      config.setHosts(CassandraUnit.host);
      config.setKeyspaceName("SAE");
      DFCECassandraUpdater updater = new DFCECassandraUpdater(config);
      updater.updateToVersion110();
     
   }*/

}
