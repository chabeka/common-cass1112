package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire
 */
public class Test1101Formulaire extends TestWsParentFormulaire {

   private final CaptureUnitaireFormulaire captUnit = new CaptureUnitaireFormulaire(
         this);

   private final RechercheFormulaire rechercheFormulaire = new RechercheFormulaire(this);
   
   
   private String dernierIdArchiv;
   private String dernierSha1;


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
