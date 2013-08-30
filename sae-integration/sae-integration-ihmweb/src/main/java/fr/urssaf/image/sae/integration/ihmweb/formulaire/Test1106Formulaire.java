package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire pour le test 1106
 */
public class Test1106Formulaire extends TestWsParentFormulaire {

   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(
         this);

   private final RechercheFormulaire recherche = new RechercheFormulaire(this);

   private final ConsultationFormulaire consultation = new ConsultationFormulaire(
         this);
   
   private final CaptureMasseFormulaire captMasseDecl = new CaptureMasseFormulaire(
         this);

   private final CaptureMasseResultatFormulaire captMasseResult = new CaptureMasseResultatFormulaire();
   
   private final ComptagesTdmFormulaire comptagesFormulaire = new ComptagesTdmFormulaire(this);

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
    * Le sous-formulaire pour l'appel à l'opération "consultation"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "consultation"
    */
   public final ConsultationFormulaire getConsultation() {
      return this.consultation;
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
    * Le sous-formulaire pour l'appel à l'opération "archivageMasse"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "archivageMasse"
    */
   public final CaptureMasseFormulaire getCaptureMasseDeclenchement() {
      return this.captMasseDecl;
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
    * Le sous-formulaire pour les comptages
    * @return Le sous-formulaire pour les comptages
    */
   public final ComptagesTdmFormulaire getComptagesFormulaire() {
      return comptagesFormulaire;
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
    * @param dernierIdArchiv
    *           Le dernier id d'archivage obtenu en réponse de la capture
    *           unitaire
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
    * @param dernierSha1
    *           Le SHA-1 du dernier document envoyé lors de la capture unitaire
    */
   public final void setDernierSha1(String dernierSha1) {
      this.dernierSha1 = dernierSha1;
   }

}
