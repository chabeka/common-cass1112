package fr.urssaf.image.sae.pile.travaux.model;

import java.util.Date;
import java.util.UUID;

/**
 * Un nouveau travail à ajouter à la pile des travaux
 */
public class JobToCreate {

   private UUID idJob;

   private String type;

   private String parameters;
   
   private Date creationDate;

   private String saeHost;

   private String clientHost;

   private Integer docCount;

   /**
    * @return the idJob
    */
   public final UUID getIdJob() {
      return idJob;
   }

   /**
    * @param idJob
    *           the idJob to set
    */
   public final void setIdJob(UUID idJob) {
      this.idJob = idJob;
   }

   /**
    * @return the type
    */
   public final String getType() {
      return type;
   }

   /**
    * @param type
    *           the type to set
    */
   public final void setType(String type) {
      this.type = type;
   }

   /**
    * @return the parameters
    */
   public final String getParameters() {
      return parameters;
   }

   /**
    * @param parameters
    *           the parameters to set
    */
   public final void setParameters(String parameters) {
      this.parameters = parameters;
   }
   
   /**
    * @return the creationDate
    */
   public final Date getCreationDate() {
      // On ne renvoie pas la date directement, car c'est un objet mutable
      return creationDate == null ? null : new Date(creationDate.getTime());
   }

   /**
    * @param creationDate
    *           the creationDate to set
    */
   public final void setCreationDate(Date creationDate) {
      this.creationDate = creationDate == null ? null : new Date(creationDate
            .getTime());
   }

   /**
    * @return le nom de machine ou l'IP de la machine SAE ayant traité la
    *         demande
    */
   public final String getSaeHost() {
      return saeHost;
   }

   /**
    * @param saeHost
    *           le nom de machine ou l'IP de la machine SAE ayant traité la
    *           demande
    * 
    */
   public final void setSaeHost(String saeHost) {
      this.saeHost = saeHost;
   }

   /**
    * @return le nom de machine ou l'IP de la machine cliente ayant traité la
    *         demande
    */
   public final String getClientHost() {
      return clientHost;
   }

   /**
    * @param clientHost
    *           le nom de machine ou l'IP de la machine cliente ayant traité la
    *           demande
    */
   public final void setClientHost(String clientHost) {
      this.clientHost = clientHost;
   }

   /**
    * @return le nombre de documents présents dans le fichier sommaire
    */
   public final Integer getDocCount() {
      return docCount;
   }

   /**
    * @param docCount
    *           le nombre de documents présents dans le fichier sommaire
    */
   public final void setDocCount(Integer docCount) {
      this.docCount = docCount;
   }

}
