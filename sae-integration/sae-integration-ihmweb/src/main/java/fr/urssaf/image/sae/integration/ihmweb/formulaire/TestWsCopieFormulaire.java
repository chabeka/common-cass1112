package fr.urssaf.image.sae.integration.ihmweb.formulaire;

public class TestWsCopieFormulaire extends TestWsParentFormulaire {

   /**
    * Capture unitaire
    */
   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(
         this);

   /**
    * Copie formulaire
    */
   private final CopieFormulaire copie = new CopieFormulaire(this);

   /**
    * Recherche document existant formulaire
    */
   private final RechercheFormulaire rechercheDocExistant = new RechercheFormulaire(
         this);

   /**
    * Recherche document copie formulaire
    */
   private final RechercheFormulaire rechercheDocCopie = new RechercheFormulaire(
         this);

   /**
    * Getter pour copie
    * 
    * @return the copie
    */
   public CopieFormulaire getCopie() {
      return copie;
   }

   /**
    * Getter pour captureUnitaire
    * 
    * @return the captureUnitaire
    */
   public CaptureUnitaireFormulaire getCaptureUnitaire() {
      return captureUnitaire;
   }

   /**
    * Getter pour rechercheDocExistant
    * 
    * @return the rechercheDocExistant
    */
   public RechercheFormulaire getRechercheDocExistant() {
      return rechercheDocExistant;
   }

   /**
    * Getter pour rechercheDocCopie
    * 
    * @return the rechercheDocCopie
    */
   public RechercheFormulaire getRechercheDocCopie() {
      return rechercheDocCopie;
   }

}
