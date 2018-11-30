package fr.urssaf.image.sae.integration.ihmweb.modele;

/**
 * 
 * Modèle représentant un test de traitement de masse
 */
public class TestMasse {

   // Nom du test
   private String name;

   // Resultat.xml present ou non
   private boolean isResultatPresent;

   // lien ecde vers le sommaire et resultat
   private String lienEcde;

   // test à valider "NON", validation ok "OK", validation ko "KO"
   private String valider;

   public String getName() {
      return name;
   }

   public boolean getIsResultatPresent() {
      return isResultatPresent;
   }

   public String getLienEcde() {
      return lienEcde;
   }

   public String getValider() {
      return valider;
   }

   public void setValider(String valider) {
      this.valider = valider;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setIsResultatPresent(boolean isResultatPresent) {
      this.isResultatPresent = isResultatPresent;
   }

   public void setLienEcde(String lienEcde) {
      this.lienEcde = lienEcde;
   }

}
