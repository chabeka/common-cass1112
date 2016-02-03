package fr.urssaf.image.sae.services.batch.common.model;

/**
 * Modèle pour le résultat de l'exécution d'un traitement de masse.<br>
 * <ul>
 * <li><code>succes</code>: indique si oui ou non le traitement s'est terminé
 * sur un succès</li>
 * <li><code>exitMessage</code>: indique une information sur la fin du
 * traitement</li>
 * </ul>
 * 
 * 
 */
public class ExitTraitement {

   private boolean succes;

   private String exitMessage;

   /**
    * @return <code>true</code> si le traitement a réussi, <code>false</code>
    *         sinon
    */
   public final boolean isSucces() {
      return succes;
   }

   /**
    * @param succes
    *           <code>true</code> si le traitement a réussi, <code>false</code>
    *           sinon
    */
   public final void setSucces(boolean succes) {
      this.succes = succes;
   }

   /**
    * @return message de sortie en fin de traitement
    */
   public final String getExitMessage() {
      return exitMessage;
   }

   /**
    * @param exitMessage
    *           message de sortie en fin de traitement
    */
   public final void setExitMessage(String exitMessage) {
      this.exitMessage = exitMessage;
   }

}
