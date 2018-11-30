package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

/**
 * Classe de sous-formulaire pour un test de traitement de masse<br>
 * <br>
 * Un objet de cette classe s'associe au tag "comptagesTdm.tag" (attribut
 * "objetFormulaire")
 */
public class ComptagesTdmFormulaire extends GenericForm {

   private ResultatTest resultats = new ResultatTest();

   private String idTdm;

   /**
    * constructeur
    * 
    * @param parent
    *           formulaire pere
    */
   public ComptagesTdmFormulaire(TestWsParentFormulaire parent) {
      super(parent);
   }

   /**
    * Les résultats de l'appel à l'opération
    * 
    * @return Les résultats de l'appel à l'opération
    */
   public final ResultatTest getResultats() {
      return this.resultats;
   }

   /**
    * Les résultats de l'appel à l'opération
    * 
    * @param resultats
    *           Les résultats de l'appel à l'opération
    */
   public final void setResultats(ResultatTest resultats) {
      this.resultats = resultats;
   }

   /**
    * L'identifiant du traitement de masse
    * 
    * @return L'identifiant du traitement de masse
    */
   public final String getIdTdm() {
      return idTdm;
   }

   /**
    * L'identifiant du traitement de masse
    * 
    * @param idTdm
    *           L'identifiant du traitement de masse
    */
   public final void setIdTdm(String idTdm) {
      this.idTdm = idTdm;
   }

}
