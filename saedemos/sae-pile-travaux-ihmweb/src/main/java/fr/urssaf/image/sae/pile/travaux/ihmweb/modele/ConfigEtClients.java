package fr.urssaf.image.sae.pile.travaux.ihmweb.modele;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;

public class ConfigEtClients {

   private CassandraEtZookeeperConfig config;

   private CuratorFramework zkClient;

   private CassandraClientFactory cassClient;

   public final CassandraEtZookeeperConfig getConfig() {
      return config;
   }

   public final void setConfig(CassandraEtZookeeperConfig config) {
      this.config = config;
   }

   public final CuratorFramework getZkClient() {
      return zkClient;
   }

   public final void setZkClient(CuratorFramework zkClient) {
      this.zkClient = zkClient;
   }

   public final CassandraClientFactory getCassClient() {
      return cassClient;
   }

   public final void setCassClient(CassandraClientFactory cassClient) {
      this.cassClient = cassClient;
   }

}
