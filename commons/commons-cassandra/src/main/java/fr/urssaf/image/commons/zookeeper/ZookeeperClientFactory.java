package fr.urssaf.image.commons.zookeeper;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.CuratorFrameworkFactory.Builder;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import com.netflix.curator.retry.ExponentialBackoffRetry;

/**
 * Factory pour récupérer une connexion à zookeeper
 * 
 */
public final class ZookeeperClientFactory implements DisposableBean {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ZookeeperClientFactory.class);

   private static final int BASE_SLEEP_TIME = 100; // En milli-secondes
   private static final int MAX_RETRIES = 10;
   private CuratorFramework zkClient;

   /**
    * Constructeur
    * 
    * @param zookeeperServer
    *           correspond au serveur zookeeper qu'il faut joindre
    * @param namespace
    *           namespace de l'application
    * @throws IOException
    *            quand on n'arrive pas à joindre le serveur zookeeper
    */
   public ZookeeperClientFactory(ZookeeperServerBean zookeeperServer,
         String namespace) throws IOException {
      zkClient = getClient(zookeeperServer.getHosts(), namespace);
   }

   /**
    * @return le client de connexion à zookeeper
    */
   public CuratorFramework getClient() {
      return zkClient;
   }

   /**
    * Renvoie une connexion à zookeeper. Attention, il est de la responsabilité
    * de l'appelant de détruire l'objet (appel de la méthode close) lorsque
    * celui-ci n'est plus utilisé.
    * 
    * @param connexionString
    *           correspond à la chaîne de connexion vers le serveur zookeeper
    * @param namespace
    *           namespace de l'application
    * @return connexion
    * @throws IOException
    *            quand on n'arrive pas à joindre zookeeper
    */
   public static CuratorFramework getClient(String connexionString,
         String namespace) throws IOException {
      Builder builder = CuratorFrameworkFactory.builder();
      builder.connectString(connexionString).namespace(namespace);
      builder.retryPolicy(new ExponentialBackoffRetry(BASE_SLEEP_TIME,
            MAX_RETRIES));
      CuratorFramework zkClient = builder.build();

      zkClient.getConnectionStateListenable().addListener(
            new ConnectionStateListener() {

               @Override
               public void stateChanged(CuratorFramework client,
                     ConnectionState newState) {

                  LOGGER.debug("Etat connexion: {}", newState.toString());

               }
            });

      zkClient.start();

      return zkClient;

   }

   @Override
   public void destroy() throws Exception {
      if (zkClient != null) {
         zkClient.close();
         zkClient = null;
      }
   }

}
