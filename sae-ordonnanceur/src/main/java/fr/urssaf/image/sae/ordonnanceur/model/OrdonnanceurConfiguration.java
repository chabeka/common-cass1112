package fr.urssaf.image.sae.ordonnanceur.model;

/**
 * Configuration spécifique de l'ordonnanceur.<br>
 * <ul>
 * <li>
 * <code>intervalle</code> : temps minimum d'attente entre deux traitements en
 * secondes</li>
 * </ul>
 * 
 * 
 */
public class OrdonnanceurConfiguration {

   private int intervalle;
   
   private int tpsMaxReservation;
   
   private int tpsMaxTraitement;

   /**
    * 
    * @param intervalle
    *           temps minimum d'attente entre deux traitements en secondes
    */
   public final void setIntervalle(int intervalle) {
      this.intervalle = intervalle;
   }

   /**
    * 
    * @return temps minimum d'attente entre deux traitements en secondes
    */
   public final int getIntervalle() {
      return intervalle;
   }

   /**
    * @return the tpsMaxReservation
    */
   public final int getTpsMaxReservation() {
      return tpsMaxReservation;
   }

   /**
    * @param tpsMaxReservation the tpsMaxReservation to set
    */
   public final void setTpsMaxReservation(int tpsMaxReservation) {
      this.tpsMaxReservation = tpsMaxReservation;
   }

   /**
    * @return the tpsMaxTraitement
    */
   public final int getTpsMaxTraitement() {
      return tpsMaxTraitement;
   }

   /**
    * @param tpsMaxTraitement the tpsMaxTraitement to set
    */
   public final void setTpsMaxTraitement(int tpsMaxTraitement) {
      this.tpsMaxTraitement = tpsMaxTraitement;
   }

}
