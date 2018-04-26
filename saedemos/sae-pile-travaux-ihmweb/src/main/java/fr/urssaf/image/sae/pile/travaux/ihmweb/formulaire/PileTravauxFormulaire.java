package fr.urssaf.image.sae.pile.travaux.ihmweb.formulaire;

import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.CassandraEtZookeeperConfig;

/**
 * Classe de formulaire pour l'affichage de la pile des travaux
 */
public class PileTravauxFormulaire {

   private CassandraEtZookeeperConfig connexionConfig = new CassandraEtZookeeperConfig();

   public final CassandraEtZookeeperConfig getConnexionConfig() {
      return connexionConfig;
   }

   public final void setConnexionConfig(
         CassandraEtZookeeperConfig connexionConfig) {
      this.connexionConfig = connexionConfig;
   }

}
