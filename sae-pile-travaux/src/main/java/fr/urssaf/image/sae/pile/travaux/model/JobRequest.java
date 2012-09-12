package fr.urssaf.image.sae.pile.travaux.model;

import java.util.Date;
import java.util.UUID;

import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

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
 * <li><code>toCheckFlag</code>: flag pour indiquer que le traitement est à
 * vérifier</li>
 * <li><code>toCheckFlagRaison</code>: raison pour laquelle le traitement est à
 * vérifier</li>
 * <li><code>clientHost</code>: host du client qui demande le traitement de
 * masse</li>
 * <li><code>saeHost</code>: host du serveur de l'ordonnanceur qui exécute le
 * traitement de masse</li>
 * <li><code>docCount</code>: nombre de documents à traiter pour une capture en
 * masse uniquement</li>
 * <li><code>pid</code>: PID du processus qui exécute le traitement de masse</li>
 * </ul>
 * 
 * 
 * 
 */
public class JobRequest {

   private UUID idJob;

   private String type;

   private String parameters;

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

   private Boolean toCheckFlag;

   private String toCheckFlagRaison;

   private VIContenuExtrait vi;

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

   /**
    * @return the toCheckFlag
    */
   public final Boolean getToCheckFlag() {
      return toCheckFlag;
   }

   /**
    * @param toCheckFlag
    *           the toCheckFlag to set
    */
   public final void setToCheckFlag(Boolean toCheckFlag) {
      this.toCheckFlag = toCheckFlag;
   }

   /**
    * @return the toCheckFlagRaison
    */
   public final String getToCheckFlagRaison() {
      return toCheckFlagRaison;
   }

   /**
    * @param toCheckFlagRaison
    *           the toCheckFlagRaison to set
    */
   public final void setToCheckFlagRaison(String toCheckFlagRaison) {
      this.toCheckFlagRaison = toCheckFlagRaison;
   }

   /**
    * @return le contenu du VI
    */
   public final VIContenuExtrait getVi() {
      return vi;
   }

   /**
    * @param vi
    *           le contenu du VI
    */
   public final void setVi(VIContenuExtrait vi) {
      this.vi = vi;
   }

}
