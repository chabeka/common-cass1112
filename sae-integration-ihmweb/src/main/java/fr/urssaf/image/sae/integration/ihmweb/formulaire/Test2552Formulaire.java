package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire
 */
public class Test2552Formulaire extends TestWsParentFormulaire {

   private final CaptureMasseFormulaire captMasseDecl = new CaptureMasseFormulaire(this);

   private final CaptureMasseResultatFormulaire captMasseResult = new CaptureMasseResultatFormulaire();

   private final RechercheFormulaire rechFormulaire = new RechercheFormulaire(this);

   private final ConsultationFormulaire consultFormulaire = new ConsultationFormulaire(this);
   
   private final ComptagesTdmFormulaire comptagesFormulaire = new ComptagesTdmFormulaire(this);

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
    * Le sous-formulaire pour la recherche du résultat du traitement de masse
    * effectué auparavant
    * 
    * @return le sous-formulaire de recherche de document
    */
   public final RechercheFormulaire getRechFormulaire() {
      return this.rechFormulaire;
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
