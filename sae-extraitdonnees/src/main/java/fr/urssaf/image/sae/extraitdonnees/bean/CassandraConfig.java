package fr.urssaf.image.sae.extraitdonnees.bean;

/**
 * Paramètres de connexion à Cassandra
 */
public class CassandraConfig {

   private String servers;

   private String user;

   private String password;

   private int port;

   /**
    * L'adresse du ou des serveurs Cassandra (séparer les serveurs par une
    * virgule)
    * 
    * @return L'adresse du ou des serveurs Cassandra (séparer les serveurs par
    *         une virgule)
    */
   public final String getServers() {
      return servers;
   }

   /**
    * L'adresse du ou des serveurs Cassandra (séparer les serveurs par une
    * virgule)
    * 
    * @param servers
    *           L'adresse du ou des serveurs Cassandra (séparer les serveurs par
    *           une virgule)
    */
   public final void setServers(String servers) {
      this.servers = servers;
   }

   /**
    * Le login de connexion à Cassandra
    * 
    * @return Le login de connexion à Cassandra
    */
   public final String getUser() {
      return user;
   }

   /**
    * Le login de connexion à Cassandra
    * 
    * @param user
    *           Le login de connexion à Cassandra
    */
   public final void setUser(String user) {
      this.user = user;
   }

   /**
    * Le mot de passe de connexion à Cassandra
    * 
    * @return Le mot de passe de connexion à Cassandra
    */
   public final String getPassword() {
      return password;
   }

   /**
    * Le mot de passe de connexion à Cassandra
    * 
    * @param password
    *           Le mot de passe de connexion à Cassandra
    */
   public final void setPassword(String password) {
      this.password = password;
   }

   /**
    * Le numéro de port du service Cassandra
    * 
    * @return Le numéro de port du service Cassandra
    */
   public final int getPort() {
      return port;
   }

   /**
    * Le numéro de port du service Cassandra
    * 
    * @param port
    *           Le numéro de port du service Cassandra
    */
   public final void setPort(int port) {
      this.port = port;
   }

}
