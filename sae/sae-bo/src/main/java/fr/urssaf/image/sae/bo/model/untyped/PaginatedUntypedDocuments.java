package fr.urssaf.image.sae.bo.model.untyped;

import java.util.List;

/**
 * Classe représentant une page de document. Une page de document correspond à
 * un appel du service, et comporte une liste de documents, ainsi qu’un flag
 * indiquant s’il s’agit de la dernière page
 * 
 * 
 */
public class PaginatedUntypedDocuments {

   /**
    * Valeur minimum de la métadonnée
    */
   private List<UntypedDocument> documents;

   /**
    * Valeur maximum de la métadonnée
    */
   private Boolean lastPage;
   
   /**
    * Valeur de la métadonnée à retourner au client dans l'identifiant de la dernière page
    */
   private String valeurMetaLastPage;

   /**
    * @return the documents
    */
   public final List<UntypedDocument> getDocuments() {
      return documents;
   }

   /**
    * @param documents the documents to set
    */
   public final void setDocuments(List<UntypedDocument> documents) {
      this.documents = documents;
   }

   /**
    * @return the lastPage
    */
   public final Boolean getLastPage() {
      return lastPage;
   }

   /**
    * @param lastPage the lastPage to set
    */
   public final void setLastPage(Boolean lastPage) {
      this.lastPage = lastPage;
   }

   /**
    * @return the valeurMetaLastPage
    */
   public String getValeurMetaLastPage() {
      return valeurMetaLastPage;
   }

   /**
    * @param valeurMetaLastPage the valeurMetaLastPage to set
    */
   public void setValeurMetaLastPage(String valeurMetaLastPage) {
      this.valeurMetaLastPage = valeurMetaLastPage;
   }

 
}
