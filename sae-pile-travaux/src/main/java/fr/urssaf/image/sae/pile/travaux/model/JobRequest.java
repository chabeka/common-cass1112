package fr.urssaf.image.sae.pile.travaux.model;

import java.util.Date;
import java.util.UUID;

/**
 * Traitement dans la pile des travaux. Les propriétés sont.
 * <ul>
 * <li><code>idJob</code>: identifiant unique du traitement</li>
 * <li><code>type</code>: type de traitement</li>
 * <li><code>parameters</code>: paramètres du traitement</li>
 * <li><code>etat</code>: état du traitement</li>
 * <li><code>reservedBy</code>: hostname du serveur ayant réservé la demande</li>
 * <li><code>creationDate</code>: date/heure d'arrivée de la demande</li>
 * <li><code>reservationDate</code>: date/heure de réservation</li>
 * <li><code>startingDate</code>: date/heure de début de traitement</li>
 * <li><code>endingDate</code>: date/heure de fin de traitement</li>
 * </ul>
 * 
 * 
 * 
 */
public class JobRequest {

   private UUID idJob;

   private String type;

   private String parameters;

   private JobState etat;

   private String reservedBy;

   private Date creationDate;

   private Date reservationDate;

   private Date startingDate;

   private Date endingDate;

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
    * @return the etat
    */
   public final JobState getEtat() {
      return etat;
   }

   /**
    * @param etat
    *           the etat to set
    */
   public final void setEtat(JobState etat) {
      this.etat = etat;
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
      return creationDate;
   }

   /**
    * @param creationDate
    *           the creationDate to set
    */
   public final void setCreationDate(Date creationDate) {
      this.creationDate = creationDate;
   }

   /**
    * @return the reservationDate
    */
   public final Date getReservationDate() {
      return reservationDate;
   }

   /**
    * @param reservationDate
    *           the reservationDate to set
    */
   public final void setReservationDate(Date reservationDate) {
      this.reservationDate = reservationDate;
   }

   /**
    * @return the startingDate
    */
   public final Date getStartingDate() {
      return startingDate;
   }

   /**
    * @param startingDate
    *           the startingDate to set
    */
   public final void setStartingDate(Date startingDate) {
      this.startingDate = startingDate;
   }

   /**
    * @return the endingDate
    */
   public final Date getEndingDate() {
      return endingDate;
   }

   /**
    * @param endingDate
    *           the endingDate to set
    */
   public final void setEndingDate(Date endingDate) {
      this.endingDate = endingDate;
   }
}
