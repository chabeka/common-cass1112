package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "transfert" du WS SaeService
 */
public class TestWsTransfertFormulaire extends TestWsParentFormulaire {

   private final TransfertFormulaire transfert = new TransfertFormulaire(
         this);
   
   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(
         this);

   private final RechercheFormulaire recherche = new RechercheFormulaire(this);
   
   private final RechercheFormulaire rechercheGns = new RechercheFormulaire(this);

   public CaptureUnitaireFormulaire getCaptureUnitaire() {
      return captureUnitaire;
   }

   public RechercheFormulaire getRecherche() {
      return recherche;
   }

   public RechercheFormulaire getRechercheGns() {
      return rechercheGns;
   }

   /**
    * Le sous-formulaire pour l'appel à l'opération "transfert"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "transfert"
    */
   public final TransfertFormulaire getTransfert() {
      return this.transfert;
   }

}
