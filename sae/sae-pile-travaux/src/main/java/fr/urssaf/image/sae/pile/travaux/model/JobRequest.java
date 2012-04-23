package fr.urssaf.image.sae.pile.travaux.model;

import java.util.Date;

/**
 * Traitement dans la pile des travaux. Les propriétés sont.
 * <ul>
 * <li><code>idJob</code>: identifiant unique du traitement</li>
 * <li><code>type</code>: type de traitement</li>
 * <li><code>parameters</code>: paramètres du traitement</li>
 * <li><code>state</code>: état du traitement</li>
 * <li><code>reservedBy</code>: hostname du serveur ayant réservé la demande</li>
 * <li><code>creationDate</code>: date/heure d'arrivée de la demande</li>
 * <li><code>reservationDate</code>: date/heure de réservation</li>
 * <li><code>startingDate</code>: date/heure de début de traitement</li>
 * <li><code>endingDate</code>: date/heure de fin de traitement</li>
 * <li><code>message</code>: message de compte-rendu du traitement. Exemple :
 * message d'erreur</li>
 * </ul>
 * 
 * 
 * 
 */
public class JobRequest extends SimpleJobRequest {

   private JobState state;

   private String reservedBy;

   private Date creationDate;

   private Date reservationDate;

   private Date startingDate;

   private Date endingDate;

   private String message;

   private String saeHost;

   private String clientHost;

   private Integer docCount;

   private Integer pid;

   /**
    * Constructeur qui instancie un jobRequest vide
    */
   public JobRequest() {
      super();
   }

   /**
    * Construit un JobRequest à partir d'un SimpleJobRequest
    * 
    * @param simpleJobRequest
    *           Le SimpleJobRequest
    */
   public JobRequest(SimpleJobRequest simpleJobRequest) {
      super();
      setIdJob(simpleJobRequest.getIdJob());
      setType(simpleJobRequest.getType());
      setParameters(simpleJobRequest.getParameters());
   }

   /**
    * @return the state of the jobRequest
    */
   public final JobState getState() {
      return state;
   }

   /**
    * @param state
    *           the state to set
    */
   public final void setState(JobState state) {
      this.state = state;
   }

   /**
    * @return the reservedBy
    */
   public final String getReservedBy() {
      return reservedBy;
   }

   /**
    * @param reservedBy
    *           the reservedBy to set
    */
   public final void setReservedBy(String reservedBy) {
      this.reservedBy = reservedBy;
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
    * @return the reservationDate
    */
   public final Date getReservationDate() {
      // On ne renvoie pas la date directement, car c'est un objet mutable
      return reservationDate == null ? null : new Date(reservationDate
            .getTime());
   }

   /**
    * @param reservationDate
    *           the reservationDate to set
    */
   public final void setReservationDate(Date reservationDate) {
      this.reservationDate = reservationDate == null ? null : new Date(
            reservationDate.getTime());
   }

   /**
    * @return the startingDate
    */
   public final Date getStartingDate() {
      // On ne renvoie pas la date directement, car c'est un objet mutable
      return startingDate == null ? null : new Date(startingDate.getTime());
   }

   /**
    * @param startingDate
    *           the startingDate to set
    */
   public final void setStartingDate(Date startingDate) {
      this.startingDate = startingDate == null ? null : new Date(startingDate
            .getTime());
   }

   /**
    * @return the endingDate
    */
   public final Date getEndingDate() {
      // On ne renvoie pas la date directement, car c'est un objet mutable
      return endingDate == null ? null : new Date(endingDate.getTime());
   }

   /**
    * @param endingDate
    *           the endingDate to set
    */
   public final void setEndingDate(Date endingDate) {
      this.endingDate = endingDate == null ? null : new Date(endingDate
            .getTime());
   }

   /**
    * Renvoie un SimpleJob contenant les propriétés de base du jobRequest
    * 
    * @return Un SimpleJob
    */
   public final SimpleJobRequest getSimpleJob() {
      SimpleJobRequest simpleJobRequest = new SimpleJobRequest();
      simpleJobRequest.setIdJob(getIdJob());
      simpleJobRequest.setType(getType());
      simpleJobRequest.setParameters(getParameters());
      return simpleJobRequest;
   }

   /**
    * @param message
    *           : message de compte-rendu du traitement
    */
   public final void setMessage(String message) {
      this.message = message;
   }

   /**
    * 
    * @return message de compte-rendu du traitement
    */
   public final String getMessage() {
      return message;
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

   /**
    * @return le process ID du traitement
    */
   public final Integer getPid() {
      return pid;
   }

   /**
    * @param pid
    *           le process ID du traitement
    */
   public final void setPid(Integer pid) {
      this.pid = pid;
   }

}
