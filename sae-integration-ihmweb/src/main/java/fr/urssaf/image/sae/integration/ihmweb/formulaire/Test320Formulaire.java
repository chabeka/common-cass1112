package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire du cas de test 320
 */
public class Test320Formulaire extends TestWsParentFormulaire {

   private CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(
         this);

   private RechercheFormulaire recherche1 = new RechercheFormulaire(this);

   private RechercheFormulaire recherche2 = new RechercheFormulaire(this);

   public CaptureUnitaireFormulaire getCaptureUnitaire() {
      return captureUnitaire;
   }

   public RechercheFormulaire getRecherche1() {
      return recherche1;
   }

   public RechercheFormulaire getRecherche2() {
      return recherche2;
   }

}
