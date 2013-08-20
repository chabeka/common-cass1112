/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.bean;

/**
 * classe représentant les paramètres CASSANDRA
 * 
 */
public class CassandraConfig {

   private String servers;
   
   private String user;
   
   private String password;
   
   private String cluster;
   
   private String keyspace;
   
   private int port;
   
   private String baseUuid;

   /**
    * @return the servers
    */
   public final String getServers() {
      return servers;
   }

   /**
    * @param servers the servers to set
    */
   public final void setServers(String servers) {
      this.servers = servers;
   }

   /**
    * @return the user
    */
   public final String getUser() {
      return user;
   }

   /**
    * @param user the user to set
    */
   public final void setUser(String user) {
      this.user = user;
   }

   /**
    * @return the password
    */
   public final String getPassword() {
      return password;
   }

   /**
    * @param password the password to set
    */
   public final void setPassword(String password) {
      this.password = password;
   }

   /**
    * @return the cluster
    */
   public final String getCluster() {
      return cluster;
   }

   /**
    * @param cluster the cluster to set
    */
   public final void setCluster(String cluster) {
      this.cluster = cluster;
   }

   /**
    * @return the keyspace
    */
   public final String getKeyspace() {
      return keyspace;
   }

   /**
    * @param keyspace the keyspace to set
    */
   public final void setKeyspace(String keyspace) {
      this.keyspace = keyspace;
   }

   /**
    * @return the port
    */
   public final int getPort() {
      return port;
   }

   /**
    * @param port the port to set
    */
   public final void setPort(int port) {
      this.port = port;
   }

   /**
    * @return the baseUuid
    */
   public final String getBaseUuid() {
      return baseUuid;
   }

   /**
    * @param baseUuid the baseUuid to set
    */
   public final void setBaseUuid(String baseUuid) {
      this.baseUuid = baseUuid;
   }
   
   
   
}
