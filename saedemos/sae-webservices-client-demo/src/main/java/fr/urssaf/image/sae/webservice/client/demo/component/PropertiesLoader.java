package fr.urssaf.image.sae.webservice.client.demo.component;

import java.util.Properties;

import fr.urssaf.image.sae.webservice.client.demo.util.PropertiesUtils;
import fr.urssaf.image.sae.webservice.client.demo.util.ResourceUtils;

/**
 * Configuration d'un serveur de web services par défaut<br>
 * <br>
 * La configuration du serveur se trouve dans
 * sae-webservices-client-demo.properties<br>
 * paramètres:
 * <ul>
 * <li><code>server.url</code>: url du serveur</li>
 * </ul>
  * Cette classe est un singleton<br>
 * l'unique instance est accessible avec la méthode {@link #getInstance()}
 */
public final class PropertiesLoader {

   private static final String PARAM_PROPERTIES = "sae-webservices-client-demo.properties";

   private static final String EDCE_URL = "ecde.url";

   private static final String TYPE_HASH = "type.hash";

   private static final String HASH = "hash";

   private static final String TIME_DELAY_RELAUNCH_SERVICE = "time.delay.relaunch.service";

   private static final String NB_RELAUNCH_SERVICE = "nb.relaunch.service";

   private static final String CODE_TRAITEMENT = "code.traitement";

   private static final String NB_APPEL_CODE_TRAITEMENT = "nb.appel.code.traitement";

   private static final String SERVER_CASSANDRA = "server.cassandra";

   private static final String TIME_DELAY_SUPERVISION = "time.delay.supervision";

   private String urlEcdeSommaire;

   private String typeHash;

   private String hash;

   private long timeDelayRelaunchService;

   private int nbRelaunchService;

   private String codeTraitement;

   private int nbAppelCodeTraitement;

   private String urlServeurCassandra;

   private long timeDelaySupervision;

   /**
    * Constructeur
    */
   private PropertiesLoader() {

      this(PARAM_PROPERTIES);

   }

   protected PropertiesLoader(String paramProperties) {

      Properties properties = PropertiesUtils.load(ResourceUtils.loadResource(
            this, paramProperties));

      if (properties.containsKey(EDCE_URL)) {
         this.urlEcdeSommaire = properties.getProperty(EDCE_URL);
      }

      if (properties.containsKey(HASH)) {
         this.hash = properties.getProperty(HASH);
      }

      if (properties.containsKey(TYPE_HASH)) {
         this.typeHash = properties.getProperty(TYPE_HASH);
      }

      if (properties.containsKey(TIME_DELAY_RELAUNCH_SERVICE)) {
         try {
            this.timeDelayRelaunchService = Long.parseLong(properties
                  .getProperty(TIME_DELAY_RELAUNCH_SERVICE));
         } catch (Exception e) {
            this.timeDelayRelaunchService = 0L;
         }
      }

      if (properties.containsKey(NB_RELAUNCH_SERVICE)) {
         try {
            this.nbRelaunchService = Integer.parseInt(properties
               .getProperty(NB_RELAUNCH_SERVICE));
         } catch (Exception e) {
            this.nbRelaunchService = -1;
         }
      }

      if (properties.containsKey(CODE_TRAITEMENT)) {
         this.codeTraitement = properties.getProperty(CODE_TRAITEMENT);
      }

      if (properties.containsKey(NB_APPEL_CODE_TRAITEMENT)) {
         this.nbAppelCodeTraitement = Integer.parseInt(properties
               .getProperty(NB_APPEL_CODE_TRAITEMENT));
      }

      if (properties.containsKey(SERVER_CASSANDRA)) {
         this.urlServeurCassandra = properties.getProperty(SERVER_CASSANDRA);
      }

      if (properties.containsKey(TIME_DELAY_SUPERVISION)) {
         try {
            this.timeDelaySupervision = Long.parseLong(properties
                  .getProperty(TIME_DELAY_SUPERVISION));
         } catch (Exception e) {
            this.timeDelaySupervision = 0L;
         }
      }
   }

   private static PropertiesLoader server = new PropertiesLoader();

   /**
    * 
    * @return instance du serveur de web service
    */
   public static PropertiesLoader getInstance() {

      return server;
   }

   /**
    * @return the urlEcdeSommaire
    */
   public String getUrlEcdeSommaire() {
      return urlEcdeSommaire;
   }

   /**
    * @param urlEcdeSommaire
    *           the urlEcdeSommaire to set
    */
   public void setUrlEcdeSommaire(String urlEcdeSommaire) {
      this.urlEcdeSommaire = urlEcdeSommaire;
   }

   /**
    * @return the typeHash
    */
   public String getTypeHash() {
      return typeHash;
   }

   /**
    * @param typeHash
    *           the typeHash to set
    */
   public void setTypeHash(String typeHash) {
      this.typeHash = typeHash;
   }

   /**
    * @return the hash
    */
   public String getHash() {
      return hash;
   }

   /**
    * @param hash
    *           the hash to set
    */
   public void setHash(String hash) {
      this.hash = hash;
   }

   /**
    * @return the timeDelayRelaunchService
    */
   public long getTimeDelayRelaunchService() {
      return timeDelayRelaunchService;
   }

   /**
    * @return the nbRelaunchService
    */
   public int getNbRelaunchService() {
      return nbRelaunchService;
   }

   /**
    * @return the codeTraitement
    */
   public String getCodeTraitement() {
      return codeTraitement;
   }

   /**
    * @return the nbAppelCodeTraitement
    */
   public int getNbAppelCodeTraitement() {
      return nbAppelCodeTraitement;
   }

   /**
    * @return the urlServeurCassandra
    */
   public String getUrlServeurCassandra() {
      return urlServeurCassandra;
   }

   /**
    * @return the timeDelaySupervision
    */
   public long getTimeDelaySupervision() {
      return timeDelaySupervision;
   }

}
