package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire du cas de test 2799
 */
public class Test2799Formulaire extends TestWsParentFormulaire {

 
   private StockageUnitaireFormulaire stockageUnitaire = new StockageUnitaireFormulaire(
         this);

   private RechercheFormulaire recherche = new RechercheFormulaire(this);
  
   private final ConsultationFormulaire consultation = new ConsultationFormulaire(this);

   public StockageUnitaireFormulaire getStockageUnitaire() {
      return stockageUnitaire;
   }

   public RechercheFormulaire getRecherche() {
      return recherche;
   }
  
   public ConsultationFormulaire getConsultation() {
      return consultation;
   }
   
}
