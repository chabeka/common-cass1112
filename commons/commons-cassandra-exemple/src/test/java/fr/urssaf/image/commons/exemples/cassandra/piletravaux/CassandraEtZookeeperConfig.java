package fr.urssaf.image.commons.exemples.cassandra.piletravaux;

/**
 * Configuration de connexion à Zookeeper et Cassandra
 */
public class CassandraEtZookeeperConfig {

   private String zookeeperHosts;
   private String zookeeperNamespace;

   private String cassandraHosts;
   private String cassandraUserName;
   private String cassandraPassword;
   private String cassandraKeySpace;

   public final String getZookeeperHosts() {
      return zookeeperHosts;
   }

   public final void setZookeeperHosts(String zookeeperHosts) {
      this.zookeeperHosts = zookeeperHosts;
   }

   public final String getZookeeperNamespace() {
      return zookeeperNamespace;
   }

   public final void setZookeeperNamespace(String zookeeperNamespace) {
      this.zookeeperNamespace = zookeeperNamespace;
   }

   public final String getCassandraHosts() {
      return cassandraHosts;
   }

   public final void setCassandraHosts(String cassandraHosts) {
      this.cassandraHosts = cassandraHosts;
   }

   public final String getCassandraUserName() {
      return cassandraUserName;
   }

   public final void setCassandraUserName(String cassandraUserName) {
      this.cassandraUserName = cassandraUserName;
   }

   public final String getCassandraPassword() {
      return cassandraPassword;
   }

   public final void setCassandraPassword(String cassandraPassword) {
      this.cassandraPassword = cassandraPassword;
   }

   public final String getCassandraKeySpace() {
      return cassandraKeySpace;
   }

   public final void setCassandraKeySpace(String cassandraKeySpace) {
      this.cassandraKeySpace = cassandraKeySpace;
   }

}
