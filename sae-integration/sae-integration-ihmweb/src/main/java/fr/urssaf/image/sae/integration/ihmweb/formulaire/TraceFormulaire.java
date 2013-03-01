package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import java.util.Date;

public class TraceFormulaire {

   Date dateDebut;
   
   Date dateFin;
   
   boolean inverse;
   
   String nbTrace;

   public Date getDateDebut() {
      return dateDebut;
   }

   public void setDateDebut(Date dateDebut) {
      this.dateDebut = dateDebut;
   }

   public Date getDateFin() {
      return dateFin;
   }

   public void setDateFin(Date dateFin) {
      this.dateFin = dateFin;
   }

   public boolean isInverse() {
      return inverse;
   }

   public void setInverse(boolean inverse) {
      this.inverse = inverse;
   }

   public String getNbTrace() {
      return nbTrace;
   }

   public void setNbTrace(String nbTrace) {
      this.nbTrace = nbTrace;
   }
   


   
   
   
}
