package fr.urssaf.image.sae.integration.ihmweb.modele;

public class EcdeRepertoire {
   
   private String debutTraitement;
   private String finTraitement;
   private String Sommaire;
   private String Resultat;
   
   public String getDebutTraitement() {
      return debutTraitement;
   }
   public void setDebutTraitement(String debutTraitement) {
      this.debutTraitement = debutTraitement;
   }
   public String getFinTraitement() {
      return finTraitement;
   }
   public void setFinTraitement(String finTraitement) {
      this.finTraitement = finTraitement;
   }
   public String getSommaire() {
      return Sommaire;
   }
   public void setSommaire(String sommaire) {
      Sommaire = sommaire;
   }
   public String getResultat() {
      return Resultat;
   }
   public void setResultat(String resultat) {
      Resultat = resultat;
   }

}
