package fr.urssaf.image.sae.batch.documents.executable.model;

import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * Classe représentant une configuration d'environnement SAE
 * @author Michael P. O
 */
public class ConfigurationEnvironnement {

  /**
   * Nom associé à la configuration
   */
  private String nom;

   /**
   * @param nom
   *          the nom to set
   */
  public void setNom(String nom) {
    this.nom = nom;
  }

  /**
   * @param zookeeperHost
   *          the zookeeperHost to set
   */
  public void setZookeeperHost(String zookeeperHost) {
    this.zookeeperHost = zookeeperHost;
  }

  /**
   * @param zookeeperNameSpace
   *          the zookeeperNameSpace to set
   */
  public void setZookeeperNameSpace(String zookeeperNameSpace) {
    this.zookeeperNameSpace = zookeeperNameSpace;
  }

  /**
   * @param cassandraHost
   *          the cassandraHost to set
   */
  public void setCassandraHost(String cassandraHost) {
    this.cassandraHost = cassandraHost;
  }

  /**
   * @param cassandraUserName
   *          the cassandraUserName to set
   */
  public void setCassandraUserName(String cassandraUserName) {
    this.cassandraUserName = cassandraUserName;
  }

  /**
   * @param cassandraPwd
   *          the cassandraPwd to set
   */
  public void setCassandraPwd(String cassandraPwd) {
    this.cassandraPwd = cassandraPwd;
  }

  /**
   * @param cassandraKeySpace
   *          the cassandraKeySpace to set
   */
  public void setCassandraKeySpace(String cassandraKeySpace) {
    this.cassandraKeySpace = cassandraKeySpace;
  }

  /**
   * @param dfceAddress
   *          the dfceAddress to set
   */
  public void setDfceAddress(URL dfceAddress) {
    this.dfceAddress = dfceAddress;
  }

  /**
   * @param dfceLogin
   *          the dfceLogin to set
   */
  public void setDfceLogin(String dfceLogin) {
    this.dfceLogin = dfceLogin;
  }

  /**
   * @param dfcePwd
   *          the dfcePwd to set
   */
  public void setDfcePwd(String dfcePwd) {
    this.dfcePwd = dfcePwd;
  }

  /**
   * @param dfceBaseName
   *          the dfceBaseName to set
   */
  public void setDfceBaseName(String dfceBaseName) {
    this.dfceBaseName = dfceBaseName;
  }

  /**
   * @param dfceTimeout
   *          the dfceTimeout to set
   */
  public void setDfceTimeout(String dfceTimeout) {
    this.dfceTimeout = dfceTimeout;
  }

  /**
   * @param dfceSecure
   *          the dfceSecure to set
   */
  public void setDfceSecure(String dfceSecure) {
    this.dfceSecure = dfceSecure;
  }

  /**
   * @param parametres
   *          the parametres to set
   */
  public void setParametres(List<String> parametres) {
    this.parametres = parametres;
  }

  /**
   * @return le nom associé à la configuration
   */
   public final String getNom() {
      return nom;
   }

   /**
    * URL des services Web SaeService ex :
    * http://localhost/sae-webservices/services/SaeService
    */
  private URI urlWs;

   /**
   * @param urlWs
   *          the urlWs to set
   */
  public void setUrlWs(URI urlWs) {
    this.urlWs = urlWs;
  }

  /**
   * @return l'URL des services Web SaeService
   */
   public final URI getUrlWs() {
      return urlWs;
   }

   /**
    * DNS ou IP des serveurs zookeper avec le port associé. Plusieurs DNS/IP
    * peuvent être spécifiés. Le format est le suivant :
    * dns1:port,dns2;port,dn3:port
    */
  private String zookeeperHost;

   /**
    * 
    * @return DNS ou IP des serveurs zookeper avec le port associé
    */
   public final String getZookeeperHost() {
      return zookeeperHost;
   }

   /**
    * NameSpace(s) zookeeper
    */
  private String zookeeperNameSpace;

   /**
    * 
    * @return NameSpace(s) zookeeper
    */
   public final String getZookeeperNameSpace() {
      return zookeeperNameSpace;
   }

   /**
    * DNS ou IP des serveurs Cassandra. Plusieurs DNS/IP peuvent être spécifiés.
    * Le format est le suivant : dns1:port,dns2;port,dn3:port
    */
  private String cassandraHost;

   /**
    * 
    * @return DNS ou IP des serveurs Cassandra
    */
  public String getCassandraHost() {
      return cassandraHost;
   }

