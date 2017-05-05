package fr.urssaf.image.sae.pile.travaux.ihmweb.modele;

import java.util.Date;

/**
 * Modèle de l'historique d'un traitement.<br>
 * <ul>
 * <li><code>trace</code>: message de la trace</li>
 * <li><code>date</code>: date de la trace</li>
 * </ul>
 * 
 * 
 */
public class JobHistory {

   private String trace;

   private Date date;

   /**
    * @return the trace
    */
   public final String getTrace() {
      return trace;
   }

   /**
    * @param trace
    *           the trace to set
    */
   public final void setTrace(String trace) {
      this.trace = trace;
   }

   /**
    * @return the date
    */
   public final Date getDate() {
      return date;
   }

   /**
    * @param timestamp
    *           the date to set
    */
   public final void setDate(Date date) {
      this.date = date;
   }

}