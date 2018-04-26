package fr.urssaf.image.sae.pile.travaux.model;

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
      return getDateCopy(date);
   }

   /**
    * @param date
    *           the date to set
    */
   public final void setDate(Date date) {
      this.date = getDateCopy(date);
   }

   private Date getDateCopy(Date date) {
      Date tDate = null;
      if (date != null) {
         tDate = new Date(date.getTime());
      }
      return tDate;
   }

}
