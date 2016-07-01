package fr.urssaf.image.sae.integration.ihmweb.formulaire;

//import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

/**
 * Classe de sous-formulaire pour un test du WS SaeService, opération
 * "suppressionMasse"<br>
 * <br>
 * Un objet de cette classe s'associe au tag "supressionMasse.tag" (attribut
 * "objetFormulaire")
 */
public class SuppressionMasseFormulaire extends GenericForm {

   private ResultatTest resultats = new ResultatTest();

   private String requeteLucene;
   
//   private CodeMetadonneeList codeMetadonnees = new CodeMetadonneeList();

   /**
    * Constructeur
    * 
    * @param parent
    *           formulaire pere
    */
   public SuppressionMasseFormulaire(TestWsParentFormulaire parent) {
      super(parent);
   }
   
   
   /**
    * Constructeur
    * 
    */
   public SuppressionMasseFormulaire() {

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
   {}

   /**
    * La requête LUCENE
    * 
    * @return La requête LUCENE
    */
   public final String getRequeteLucene() {
      return requeteLucene;
   }

   /**
    * La requête LUCENE
    * 
    * @param requeteLucene
    *           La requête LUCENE
    */
   public final void setRequeteLucene(String requeteLucene) {
      this.requeteLucene = requeteLucene;
   }
   
   
//   /**
//    * La liste des codes des métadonnées que l'on souhaite dans les résultats de
//    * suppressionMasse
//    * 
//    * @return La liste des codes des métadonnées que l'on souhaite dans les
//    *         résultats de suppressionMasse
//    */
//   public final CodeMetadonneeList getCodeMetadonnees() {
//      return codeMetadonnees;
//   }
//
//   /**
//    * La liste des codes des métadonnées que l'on souhaite dans les résultats de
//    * suppressionMasse
//    * 
//    * @param codeMetadonnees
//    *           La liste des codes des métadonnées que l'on souhaite dans les
//    *           résultats de suppressionMasse
//    */
//   public final void setCodeMetadonnees(CodeMetadonneeList codeMetadonnees) {
//      this.codeMetadonnees = codeMetadonnees;
//   }

}