package fr.urssaf.image.commons.cassandra.spring.batch.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.netflix.curator.test.TestingServer;

/**
 * Classe utilitaire facilitant la création d'un serveur zookeeper local
 * par un bean spring.
 *
 */
public class LocalZookeeperServerBean implements InitializingBean, DisposableBean  {

   private static final Logger LOG = LoggerFactory.getLogger(LocalZookeeperServerBean.class);
   private static final int DEFAULT_PORT = 2181;
   private int port = DEFAULT_PORT; 
   private boolean shouldInit= false;
   private TestingServer testingServer = null;

   /**
    * Affecte le port du serveur qui doit être lancé
    * @param port Le n° du port
    */
   public final void setPort(int port) {
      this.port = port;
   }
   
   /**
    * Indique s'il faut lancer un serveur zookeeper local
    * @param init vrai s'il faut lancer un serveur local
    */
   public final void setShouldInit(boolean init) {
      shouldInit = init;
   }
   
   @Override
   public final void destroy() throws Exception {
      if (testingServer != null) {
         LOG.debug("LocalZookeeperServerBean : closing server...");
         testingServer.close();
      }
   }

   @Override
   public final void afterPropertiesSet() throws Exception {
      LOG.debug("LocalZookeeperServerBean : shouldInit={} - port={}", shouldInit, port);
      if (shouldInit) testingServer = new TestingServer(port);
   }
}
