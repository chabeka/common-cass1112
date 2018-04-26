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

   /**
    * Etat du traitement
    */
   private boolean succes;

   /**
    * Message de sortie du traitement
    */
   private String exitMessage;

   /**
    * Nombre de document traitement
    */
   private int nbDocumentTraite;

   /**
    * Getter succes
    * 
    * @return <code>true</code> si le traitement a réussi, <code>false</code>
    *         sinon
    */
   public final boolean isSucces() {
      return succes;
   }

   /**
    * Setter succes
    * 
    * @param succes
    *           <code>true</code> si le traitement a réussi, <code>false</code>
    *           sinon
    */
   public final void setSucces(boolean succes) {
      this.succes = succes;
   }

   /**
    * Getter exit message
    * 
    * @return message de sortie en fin de traitement
    */
   public final String getExitMessage() {
      return exitMessage;
   }

   /**
    * Setter exit message
    * 
    * @param exitMessage
    *           message de sortie en fin de traitement
    */
   public final void setExitMessage(String exitMessage) {
      this.exitMessage = exitMessage;
   }

   /**
    * Getter pour nbDocumentTraite
    * 
    * @return the nbDocumentTraite
    */
   public int getNbDocumentTraite() {
      return nbDocumentTraite;
   }

   /**
    * Setter pour nbDocumentTraite
    * 
    * @param nbDocumentTraite
    *           the nbDocumentTraite to set
    */
   public void setNbDocumentTraite(int nbDocumentTraite) {
      this.nbDocumentTraite = nbDocumentTraite;
   }

}
