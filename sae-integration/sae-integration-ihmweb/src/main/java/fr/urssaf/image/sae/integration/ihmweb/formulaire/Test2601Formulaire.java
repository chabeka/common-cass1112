package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour le test 2601
 */
public class Test2601Formulaire extends TestWsParentFormulaire {


   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(this) ;
   
   
   private final RechercheFormulaire recherche = new RechercheFormulaire(this) ;
   
     
   private String dernierIdArchiv;
   private String dernierSha1;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "capture"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "capture"
    */
   public final CaptureUnitaireFormulaire getCaptureUnitaire() {
      return this.captureUnitaire;
   }
   
     
   /**
    * Le sous-formulaire pour l'appel à l'opération "recherche"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "recherche"
    */
   public final RechercheFormulaire getRecherche() {
      return this.recherche;
   }


   /**
    * Le dernier id d'archivage obtenu en réponse de la capture unitaire
    * 
    * @return Le dernier id d'archivage obtenu en réponse de la capture unitaire
    */
   public final String getDernierIdArchivage() {
      return dernierIdArchiv;
   }

   
   /**
    * Le dernier id d'archivage obtenu en réponse de la capture unitaire
    * 
    * @param dernierIdArchiv Le dernier id d'archivage obtenu en réponse de la capture unitaire
    */
   public final void setDernierIdArchivage(String dernierIdArchiv) {
      this.dernierIdArchiv = dernierIdArchiv;
   }


   /**
    * Le SHA-1 du dernier document envoyé lors de la capture unitaire
    * 
    * @return Le SHA-1 du dernier document envoyé lors de la capture unitaire
    */
   public final String getDernierSha1() {
      return dernierSha1;
   }


   /**
    * Le SHA-1 du dernier document envoyé lors de la capture unitaire
    * 
    * @param dernierSha1 Le SHA-1 du dernier document envoyé lors de la capture unitaire
    */
   public final void setDernierSha1(String dernierSha1) {
      this.dernierSha1 = dernierSha1;
   }
   
   
}
