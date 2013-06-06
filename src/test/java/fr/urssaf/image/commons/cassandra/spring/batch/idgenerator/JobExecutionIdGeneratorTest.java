package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.cassandraunit.AbstractCassandraUnit4TestCase;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.test.TestingServer;

import fr.urssaf.image.commons.cassandra.spring.batch.support.JobClockSupportFactory;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;

public class JobExecutionIdGeneratorTest extends AbstractCassandraUnit4TestCase {

   private TestingServer zkServer;
   private CuratorFramework zkClient;

   @Override
   public DataSet getDataSet() {
      return new ClassPathXmlDataSet("dataSet-commons-cassandra-spring-batch.xml");
   }

   @Before
   public void init() throws Exception {
      // Connexion à un serveur zookeeper local
      initZookeeperServer();
      zkClient = ZookeeperClientFactory.getClient(zkServer.getConnectString(), "Batch");
   }

   @After
   public void clean() {
      zkServer.close();
   }

   private void initZookeeperServer() throws Exception {
      if (zkServer == null)
         zkServer = new TestingServer();
   }

   @Test
   public void testMonoThread() {
      
      JobClockSupport clockSupport =  JobClockSupportFactory.createJobClockSupport(getKeyspace());
      
      IdGenerator generator = new JobExecutionIdGenerator(getKeyspace(),
            zkClient,clockSupport);
      for (int i = 1; i < 5; i++) {
         Assert.assertEquals(i, generator.getNextId());
      }
   }

   @Test
   public void testMultiThread() throws InterruptedException {
      
      JobClockSupport clockSupport =  JobClockSupportFactory.createJobClockSupport(getKeyspace());
      
      IdGenerator generator = new JobExecutionIdGenerator(getKeyspace(),
            zkClient,clockSupport);
      Map<Long, Long> map = new ConcurrentHashMap<Long, Long>();
      SimpleThread[] threads = new SimpleThread[10]; 
      for (int i = 0; i < 10; i++) {
         threads[i] = new SimpleThread(generator, map);
         threads[i].start();
      }
      for (int i = 0; i < 10; i++) {
         threads[i].join();
      }
      Assert.assertEquals(50, map.size());
      for (Entry<Long, Long> entry : map.entrySet()) {
         System.out.print(entry.getKey() + " ");
      }
   }

   private class SimpleThread extends Thread {
      
      IdGenerator generator;
      Map<Long, Long> map;
      
      public SimpleThread(IdGenerator generator, Map<Long, Long> map) {
         super();
         this.generator = generator;
         this.map = map;
      }

      public void run() {
         for (int i = 0; i < 5; i++) {
            long id = generator.getNextId();
            map.put(id, id);
         }
      }
   }

}
