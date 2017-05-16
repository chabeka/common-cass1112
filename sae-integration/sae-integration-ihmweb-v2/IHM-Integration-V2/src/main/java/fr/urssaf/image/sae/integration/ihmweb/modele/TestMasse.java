package fr.urssaf.image.sae.integration.ihmweb.modele;

public class TestMasse {
   
   private String name;
   private boolean isResultatPresent;
   private String lienEcde;
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
