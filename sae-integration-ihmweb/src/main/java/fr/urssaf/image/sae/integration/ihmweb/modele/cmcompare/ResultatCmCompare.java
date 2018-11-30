package fr.urssaf.image.sae.integration.ihmweb.modele.cmcompare;

public class ResultatCmCompare {

   private String nomFichier;
   private EnumCmCompare etatReference;
   private EnumCmCompare etatPasse;

   public final String getNomFichier() {
      return nomFichier;
   }

   public final void setNomFichier(String nomFichier) {
      this.nomFichier = nomFichier;
   }

   public final EnumCmCompare getEtatReference() {
      return etatReference;
   }

   public final void setEtatReference(EnumCmCompare etatReference) {
      this.etatReference = etatReference;
   }

   public final EnumCmCompare getEtatPasse() {
      return etatPasse;
   }

   public final void setEtatPasse(EnumCmCompare etatPasse) {
      this.etatPasse = etatPasse;
   }

}
