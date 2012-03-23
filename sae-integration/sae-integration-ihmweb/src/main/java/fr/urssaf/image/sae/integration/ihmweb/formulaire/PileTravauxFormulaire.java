package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.sae.integration.ihmweb.modele.piletravaux.JobRequest;

/**
 * Classe de formulaire pour l'affichage de la pile des travaux
 */
public class PileTravauxFormulaire {

   private String serveursZookeeper;
   private String serveursCassandra;
   
   private List<JobRequest> travaux = new ArrayList<JobRequest>();

   public final String getServeursZookeeper() {
      return serveursZookeeper;
   }

   public final void setServeursZookeeper(String serveursZookeeper) {
      this.serveursZookeeper = serveursZookeeper;
   }

   public final String getServeursCassandra() {
      return serveursCassandra;
   }

   public final void setServeursCassandra(String serveursCassandra) {
      this.serveursCassandra = serveursCassandra;
   }

   public final List<JobRequest> getTravaux() {
      return travaux;
   }

   public final void setTravaux(List<JobRequest> travaux) {
      this.travaux = travaux;
   }

}
