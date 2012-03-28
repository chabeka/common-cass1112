package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.sae.integration.ihmweb.modele.cmcompare.ResultatCmCompare;

public class ResultatsCMComparateurFormulaire {

   
   private String repRef;
   private String repPasse;
   private List<ResultatCmCompare> listeFichiers = new ArrayList<ResultatCmCompare>();
   
   
   public final List<ResultatCmCompare> getListeFichiers() {
      return listeFichiers;
   }

   public final void setListeFichiers(List<ResultatCmCompare> listeFichiers) {
      this.listeFichiers = listeFichiers;
   }

   public final String getRepRef() {
      return repRef;
   }

   public final void setRepRef(String repRef) {
      this.repRef = repRef;
   }

   public final String getRepPasse() {
      return repPasse;
   }

   public final void setRepPasse(String repPasse) {
      this.repPasse = repPasse;
   }

}
