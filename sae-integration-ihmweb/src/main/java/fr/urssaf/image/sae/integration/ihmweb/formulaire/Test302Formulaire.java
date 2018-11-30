package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire
 */
public class Test302Formulaire extends TestWsParentFormulaire {

   private RechercheFormulaire rechFormulaire = new RechercheFormulaire(this);

   private final ComptagesTdmFormulaire comptagesFormulaire = new ComptagesTdmFormulaire(this);
   
   
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
    * Le sous-formulaire de comptages
    * 
    * @return le sous-formulaire de comptages
    */
   public final ComptagesTdmFormulaire getComptagesFormulaire() {
      return comptagesFormulaire;
   }


}
