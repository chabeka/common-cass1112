/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag.model;

import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

/**
 * Modèle objet pour l'écriture du flag d'un traitement de capture de masse
 * 
 */
public class DebutTraitementFlag {

   /**
    * identifiant du traitement de capture de masse
    */
   private UUID idTraitement;

   /**
    * Date de début du traitement de capture de masse
    */
   private Date startDate;

   /**
    * Infos sur le serveur où à lieu le traitement de capture de masse
    */
   private InetAddress hostInfo;

   /**
    * @return identifiant du traitement de capture de masse
    */
   public final UUID getIdTraitement() {
      return idTraitement;
   }

   /**
    * @param idTraitement
    *           identifiant du traitement de capture de masse
    */
   public final void setIdTraitement(UUID idTraitement) {
      this.idTraitement = idTraitement;
   }

   /**
    * @return Date de début du traitement de capture de masse
    */
   public final Date getStartDate() {
      return startDate;
   }

   /**
    * @param startDate
    *           Date de début du traitement de capture de masse
    */
   public final void setStartDate(Date startDate) {
      this.startDate = startDate;
   }

   /**
    * @return Infos sur le serveur où à lieu le traitement de capture de masse
    */
   public final InetAddress getHostInfo() {
      return hostInfo;
   }

   /**
    * @param hostInfo
    *           Infos sur le serveur où à lieu le traitement de capture de masse
    */
   public final void setHostInfo(InetAddress hostInfo) {
      this.hostInfo = hostInfo;
   }

}
