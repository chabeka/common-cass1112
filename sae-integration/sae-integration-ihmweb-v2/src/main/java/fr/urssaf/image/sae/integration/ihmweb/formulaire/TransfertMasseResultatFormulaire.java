package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

public class TransfertMasseResultatFormulaire {

private ResultatTest resultats = new ResultatTest();
   
   private String urlSommaire;
   
   
   /**
    * Les résultats de l'appel à l'opération
    * 
    * @return Les résultats de l'appel à l'opération
    */
   public final ResultatTest getResultats() {
      return this.resultats;
   }


   /**
    * Les résultats de l'appel à l'opération
    * 
    * @param resultats Les résultats de l'appel à l'opération
    */
   public final void setResultats(ResultatTest resultats) {
      this.resultats = resultats;
   }


   /**
    * L'URL du fichier sommaire.xml
    * 
    * @return L'URL du fichier sommaire.xml
    */
   public final String getUrlSommaire() {
      return urlSommaire;
   }

   
   /**
    * L'URL du fichier sommaire.xml
    * 
    * @param urlSommaire L'URL du fichier sommaire.xml
    */
   public final void setUrlSommaire(String urlSommaire) {
      this.urlSommaire = urlSommaire;
   }
   
}
