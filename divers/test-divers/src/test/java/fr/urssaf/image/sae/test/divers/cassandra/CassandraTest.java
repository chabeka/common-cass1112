package fr.urssaf.image.sae.test.divers.cassandra;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-integ-interne.xml" })
public class CassandraTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(CassandraTest.class);
   
   @Autowired
   private Keyspace keyspace;

   @Test
   @Ignore
   public void testVerrouZookeeper() {
      
      CuratorFramework zkClient = null;
      try {
         
         LOGGER.debug("Recuperation du client");
         zkClient = ZookeeperClientFactory.getClient("hwi69devsaeapp1.cer69.recouv", "SAE");
         
         for (int index = 0; index < 15; index++) {
            
            
            LOGGER.debug("Creation du mutext");
            ZookeeperMutex mutext = new ZookeeperMutex(zkClient, "/Cedric/Test");
            LOGGER.debug("Acquisition du verrou");
            mutext.acquire(20, TimeUnit.SECONDS);
            
            Thread.sleep(2000);
            
            LOGGER.debug("Release du verrou");
            mutext.release();
         }
      } catch (IOException e) {
         LOGGER.error("IOException : {}", e.getMessage());
      } catch (InterruptedException e) {
         LOGGER.error("InterruptedException : {}", e.getMessage());
      } finally {
         if (zkClient != null) {
            LOGGER.debug("Fermeture de la connexion cliente");
            zkClient.close();
         }
      }
   }
   
   @Test
   public void getStepSpringBatch() throws Exception {
      
      Assert.assertNotNull("L'objet aurait du etre non null", keyspace);
      
      String idJob = "119";
      
      SliceQuery<String,String,String> query = HFactory.createSliceQuery(keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
      query.setColumnFamily("JobExecution").setKey(idJob).setRange(null, null, false, 100);
      QueryResult<ColumnSlice<String,String>> result = query.execute();
      for (HColumn<String, String> column : result.get().getColumns()) {
          System.out.println(column.getName() +"::"+ column.getValue());
      }
   }
   
   @Test
   public void getTokenCassandra() throws Exception {
      
      //String keyValue = "ACCES_FULL_PAGMA";
      String keyValue = "PAGM_V2_ARCHIVAGE_PI06F_PI06_L06_PAGMa";
      LOGGER.debug("Valeur de cle a convertir: {}", keyValue);
      
      // calcule le resume de la cle en md5
      String resume;
      BigInteger resumeEnDecimal;
      try {
         MessageDigest mdEnc = MessageDigest.getInstance("MD5"); // Encryption algorithm
         mdEnc.update(keyValue.getBytes(), 0, keyValue.length());
         resumeEnDecimal = new BigInteger(1, mdEnc.digest());
         resume = resumeEnDecimal.toString(16); // Encrypted string
      } catch (Exception ex) {
         throw ex;
      }
      LOGGER.debug("Resume de la cle en hexa: {}", resume);
      LOGGER.debug("Resume de la cle en decimal: {}", resumeEnDecimal);
      
      // supprime le premier bit
      String token = resume.substring(2);
      BigInteger tokenEnDecimal = new BigInteger(token, 16);
      LOGGER.debug("Token en hexa: {}", token);
      LOGGER.debug("Token en decimal: {}", tokenEnDecimal);
   }
}
