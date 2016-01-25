package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import java.util.UUID;

import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

/**
 * Classe de sous-formulaire pour un test du WS SaeService, opération
 * "modification"<br>
 * <br>
 * Un objet de cette classe s'associe au tag "modification.tag" (attribut
 * "objetFormulaire")
 */
public class ModificationFormulaire extends GenericForm {

   private ResultatTest resultats = new ResultatTest();

   private UUID idDocument;
   
   private MetadonneeValeurList metadonnees = new MetadonneeValeurList();

   /**
    * Constructeur
    * 
    * @param parent
    *           formulaire pere
    */
   public ModificationFormulaire(TestWsParentFormulaire parent) {
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
    * La liste des métadonnées à modifier
    * 
    * @return La liste des métadonnées à modifier
    */
   public final MetadonneeValeurList getMetadonnees() {
      return metadonnees;
   }

   /**
    * La liste des métadonnées à modifier
    * 
    * @param metadonnees
    *           La liste des métadonnées à modifier
    */
   public final void setMetadonnees(MetadonneeValeurList metadonnees) {
      this.metadonnees = metadonnees;
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
