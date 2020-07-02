package fr.urssaf.image.parser_opencsv.application.environment;

/**
 * Classe représentant les propriétés d'un environnement cible des tests
 */
public class Environment {

   private final String url;

   private final String ecdeName;

   private final String appliServer;

   private final String ecdeMountPoint;

   public Environment(final String url, final String ecdeName, final String appliServer, final String ecdeMountPoint,
                      final String cassandraServers) {
      this.url = url;
      this.ecdeName = ecdeName;
      this.appliServer = appliServer;
      this.ecdeMountPoint = ecdeMountPoint;
   }

   /**
    * @return L'url du webservice permettant de lancer une capture de masse
    */
   public String getUrl() {
      return url;
   }

   /**
    * @return Le nom de l'ECDE, à passer en préfixe de l'url ECDE
    */
   public String getEcdeName() {
      return ecdeName;
   }

   /**
    * @return Le nom du serveur d'application sur lequel sera lancé les traitements de masse
    */
   public String getAppliServer() {
      return appliServer;
   }

   /**
    * @return Le point de montage de l'ECDE sur le serveur d'application
    */
   public String getEcdeMountPoint() {
      return ecdeMountPoint;
   }

}

