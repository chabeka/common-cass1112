package fr.urssaf.image.sae.anais.framework.config;

/**
 * Eléments de configuration pour l'utilisation d'ANAIS
 */
public class SaeAnaisConfig {

   private String hostname;
   private Integer port;
   private boolean usetls;
   private String appdn;
   private String passwd;
   private String codeapp;
   private String codeenv;
   private String timeout;
   private String comptePortail;
   private boolean droitsDirect;

   /**
    * @return DNS ou adresse IP du serveur ANAIS
    */
   public final String getHostname() {
      return hostname;
   }

   /**
    * 
    * @param hostname
    *           DNS ou adresse IP du serveur ANAIS
    */
   public final void setHostname(String hostname) {
      this.hostname = hostname;
   }

   /**
    * 
    * @return Port TCP du serveur ANAIS
    */
   public final Integer getPort() {
      return port;
   }

   /**
    * 
    * @param port
    *           Port TCP du serveur ANAIS
    */
   public final void setPort(Integer port) {
      this.port = port;
   }

   /**
    * 
    * @return Activation de TLS pour la connexion au serveur ANAIS
    */
   public final boolean isUsetls() {
      return usetls;
   }

   /**
    * 
    * @param usetls
    *           Activation de TLS pour la connexion au serveur ANAIS
    */
   public final void setUsetls(boolean usetls) {
      this.usetls = usetls;
   }

   /**
    * 
    * @return DN du compte applicatif
    */
   public final String getAppdn() {
      return appdn;
   }

   /**
    * 
    * @param appdn
    *           DN du compte applicatif
    */
   public final void setAppdn(String appdn) {
      this.appdn = appdn;
   }

   /**
    * 
    * @return Mot de passe du compte applicatif
    */
   public final String getPasswd() {
      return passwd;
   }

   /**
    * 
    * @param passwd
    *           Mot de passe du compte applicatif
    */
   public final void setPasswd(String passwd) {
      this.passwd = passwd;
   }

   /**
    * 
    * @return Code de l'application
    */
   public final String getCodeapp() {
      return codeapp;
   }

   /**
    * 
    * @param codeapp
    *           Code de l'application
    */
   public final void setCodeapp(String codeapp) {
      this.codeapp = codeapp;
   }

   /**
    * 
    * @return Code environnement
    */
   public final String getCodeenv() {
      return codeenv;
   }

   /**
    * 
    * @param codeenv
    *           Code environnement
    */
   public final void setCodeenv(String codeenv) {
      this.codeenv = codeenv;
   }

   /**
    * 
    * @return Timeout de la connexion à ANAIS
    */
   public final String getTimeout() {
      return timeout;
   }

   /**
    * 
    * @param timeout
    *           Timeout de la connexion à ANAIS
    */
   public final void setTimeout(String timeout) {
      this.timeout = timeout;
   }

   /**
    * Si l'API ANAIS est utilisée pour un portail, le nom du portail, sinon
    * null.
    * 
    * @return Si l'API ANAIS est utilisée pour un portail, le nom du portail,
    *         sinon null.
    */
   public final String getComptePortail() {
      return comptePortail;
   }

   /**
    * Si l'API ANAIS est utilisée pour un portail, le nom du portail, sinon
    * null.
    * 
    * @param comptePortail
    *           Si l'API ANAIS est utilisée pour un portail, le nom du portail,
    *           sinon null.
    */
   public final void setComptePortail(String comptePortail) {
      this.comptePortail = comptePortail;
   }

   /**
    * Activation ou pas des droits directs
    * 
    * @return Activation ou pas des droits directs
    */
   public final boolean isDroitsDirect() {
      return droitsDirect;
   }

   /**
    * Activation ou pas des droits directs
    * 
    * @param droitsDirect
    *           Activation ou pas des droits directs
    */
   public final void setDroitsDirect(boolean droitsDirect) {
      this.droitsDirect = droitsDirect;
   }

}
