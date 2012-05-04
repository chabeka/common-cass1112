package fr.urssaf.image.commons.exemples.cassandra.dfce.modele;

import java.util.Date;

public class Job {

   private String nom;
   private String typeAttributeName;
   private String key;
   private Date lastSuccessfullRunDate;
   private Date launchDate;
   private Boolean running;
   
   public final String getTypeAttributeName() {
      return typeAttributeName;
   }
   public final void setTypeAttributeName(String typeAttributeName) {
      this.typeAttributeName = typeAttributeName;
   }
   public final String getKey() {
      return key;
   }
   public final void setKey(String key) {
      this.key = key;
   }
   public final Date getLastSuccessfullRunDate() {
      return lastSuccessfullRunDate;
   }
   public final void setLastSuccessfullRunDate(Date lastSuccessfullRunDate) {
      this.lastSuccessfullRunDate = lastSuccessfullRunDate;
   }
   public final Date getLaunchDate() {
      return launchDate;
   }
   public final void setLaunchDate(Date launchDate) {
      this.launchDate = launchDate;
   }
   public final Boolean isRunning() {
      return running;
   }
   public final void setRunning(Boolean running) {
      this.running = running;
   }
   public final String getNom() {
      return nom;
   }
   public final void setNom(String nom) {
      this.nom = nom;
   }
   
}
