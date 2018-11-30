package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import java.util.UUID;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

/**
 * Classe de sous-formulaire pour un test du WS SaeService, opération
 * "suppression"<br>
 * <br>
 * Un objet de cette classe s'associe au tag "suppression.tag" (attribut
 * "objetFormulaire")
 */
public class SuppressionFormulaire extends GenericForm {

   private ResultatTest resultats = new ResultatTest();

   private UUID idDocument;

   
   /**
    * Constructeur
    * 
    * @param parent
    *           formulaire pere
    */
   public SuppressionFormulaire(TestWsParentFormulaire parent) {
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
    * L'identifiant unique du document à modifier
    * 
    * @return L'identifiant unique du document à modifier
    */
   public UUID getIdDocument() {
      return idDocument;
   }

   /**
    * L'identifiant unique du document à modifier
    * 
    * @param idDocument
    *           L'identifiant unique du document à modifier
    */
   public void setIdDocument(UUID idDocument) {
      this.idDocument = idDocument;
   }

}
