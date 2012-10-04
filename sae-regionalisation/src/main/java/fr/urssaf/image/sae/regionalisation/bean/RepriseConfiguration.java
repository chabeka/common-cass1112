/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.bean;

/**
 * Classe comprenant la configuration de relance du traitement de
 * regionalisation
 * 
 */
public class RepriseConfiguration {

   private final int maxTestCount;

   private final int timeInterval;

   /**
    * Constructeur
    * 
    * @param maxTestCount
    *           nombre maximum d'essais
    * @param timeInterval
    *           intervalle de temps (en sec.) entre chaque essai
    */
   public RepriseConfiguration(int maxTestCount, int timeInterval) {
      this.maxTestCount = maxTestCount;
      this.timeInterval = timeInterval;
   }

   /**
    * @return the maxTestCount
    */
   public final int getMaxTestCount() {
      return maxTestCount;
   }

   /**
    * @return the timeInterval
    */
   public final int getTimeInterval() {
      return timeInterval;
   }

}
