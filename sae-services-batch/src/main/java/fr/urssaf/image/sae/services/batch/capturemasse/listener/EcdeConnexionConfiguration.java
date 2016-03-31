/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.listener;

/**
 * Classe de configuration pour les essais de connexion à l'ECDE en cas de
 * coupure NFS
 * 
 */
public class EcdeConnexionConfiguration {

   private int delaiAttenteMs;

   private int nbreEssaiMax;

   /**
    * @return le délai d'attente entre deux essais de connexion
    */
   public final int getDelaiAttenteMs() {
      return delaiAttenteMs;
   }

   /**
    * @param delaiAttenteMs
    *           le délai d'attente entre deux essais de connexion
    */
   public final void setDelaiAttenteMs(int delaiAttenteMs) {
      this.delaiAttenteMs = delaiAttenteMs;
   }

   /**
    * @return le nombre max d'essais de connexion
    */
   public final int getNbreEssaiMax() {
      return nbreEssaiMax;
   }

   /**
    * @param nbreEssaiMax
    *           le nombre max d'essais de connexion
    */
   public final void setNbreEssaiMax(int nbreEssaiMax) {
      this.nbreEssaiMax = nbreEssaiMax;
   }

}
