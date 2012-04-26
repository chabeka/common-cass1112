package fr.urssaf.image.commons.cassandra.support.clock;

/**
 * Configuration spécifique de l'horloge de Cassandra<br>
 * <ul>
 * <li>
 * <code>maxTimeSynchroError</code> : Temps maximum de décalage d'horloge qu'il
 * nous parait acceptable, en micro-secondes</li>
 * <li>
 * <code>maxTimeSynchroWarn</code> : Temps maximum de décalage d'horloge, en
 * micro-secondes. Au-delà on se met en warning</li>
 * </ul>
 * 
 * 
 */
public class JobClockConfiguration {

   private int maxTimeSynchroError;

   private int maxTimeSynchroWarn;

   /**
    * @return seuil maximum d'erreur
    */
   public final int getMaxTimeSynchroError() {
      return maxTimeSynchroError;
   }

   /**
    * @param maxTimeSynchroError
    *           seuil maximum d'erreur
    */
   public final void setMaxTimeSynchroError(int maxTimeSynchroError) {
      this.maxTimeSynchroError = maxTimeSynchroError;
   }

   /**
    * @return seuil maximum d'avertissement
    */
   public final int getMaxTimeSynchroWarn() {
      return maxTimeSynchroWarn;
   }

   /**
    * @param maxTimeSynchroWarn
    *           seuil maximum d'avertissement
    */
   public final void setMaxTimeSynchroWarn(int maxTimeSynchroWarn) {
      this.maxTimeSynchroWarn = maxTimeSynchroWarn;
   }

}
