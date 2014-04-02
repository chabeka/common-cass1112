package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire
 */
public class TestFormulaireFcpCmReCo extends TestWsParentFormulaire {

   private final CaptureMasseFormulaire captMasseDecl = new CaptureMasseFormulaire(
         this);

   private final CaptureMasseResultatFormulaire captMasseResult = new CaptureMasseResultatFormulaire();
   
   private final RechercheFormulaire rechercheFormulaire = new RechercheFormulaire(this);
   
   private final ConsultationFormulaire consultation = new ConsultationFormulaire(
         this);
   
   private final ComptagesTdmFormulaire comptagesFormulaire = new ComptagesTdmFormulaire(this);
   
   private String dernierIdArchiv;
   private String dernierSha1;

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
    * Le sous formulaire de la recherche
    * @return le sous formulaire de la recherche
    */

   public RechercheFormulaire getRechercheFormulaire() {
      return rechercheFormulaire;
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
   
   /**
    * Le sous-formulaire de comptages
    * 
    * @return le sous-formulaire de comptages
    */
   public final ComptagesTdmFormulaire getComptagesFormulaire() {
      return comptagesFormulaire;
   }
}
