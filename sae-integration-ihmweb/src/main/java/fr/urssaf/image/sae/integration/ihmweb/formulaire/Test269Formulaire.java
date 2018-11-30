package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire
 */
public class Test269Formulaire extends TestStockageMasseAllFormulaire {

   private final CaptureMasseFormulaire captMasseDeclTwo = new CaptureMasseFormulaire(
         this);

   private final RechercheFormulaire rechFormulaireParallele = new RechercheFormulaire(
         this);

   /**
    * Le sous-formulaire pour l'appel à l'opération "archivageMasse" pour un
    * appel sur le meme serveur que l'appel précédent : utilisé pour réaliser
    * deux appels d'archivage de masse sur le meme serveur
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "archivageMasse"
    */
   public final CaptureMasseFormulaire getCaptureMasseDeclenchementParallele() {
      return this.captMasseDeclTwo;
   }

   public final RechercheFormulaire getRechFormulaireParallele() {
      return rechFormulaireParallele;
   }

}
