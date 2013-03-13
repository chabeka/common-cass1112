package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de formulaire
 */
public class Test224Formulaire extends TestWsParentFormulaire {

   private final CaptureMasseFormulaire captMasseDecl = new CaptureMasseFormulaire(this);

   private final CaptureMasseResultatFormulaire captMasseResult = new CaptureMasseResultatFormulaire();

   private List<RechercheFormulaire> rechFormulaireList = new ArrayList<RechercheFormulaire>();

   private final ConsultationFormulaire consultFormulaire = new ConsultationFormulaire(this);
   
   private final ComptagesTdmFormulaire comptagesFormulaire = new ComptagesTdmFormulaire(this);
   
   
  
   public Test224Formulaire() {
    super();
    //LazyList.decorate(rechFormulaireList, FactoryUtils.instantiateFactory(RechercheFormulaire.class));
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
    * Le sous-formulaire pour la recherche du résultat du traitement de masse
    * effectué auparavant
    * 
    * @return le sous-formulaire de recherche de document
    */
   public final List<RechercheFormulaire> getRechFormulaireList() {
      return this.rechFormulaireList;
   }
   
public void setRechFormulaireList(List<RechercheFormulaire> recherche){
   this.rechFormulaireList = recherche;
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
