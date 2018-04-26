package fr.urssaf.image.sae.pile.travaux.ihmweb.modele;

/**
 * Configuration Cassandra+Zookeeper pour affichage uniquement<br>
 * <br>
 * Singleton à créé via un fichier de contexte Spring, en injectant par setter
 * les placeholders du fichier de config Cassandra
 * 
 */
public class CassandraConfig {

   private String cassandraHosts;
   private String zookeeperHosts;

   /**
    * Serveur(s) Cassandra
    * 
    * @return Serveur(s) Cassandra
    */
   public final String getCassandraHosts() {
      return cassandraHosts;
   }

   /**
    * Serveur(s) Cassandra
    * 
    * @param cassandraHosts
    *           Serveur(s) Cassandra
    */
   public final void setCassandraHosts(String cassandraHosts) {
      this.cassandraHosts = cassandraHosts;
   }

   /**
    * Serveur(s) Zookeeper
    * 
    * @return Serveur(s) Zookeeper
    */
   public final String getZookeeperHosts() {
      return zookeeperHosts;
   }

   /**
    * Serveur(s) Zookeeper
    * 
    * @param zookeeperHosts
    *           Serveur(s) Zookeeper
    */
   public final void setZookeeperHosts(String zookeeperHosts) {
      this.zookeeperHosts = zookeeperHosts;
   }

}
