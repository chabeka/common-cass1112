package fr.urssaf.image.sae.bo.model.untyped;

import java.util.List;

/**
 * Classe représentant une page de document. Une page de document correspond à
 * un appel du service, et comporte une liste de documents, ainsi qu’un flag
 * indiquant s’il s’agit de la dernière page
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
    * Valeur de l'id de la prochaine page à retourner au client
    */
   private String pageId;

   /**
    * @return the documents
    */
   public final List<UntypedDocument> getDocuments() {
      return documents;
   }

   /**
    * @param documents
    *           the documents to set
    */
   public final void setDocuments(final List<UntypedDocument> documents) {
      this.documents = documents;
   }

   /**
    * @return the lastPage
    */
   public final Boolean getLastPage() {
      return lastPage;
   }

   /**
    * @param lastPage
    *           the lastPage to set
    */
   public final void setLastPage(final Boolean lastPage) {
      this.lastPage = lastPage;
   }

   /**
    * @return the pageId
    */
   public String getPageId() {
      return pageId;
   }

   /**
    * @param pageId
    *           the pageId to set
    */
   public void setPageId(final String pageId) {
      this.pageId = pageId;
   }

}
