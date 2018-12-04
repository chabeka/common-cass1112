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
   
   private final AjoutNoteFormulaire ajoutNote = new AjoutNoteFormulaire(this) ;
   
   private final ConsultationFormulaire consultation = new ConsultationFormulaire(this);
   
   private String dernierSha1;
   /**
    * Sha1 du dernier doc capturé
    * @return Sha1 du dernier doc capturé
    */
   public String getDernierSha1() {
      return dernierSha1;
   }

   /**
    * Sha1 du dernier doc capturé
    */
   public void setDernierSha1(String dernierSha1) {
      this.dernierSha1 = dernierSha1;
   }
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "ajoutNote"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "ajoutNote"
    */
   public final AjoutNoteFormulaire getAjoutNote() {
      return this.ajoutNote;
   }


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

   /**
    * Le sous-formulaire pour l'appel à l'opération "consultation"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "consultation"
    */
   public final ConsultationFormulaire getConsultation() {
      return this.consultation;
   }
}
