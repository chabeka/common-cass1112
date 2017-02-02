package fr.urssaf.image.sae.integration.ihmweb.modele;

/**
 * Résultat d'un appel au service web d'archivage de masse
 */
public class CaptureMasseResultat {

   private boolean appelAvecHashSommaire;

   private String idTraitement;

   public boolean isAppelAvecHashSommaire() {
      return appelAvecHashSommaire;
   }

   public void setAppelAvecHashSommaire(boolean appelAvecHashSommaire) {
      this.appelAvecHashSommaire = appelAvecHashSommaire;
   }

   public String getIdTraitement() {
      return idTraitement;
   }

   public void setIdTraitement(String idTraitement) {
      this.idTraitement = idTraitement;
   }

}
