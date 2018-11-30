package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire
 */
public class Test22xFormulaire extends TestWsParentFormulaire {

   private final CaptureMasseFormulaire captMasseDecl = new CaptureMasseFormulaire(
         this);

   private final CaptureMasseResultatFormulaire captMasseResult = new CaptureMasseResultatFormulaire();
   
   private final ComptagesTdmFormulaire comptagesFormulaire = new ComptagesTdmFormulaire(this);
   
   private final RechercheFormulaire rechercheForm = new RechercheFormulaire(this);
   
   private final ConsultationFormulaire consultationForm = new ConsultationFormulaire(this);

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
      return this.comptagesFormulaire;
   }
   
   public final RechercheFormulaire getRechercheFormulaire(){
      return this.rechercheForm;
   }

   public final ConsultationFormulaire getConsultationFormulaire(){
      return this.consultationForm;
   }

}
