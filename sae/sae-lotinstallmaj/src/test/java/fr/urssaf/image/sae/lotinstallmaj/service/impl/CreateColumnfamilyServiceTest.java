package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;

import me.prettyprint.hector.api.Cluster;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.BeforeClass;

import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;

public class CreateColumnfamilyServiceTest {

   private static Cluster cluster;
   private static CassandraConfig config;

   @BeforeClass
   public static void init() throws Exception, IOException,
         InterruptedException, ConfigurationException {
      // On démarre un serveur cassandra local
      EmbeddedCassandraServerHelper.startEmbeddedCassandra();
      cluster = EasyMock.createMock(Cluster.class);
      config = new CassandraConfig();

   }
   
   @After
   public void end() {
      EasyMock.reset(cluster);
   }


}
