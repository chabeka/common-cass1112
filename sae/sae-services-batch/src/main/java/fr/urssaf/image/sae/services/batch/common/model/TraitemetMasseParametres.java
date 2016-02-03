/**
 * 
 */
package fr.urssaf.image.sae.services.batch.common.model;

import java.util.Map;
import java.util.UUID;

import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

/**
 * Objet représentant l'ensemble des paramètres de traitement de masse
 * 
 */
public class TraitemetMasseParametres {
   
   @Deprecated
   private String ecdeURL;

   private final UUID uuid;

   private final Integer nbreDocs;

   private final String saeHost;

   private final String clientHost;

   private final VIContenuExtrait viExtrait;
   
   
   /**
    * Type du traitement de masse
    */
   private String type;
   
   /**
    * Paramètre du traitement de masse
    */
   private Map<String, String> jobParameters;

   /**
    * @param ecdeURL
    *           l'url ecde
    * @param uuid
    *           l'identifiant de traitement
    * @param saeHost
    *           le nom de la machine locale
    * @param clientHost
    *           l'adresse de la machine demandant le traitement
    * @param nbreDocs
    *           nombre de documents contenus dans le fichier sommaire.xml
    * @param viExtrait
    *           contenu du VI
    */
   public TraitemetMasseParametres(String ecdeURL, UUID uuid, String saeHost,
         String clientHost, Integer nbreDocs, VIContenuExtrait viExtrait) {
      super();
      this.ecdeURL = ecdeURL;
      this.uuid = uuid;
      this.saeHost = saeHost;
      this.clientHost = clientHost;
      this.nbreDocs = nbreDocs;
      this.viExtrait = viExtrait;
   }
   
   /**
    * @param jobParameters
    *           Les paramètres de la capture de masse
    * @param uuid
    *           l'identifiant de traitement
    * @param saeHost
    *           le nom de la machine locale
    * @param clientHost
    *           l'adresse de la machine demandant le traitement
    * @param nbreDocs
    *           nombre de documents contenus dans le fichier sommaire.xml
    * @param viExtrait
    *           contenu du VI
    */
   public TraitemetMasseParametres(Map<String,String> jobParameters, UUID uuid, String saeHost,
         String clientHost, Integer nbreDocs, VIContenuExtrait viExtrait) {
      super();
      this.jobParameters = jobParameters;
      this.uuid = uuid;
      this.saeHost = saeHost;
      this.clientHost = clientHost;
      this.nbreDocs = nbreDocs;
      this.viExtrait = viExtrait;
   }
   
   /**
    * @param jobParameters
    *           Les paramètres de la capture de masse
    * @param uuid
    *           l'identifiant de traitement
    * @param type
    *           le type de traitement
    * @param saeHost
    *           le nom de la machine locale
    * @param clientHost
    *           l'adresse de la machine demandant le traitement
    * @param nbreDocs
    *           nombre de documents contenus dans le fichier sommaire.xml
    * @param viExtrait
    *           contenu du VI
    */
   public TraitemetMasseParametres(Map<String,String> jobParameters, UUID uuid, String type, String saeHost,
         String clientHost, Integer nbreDocs, VIContenuExtrait viExtrait) {
      super();
      this.jobParameters = jobParameters;
      this.uuid = uuid;
      this.type = type;
      this.saeHost = saeHost;
      this.clientHost = clientHost;
      this.nbreDocs = nbreDocs;
      this.viExtrait = viExtrait;
   }   

   /**
    * @return l'url ECDE
    */
   @Deprecated
   public final String getEcdeURL() {
      return ecdeURL;
   }

   /**
    * @return l'identifiant de traitement
    */
   public final UUID getUuid() {
      return uuid;
   }
   
   /**
    * @return le type du traitement
    */
   public String getType() {
      return type;
   }

   /**
    * @return le nombre de documents du sommaire
    */
   public final Integer getNbreDocs() {
      return nbreDocs;
   }

   /**
    * @return le nom de machine ou l'IP de la machine SAE ayant traité la
    *         demande
    */
   public final String getSaeHost() {
      return saeHost;
   }

   /**
    * @return le nom de la machine ou l'IP de la machine cliente ayant demandé
    *         le traitement
    */
   public final String getClientHost() {
      return clientHost;
   }

   /**
    * @return le contenu du VI
    */
   public final VIContenuExtrait getVi() {
      return viExtrait;
   }

   /**
    * 
    * @return les paramètre de la capture de masse
    */
   public final Map<String, String> getJobParameters() {
      return jobParameters;
   }

}
