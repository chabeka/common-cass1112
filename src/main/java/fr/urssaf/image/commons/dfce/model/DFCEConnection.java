package fr.urssaf.image.commons.dfce.model;

import java.net.URL;

/**
 * Objet contenant l'ensemble du paramétrage concernant DFCE
 */
public final class DFCEConnection {

   private String login;
   private String password;
   private String baseName;
   private boolean checkHash;
   private String digestAlgo;

   private String contextRoot;
   private boolean secure;
   private URL serverUrl;
   private int timeout;
   private String urlToolkit;
   private String hostName;
   private int hostPort;

   /**
    * @return le login de connexion à DFCE
    */
   public String getLogin() {
      return login;
   }

   /**
    * @param login
    *           le login de connexion à DFCE
    */
   public void setLogin(String login) {
      this.login = login;
   }

   /**
    * @return le mot de passe de connexion à DFCE
    */
   public String getPassword() {
      return password;
   }

   /**
    * @param password
    *           le mot de passe de connexion à DFCE
    */
   public void setPassword(String password) {
      this.password = password;
   }

   /**
    * @return le nom de la base DFCE
    */
   public String getBaseName() {
      return baseName;
   }

   /**
    * @param baseName
    *           le nom de la base DFCE
    */
   public void setBaseName(String baseName) {
      this.baseName = baseName;
   }

   /**
    * @return l'indicateur de vérification du Hash
    */
   public boolean isCheckHash() {
      return checkHash;
   }

   /**
    * @param checkHash
    *           l'indicateur de vérification du Hash
    */
   public void setCheckHash(boolean checkHash) {
      this.checkHash = checkHash;
   }

   /**
    * @return l'algo de vérification du hash
    */
   public String getDigestAlgo() {
      return digestAlgo;
   }

   /**
    * @param digestAlgo
    *           l'algo de vérification du hash
    */
   public void setDigestAlgo(String digestAlgo) {
      this.digestAlgo = digestAlgo;
   }

   /**
    * @return le context racine
    */
   public String getContextRoot() {
      return contextRoot;
   }

   /**
    * @param contextRoot
    *           le context racine
    */
   public void setContextRoot(String contextRoot) {
      this.contextRoot = contextRoot;
   }

   /**
    * @return l'indicateur de sécurisation de la connexion
    */
   public boolean getSecure() {
      return secure;
   }

   /**
    * @param secure
    *           l'indicateur de sécurisation de la connexion
    */
   public void setSecure(boolean secure) {
      this.secure = secure;
   }

   /**
    * @return l'URL de DFCE
    */
   public URL getServerUrl() {
      return serverUrl;
   }

   /**
    * @param serverUrl
    *           l'URL de DFCE
    */
   public void setServerUrl(URL serverUrl) {
      this.serverUrl = serverUrl;
   }

   /**
    * @return le timeout de la connexion
    */
   public int getTimeout() {
      return timeout;
   }

   /**
    * @param timeout
    *           le timeout de la connexion
    */
   public void setTimeout(int timeout) {
      this.timeout = timeout;
   }

   /**
    * @return l'URL du toolkit DFCE
    */
   public String getUrlToolkit() {
      return urlToolkit;
   }

   /**
    * @param urlToolkit
    *           l'URL du toolkit DFCE
    */
   public void setUrlToolkit(String urlToolkit) {
      this.urlToolkit = urlToolkit;
   }

   /**
    * @return le nom de la machine hote
    */
   public String getHostName() {
      return hostName;
   }

   /**
    * @param hostName
    *           le nom de la machine hote
    */
   public void setHostName(String hostName) {
      this.hostName = hostName;
   }

   /**
    * @return le port de la machine hote
    */
   public int getHostPort() {
      return hostPort;
   }

   /**
    * @param hostPort
    *           le port de la machine hote
    */
   public void setHostPort(int hostPort) {
      this.hostPort = hostPort;
   }

}
