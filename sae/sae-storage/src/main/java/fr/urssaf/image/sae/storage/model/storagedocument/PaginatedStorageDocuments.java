package fr.urssaf.image.sae.storage.model.storagedocument;

/**
 * Classe concrète représentant la liste des documents
 * 
 *
 */
public class PaginatedStorageDocuments extends StorageDocuments {

   /**
    * Flag indiquant s'il s'agit de la dernière page
    */
   private Boolean lastPage;

   /**
    * Valeur de la métadonnée à retourner au client dans l'identifiant de la dernière page
    */
   private String valeurMetaLastPage;
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
