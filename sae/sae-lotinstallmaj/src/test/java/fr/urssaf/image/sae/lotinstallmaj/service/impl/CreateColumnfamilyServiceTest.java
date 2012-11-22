package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;

import org.apache.cassandra.config.ConfigurationException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;
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
