package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import me.prettyprint.cassandra.service.clock.AbstractClockResolution;
import me.prettyprint.hector.api.ClockResolution;

import org.cassandraunit.AbstractCassandraUnit4TestCase;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.test.TestingServer;

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
      IdGenerator generator = new JobExecutionIdGenerator(getKeyspace(),
            zkClient);
      for (int i = 1; i < 5; i++) {
         Assert.assertEquals(i, generator.getNextId());
      }
   }

   @Test
   public void testMultiThread() throws InterruptedException {
      IdGenerator generator = new JobExecutionIdGenerator(getKeyspace(),
            zkClient);
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
   
   @Test
   public void testDistordedClock() throws InterruptedException {

      // 1er appel avec la "vrai" heure
      CassandraIdGenerator generator1 = new CassandraIdGenerator(getKeyspace(),
            zkClient, "sequenceId");
      Assert.assertEquals(1, generator1.getNextId());
      
      // 2eme appel avec une heure décalée de 5 seconde. Ca devrait passer.
      CassandraIdGenerator generator2 = new CassandraIdGenerator(getKeyspace(),
            zkClient, "sequenceId");
      generator2.setClockResolution(new DistordedMicrosecondsClockResolution(-5000L));
      Assert.assertEquals(2, generator2.getNextId());
      
      // 3eme appel avec une heure décalée de 120 secondes (supérieur au décalage autorisé).
      // Ca ne devrait pas passer.
      CassandraIdGenerator generator3 = new CassandraIdGenerator(getKeyspace(),
            zkClient, "sequenceId");
      generator3.setClockResolution(new DistordedMicrosecondsClockResolution(-120000L));
      try {
         generator3.getNextId();
         Assert.fail("Le décalage de 120 secondes ne devrait pas être autorisé");
      }
      catch (IdGeneratorException exception) {
         // ok
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

   /**
    * Permet de simuler des horloges pas bien synchronisées
    *
    */
   private class DistordedMicrosecondsClockResolution extends AbstractClockResolution implements ClockResolution {

      private static final long serialVersionUID = 98435468424165L;
      private static final long ONE_THOUSAND = 1000L;
      private final long timeLag;

      /**
       * Constructeur
       * @param timeLag : décalage d'horloge, en ms
       */
      public DistordedMicrosecondsClockResolution(long timeLag) {
         this.timeLag = timeLag;
      }
      
      @Override
      public long createClock() {
        return (getSystemMilliseconds() + timeLag) * ONE_THOUSAND;
      }

    }
   
}
