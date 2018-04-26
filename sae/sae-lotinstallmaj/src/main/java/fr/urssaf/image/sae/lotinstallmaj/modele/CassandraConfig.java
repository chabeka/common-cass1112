package fr.urssaf.image.sae.lotinstallmaj.modele;

/**
 * Configuration d'acces à cassandra
 * 
 *
 */
public final class CassandraConfig {

   private String hosts;
   private String login="";
   private String password="";
   private String keyspaceName;
   private int timeout;

   /**
    * @return Chaîne de connexion aux serveurs cassandra
    */
   public String getHosts() {
      return hosts;
   }
   /**
    * @param hosts Chaîne de connexion aux serveurs cassandra
    */
   public void setHosts(String hosts) {
      this.hosts = hosts;
   }
   
   /**
    * @return login de connexion à cassandra
    */
   public String getLogin() {
      return login;
   }
   /**
    * @param login de connexion à cassandra
    */
   public void setLogin(String login) {
      this.login = login;
   }
   /**
    * @return password de connexion à cassandra
    */
   public String getPassword() {
      return password;
   }
   /**
    * @param password de connexion à cassandra
    */
   public void setPassword(String password) {
      this.password = password;
   }
   /**
    * @return nom du keyspace à utiliser.
    */
   public String getKeyspaceName() {
      return keyspaceName;
   }
   /**
    * @param keyspaceName nom du keyspace à utiliser.
    */
   public void setKeyspaceName(String keyspaceName) {
      this.keyspaceName = keyspaceName;
   }
   
   /**
    * @return le timeout de la connexion à CASSANDRA
    */
   public final int getTimeout() {
      return timeout;
   }
   
   /**
    * @param timeout le timeout de la connexion à CASSANDRA
    */
   public final void setTimeout(int timeout) {
      this.timeout = timeout;
   }
   
   
}
