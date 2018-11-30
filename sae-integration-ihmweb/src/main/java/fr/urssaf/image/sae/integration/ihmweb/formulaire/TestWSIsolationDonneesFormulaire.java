package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire
 */
public class TestWSIsolationDonneesFormulaire extends TestWsParentFormulaire {

   private final CaptureMasseFormulaire captureMasse = new CaptureMasseFormulaire(this);
   
   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(this);

   private final CaptureMasseResultatFormulaire captMasseResult = new CaptureMasseResultatFormulaire();

   private final RechercheFormulaire recherche = new RechercheFormulaire(this);

   private final ConsultationFormulaire consultFormulaire = new ConsultationFormulaire(this);
   
   private final ComptagesTdmFormulaire comptagesFormulaire = new ComptagesTdmFormulaire(this);
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "captureUnitaire"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "archivageMasse"
    */
   public CaptureUnitaireFormulaire getCaptureUnitaire() {
      return captureUnitaire;
   }

   /**
    * Le sous-formulaire pour l'appel à l'opération "archivageMasse"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "archivageMasse"
    */
   public final CaptureMasseFormulaire getCaptureMasse() {
      return this.captureMasse;
   }

   /**
    * Le sous-formulaire pour la lecture du résultat d'un traitement de masse, à
    * partir de l'ECDE
    * 
    * @return Le sous-formulaire pour la lecture du résultat d'un traitement de
    *         masse
    */
   public final CaptureMasseResultatFormulaire getCaptureMasseResultat() {
      return this.captMasseResult;
   }

   /**
    * Le sous-formulaire pour la recherche du résultat du traitement de masse
    * effectué auparavant
    * 
    * @return le sous-formulaire de recherche de document
    */
   public final RechercheFormulaire getRecherche() {
      return this.recherche;
   }

   /**
    * Le sous-formulaire de consultation de document
    * 
    * @return le sous-formulaire de consultation
    */
   public final ConsultationFormulaire getConsultFormulaire() {
      return this.consultFormulaire;
   }

   
   /**
    * Le sous-formulaire de comptages
    * 
    * @return le sous-formulaire de comptages
    */
   public final ComptagesTdmFormulaire getComptagesFormulaire() {
      return comptagesFormulaire;
   }


}
