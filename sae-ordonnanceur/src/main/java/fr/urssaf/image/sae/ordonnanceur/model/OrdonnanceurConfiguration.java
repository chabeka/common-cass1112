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

}
