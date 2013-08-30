package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire
 */
public class Test1109Formulaire extends TestWsParentFormulaire {

   private final CaptureUnitaireFormulaire captUnit = new CaptureUnitaireFormulaire(
         this);

   
   private final CaptureMasseFormulaire captMasseDecl = new CaptureMasseFormulaire(
         this);

   private final CaptureMasseResultatFormulaire captMasseResult = new CaptureMasseResultatFormulaire();
   
   private final RechercheFormulaire rechercheFormulaire = new RechercheFormulaire(this);
   
   
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
    * @return the captUnitDecl
    */
   public CaptureUnitaireFormulaire getCaptUnit() {
      return captUnit;
   }

   /**
    * @return the dernierIdArchiv
    */
   public String getDernierIdArchiv() {
      return dernierIdArchiv;
   }

   /**
    * @param dernierIdArchiv the dernierIdArchiv to set
    */
   public void setDernierIdArchiv(String dernierIdArchiv) {
      this.dernierIdArchiv = dernierIdArchiv;
   }

   /**
    * @return the dernierSha1
    */
   public String getDernierSha1() {
      return dernierSha1;
   }

   /**
    * @param dernierSha1 the dernierSha1 to set
    */
   public void setDernierSha1(String dernierSha1) {
      this.dernierSha1 = dernierSha1;
   }
   
   
}
