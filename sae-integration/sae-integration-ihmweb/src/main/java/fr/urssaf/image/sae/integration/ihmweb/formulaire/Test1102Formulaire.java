package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire
 */
public class Test1102Formulaire extends TestWsParentFormulaire {

   private final CaptureMasseFormulaire captMasseDecl = new CaptureMasseFormulaire(
         this);

   private final CaptureMasseResultatFormulaire captMasseResult = new CaptureMasseResultatFormulaire();
   
   private final RechercheFormulaire rechercheFormulaire = new RechercheFormulaire(this);
   

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
}
