package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

public class ModificationMasseFormulaire extends GenericForm {
   
   private ResultatTest resultats = new ResultatTest();

   private String urlSommaire;
   
   private boolean avecHash;
   
   private String hash;
   
   private String typeHash;
   
   private String codeTraitement;
   
   public ModificationMasseFormulaire(TestWsParentFormulaire parent) {
      super(parent);
   }

   public ResultatTest getResultats() {
      return resultats;
   }

   public void setResultats(ResultatTest resultats) {
      this.resultats = resultats;
   }

   public String getUrlSommaire() {
      return urlSommaire;
   }

   public void setUrlSommaire(String urlSommaire) {
      this.urlSommaire = urlSommaire;
   }

   public boolean isAvecHash() {
      return avecHash;
   }

   public void setAvecHash(boolean avecHash) {
      this.avecHash = avecHash;
   }

   public String getHash() {
      return hash;
   }

   public void setHash(String hash) {
      this.hash = hash;
   }

   public String getTypeHash() {
      return typeHash;
   }

   public void setTypeHash(String typeHash) {
      this.typeHash = typeHash;
   }

   public String getCodeTraitement() {
      return codeTraitement;
   }

   public void setCodeTraitement(String codeTraitement) {
      this.codeTraitement = codeTraitement;
   }
   
   

}
