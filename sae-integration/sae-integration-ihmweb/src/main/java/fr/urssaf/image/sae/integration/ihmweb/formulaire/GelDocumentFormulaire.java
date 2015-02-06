package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import java.util.UUID;


/**
 * Classe de sous-formulaire pour le gel de document
 * <br>
 * Un objet de cette classe s'associe au tag "geldocument" (attribut "objetFormulaire")
 */
public class GelDocumentFormulaire {
   
   private UUID idDocument;
   
   private String resultats = new String();
   
   /**
    * Les résultats de l'appel à l'opération
    * 
    * @return Les résultats de l'appel à l'opération
    */
   public final String getResultats() {
      return this.resultats;
   }

   /**
    * Les résultats de l'appel à l'opération
    * 
    * @param resultats
    *           Les résultats de l'appel à l'opération
    */
   public final void setResultats(String resultats) {
      this.resultats = resultats;
   }


   /**
    * L'identifiant unique du document à geler
    * 
    * @return L'identifiant unique du document à modifier
    */
   public UUID getIdDocument() {
      return idDocument;
   }

   /**
    * L'identifiant unique du document à geler
    * 
    * @param idDocument
    *           L'identifiant unique du document à modifier
    */
   public void setIdDocument(UUID idDocument) {
      this.idDocument = idDocument;
   }
}
