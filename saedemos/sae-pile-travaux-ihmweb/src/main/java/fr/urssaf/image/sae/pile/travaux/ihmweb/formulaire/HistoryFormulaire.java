package fr.urssaf.image.sae.pile.travaux.ihmweb.formulaire;

import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.CassandraEtZookeeperConfig;

/**
 * Classe de formulaire pour l'affichage de l'historique d'un job
 */
public class HistoryFormulaire {

   private CassandraEtZookeeperConfig connexionConfig = new CassandraEtZookeeperConfig();
   
   private String idJob;

   public final CassandraEtZookeeperConfig getConnexionConfig() {
      return connexionConfig;
   }

   public final void setConnexionConfig(
         CassandraEtZookeeperConfig connexionConfig) {
      this.connexionConfig = connexionConfig;
   }

   public final String getIdJob() {
      return idJob;
   }

   public final void setIdJob(String idJob) {
      this.idJob = idJob;
   }
   
   
   
   

}
