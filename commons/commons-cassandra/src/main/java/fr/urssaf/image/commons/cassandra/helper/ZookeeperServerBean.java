package fr.urssaf.image.commons.cassandra.helper;

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
public class ZookeeperServerBean implements InitializingBean, DisposableBean  {

   private static final Logger LOG = LoggerFactory.getLogger(ZookeeperServerBean.class);
   private boolean startLocal= false;
   private TestingServer testingServer = null;
   private String hosts = null;

   /**
    * Indique s'il faut lancer un serveur zookeeper local
    * @param init vrai s'il faut lancer un serveur local
    */
   public final void setStartLocal(boolean init) {
      startLocal = init;
   }
   
   @Override
   public final void destroy() throws Exception {
      if (testingServer != null) {
         LOG.debug("ZookeeperServerBean : closing server...");
         testingServer.close();
         testingServer = null;
      }
   }

   @Override
   public final void afterPropertiesSet() throws Exception {
      LOG.debug("ZookeeperServerBean : startLocal={}", startLocal);
      if (startLocal) {
         testingServer = new TestingServer();
         LOG.debug("ZookeeperServerBean : connexionString={}", testingServer.getConnectString());
      }
   }

   /**
    * Dans le cas d'un serveur zookeeper non local, il s'agit de la chaine de connexion
    * @param hosts
    */
   public void setHosts(String hosts) {
      this.hosts = hosts;
   }

   /**
    * Renvoie la chaîne de connexion au serveur zookeeper
    * @return chaîne de connexion
    */
   public String getHosts() {
      if (testingServer != null) {
         return testingServer.getConnectString();
      }
      else {
         return hosts;
      }
   }
   
}
