package fr.urssaf.image.sae.storage.model.storagedocument;

/**
 * Classe concrète représentant la liste des documents
 */
public class PaginatedStorageDocuments extends StorageDocuments {

   /**
    * Flag indiquant s'il s'agit de la dernière page
    */
   private Boolean lastPage;

   /**
    * Valeur à retourner au client correspond à l'id de la prochaine page
    */
   private String pageId;

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
    * @return l'id de la prochaine page
    */
   public String getPageId() {
      return pageId;
   }

   /**
    * @param pageId
    *           l'id de la prochaine page
    */
   public void setPageId(final String pageId) {
      this.pageId = pageId;
   }
}