   /**
    * Utilisateur Cassandra
    */
  private String cassandraUserName;

   /**
    * 
    * @return Utilisateur Cassandra
    */
  public String getCassandraUserName() {
      return cassandraUserName;
   }

   /**
    * Mot de passe Cassandra
    */
  private String cassandraPwd;

   /**
    * 
    * @return Mot de passe Cassandra
    */
   public final String getCassandraPwd() {
      return cassandraPwd;
   }

   /**
    * Nom du KeySpace Cassandra
    */
  private String cassandraKeySpace;

   /**
    * 
    * @return Nom du KeySpace Cassandra
    */
   public final String getCassandraKeySpace() {
      return cassandraKeySpace;
   }

   /**
    * Adresse de connexion à DFCE
    */
  private URL dfceAddress;

   /**
    * @return the dfceAddress
    */
   public final URL getDfceAddress() {
      return dfceAddress;
   }

   /**
    * Login de connexion à DFCE
    */
  private String dfceLogin;

   /**
    * @return the dfceLogin
    */
   public final String getDfceLogin() {
      return dfceLogin;
   }

   /**
    * Mot de passe de connexion à DFCE
    */
  private String dfcePwd;

   /**
    * @return the dfcePwd
    */
   public final String getDfcePwd() {
      return dfcePwd;
   }

   /**
    * Nom de la base DFCE
    */
  private String dfceBaseName;

   /**
    * @return the dfceBaseName
    */
   public final String getDfceBaseName() {
      return dfceBaseName;
   }
   
   /**
    * Timeout DFCE
    */
  private String dfceTimeout;

   /**
    * @return the timeout
    */
   public String getDfceTimeout() {
      return dfceTimeout;
   }
   
   /**
    * Secure mode DFCE
    */
  private String dfceSecure;
   
   /**
    * @return the dfceSecure
    */
   public String getDfceSecure() {
      return dfceSecure;
   }
   
   /**
    * Liste des paramètres pour la recherche dans la pile des travaux
    */
  private List<String> parametres;

   /**
    * @return the listeParametres
    */
  public List<String> getParametres() {
      return parametres;
   }

   /**
    * Constructeur
    * 
    * @param nom
    *           le nom associé à la configuration
    * @param urlWs
    *           l'URL des services Web SaeService
    * @param zookeeperHosts
    *           le DNS ou IP des serveurs zookeper avec le port associé
    * @param zookeeperNameSpace
    *           le nameSpace zookeeper
    * @param cassandraHosts
    *           le DNS ou IP des serveurs Cassandra
    * @param cassandraUserName
    *           l'utilisateur Cassandra
    * @param cassandraPwd
    *           le mot de passe Cassandra
    * @param cassandraKeySpace
    *           le nom du KeySpace Cassandra
    * @param dfceAddress
    *           URL de DFCE
    * @param dfceLogin
    *           Login de connexion à DFCE
    * @param dfcePwd
    *           Mot de passe de connextion à DFCE
    * @param dfceBaseName
    *           Nom de la base
    * @param           
    * @param parametres
    *           Liste des paramètres pour la recherche dans la pile des travaux
    */
   @SuppressWarnings("PMD.ExcessiveParameterList")
   public ConfigurationEnvironnement(String nom, URI urlWs,
         String zookeeperHosts, String zookeeperNameSpace,
         String cassandraHosts, String cassandraUserName, String cassandraPwd,
         String cassandraKeySpace, URL dfceAddress, String dfceLogin,
         String dfcePwd, String dfceBaseName, String dfceTimeout, 
         String dfceSecure, List<String> parametres) {
      this.nom = nom;
      this.urlWs = urlWs;
      this.zookeeperHost = zookeeperHosts;
      this.zookeeperNameSpace = zookeeperNameSpace;
      this.cassandraHost = cassandraHosts;
      this.cassandraUserName = cassandraUserName;
      this.cassandraPwd = cassandraPwd;
      this.cassandraKeySpace = cassandraKeySpace;
      this.dfceAddress = dfceAddress;
      this.dfceLogin = dfceLogin;
      this.dfcePwd = dfcePwd;
      this.dfceBaseName = dfceBaseName;
      this.parametres = parametres;
      this.dfceTimeout = dfceTimeout;
      this.dfceSecure = dfceSecure;
   }

  /**
  * 
  */
  public ConfigurationEnvironnement() {
    super();
  }

}
