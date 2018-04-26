package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.List;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.lotinstallmaj.dao.SAECassandraDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-lotinstallmaj-test.xml" })
public class SAECassandraDAOTest {
   
//   private static Cluster cluster;
   
//   @Autowired
//   private CassandraConfig config;
   
   @Autowired
   private SAECassandraDao saeDao;
   
   @Autowired
   private CassandraServerBean cassandraServer;
   
   @Before
   public void init() throws Exception, IOException,
         InterruptedException, ConfigurationException {
      
      // On démarre un serveur cassandra local
      EmbeddedCassandraServerHelper.startEmbeddedCassandra();
   }
   
   @After
   public void end()throws Exception  {
      cassandraServer.resetData();
   }
   
   
   /**
    * Vérification que l'initialisation du Keyspace se fait s'il est null 
    */
   @Test
   public void keyspaceInitialisationTest(){
      assertNull(saeDao.getKeyspace());
      saeDao.connectToKeySpace();
      assertNotNull(saeDao.getKeyspace());

      assertEquals("Le nom du Keyspace Cassandra est incorrect","KEYSPACE_TU", saeDao.getKeyspace().getKeyspaceName());
   }
   
   @Test
   public void getColumnFamilyDefintionTest(){
      
      saeDao.connectToKeySpace();
      
      List<ColumnFamilyDefinition> cfList = saeDao.getColumnFamilyDefintion();
      
      assertEquals("Le nombre de CF est incorrect",6,cfList.size());
      
      
   }
}
