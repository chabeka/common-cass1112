package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;


/**
 * Classe de sous-formulaire pour un test du WS SaeService, opération "archivageUnitaire"<br>
 * <br>
 * Un objet de cette classe s'associe au tag "captureUnitaire.tag" (attribut "objetFormulaire")
 */
public class CaptureUnitaireFormulaire {

   private ResultatTest resultats = new ResultatTest();
   
   private String urlEcde;
   
   private MetadonneeValeurList metadonnees = new MetadonneeValeurList();
   
   
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
    * L'URL ECDE du document que l'on veut archiver
    * 
    * @return L'URL ECDE du document que l'on veut archiver
    */
   public final String getUrlEcde() {
      return this.urlEcde;
   }
   
   
   /**
    * L'URL ECDE du document que l'on veut archiver
    * 
    * @param urlEcde L'URL ECDE du document que l'on veut archiver
    */
   public final void setUrlEcde(String urlEcde) {
      this.urlEcde = urlEcde;
   }
   
   
   /**
    * La liste des métadonnées décrivant le document que l'on souhaite archiver
    * 
    * @return La liste des métadonnées décrivant le document que l'on souhaite archiver
    */
   public final MetadonneeValeurList getMetadonnees() {
      return metadonnees;
   }
   
   
   /**
    * La liste des métadonnées décrivant le document que l'on souhaite archiver
    * 
    * @param metadonnees La liste des métadonnées décrivant le document que l'on souhaite archiver
    */
   public final void setMetadonnees(MetadonneeValeurList metadonnees) {
      this.metadonnees = metadonnees;
   }
   
}
