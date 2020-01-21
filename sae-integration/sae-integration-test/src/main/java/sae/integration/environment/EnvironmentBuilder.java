package sae.integration.environment;

/**
 * Classe utilitaire facilitant l'instanciation d'un Environment
 */
public class EnvironmentBuilder {

   private String url;

   private String envCode;

   private String ecdeName;

   private String appliServer;

   private String ecdeMountPoint;

   private String cassandraServers;

   public Environment build() {
      return new Environment(url, envCode, ecdeName, appliServer, ecdeMountPoint, cassandraServers);
   }

   /**
    * @return the url
    */
   public String getUrl() {
      return url;
   }

   /**
    * @param lib
    *           libellé de l'environnement
    */
   public EnvironmentBuilder setUrl(final String url) {
      this.url = url;
      return this;
   }

   /**
    * @return Le code de l'environnement (dans le référentiel ged_batch_docs_executable)
    */
   public String getEnvCode() {
      return envCode;
   }

   /**
    * @param code
    *           Le code de l'environnement (dans le référentiel ged_batch_docs_executable)
    */
   public EnvironmentBuilder setEnvCode(final String code) {
      envCode = code;
      return this;
   }

   /**
    * @return the ecdeName
    */
   public String getEcdeName() {
      return ecdeName;
   }

   /**
    * @param ecdeName
    *           the ecdeName to set
    */
   public EnvironmentBuilder setEcdeName(final String ecdeName) {
      this.ecdeName = ecdeName;
      return this;
   }

   /**
    * @return the appliServer
    */
   public String getAppliServer() {
      return appliServer;
   }

   /**
    * @param appliServer
    *           the appliServer to set
    */
   public EnvironmentBuilder setAppliServer(final String appliServer) {
      this.appliServer = appliServer;
      return this;
   }

   /**
    * @return the ecdeMountPoint
    */
   public String getEcdeMountPoint() {
      return ecdeMountPoint;
   }

   /**
    * @param ecdeMountPoint
    *           the ecdeMountPoint to set
    */
   public EnvironmentBuilder setEcdeMountPoint(final String ecdeMountPoint) {
      this.ecdeMountPoint = ecdeMountPoint;
      return this;
   }

   /**
    * @return the cassandraServers
    */
   public String getCassandraServers() {
      return cassandraServers;
   }

   /**
    * @param cassandraServers
    *           the cassandraServers to set
    */
   public EnvironmentBuilder setCassandraServers(final String cassandraServers) {
      this.cassandraServers = cassandraServers;
      return this;
   }

}
