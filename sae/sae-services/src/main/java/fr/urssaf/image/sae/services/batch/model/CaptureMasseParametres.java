/**
 * 
 */
package fr.urssaf.image.sae.services.batch.model;

import java.util.UUID;

import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

/**
 * Objet représentant l'ensemble des paramètres de la capture en masse
 * 
 */
public class CaptureMasseParametres {

   private String ecdeURL;

   private UUID uuid;

   private Integer nbreDocs;

   private String saeHost;

   private String clientHost;

   private VIContenuExtrait vi;

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
    * @param vi
    *           contenu du VI
    */
   public CaptureMasseParametres(String ecdeURL, UUID uuid, String saeHost,
         String clientHost, Integer nbreDocs, VIContenuExtrait vi) {
      super();
      this.ecdeURL = ecdeURL;
      this.uuid = uuid;
      this.saeHost = saeHost;
      this.clientHost = clientHost;
      this.nbreDocs = nbreDocs;
      this.vi = vi;
   }

   /**
    * @return l'url ECDE
    */
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
      return vi;
   }

}
