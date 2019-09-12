package fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria;

import java.util.List;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.AbstractFilter;

/**
 * Classe concrète contenant les critères pour une recherche paginée
 */
public class PaginatedLuceneCriteria extends LuceneCriteria {

   /**
    * Construit un {@link PaginatedLuceneCriteria }
    * 
    * @param luceneQuery
    *           La requête lucene
    * @param limit
    *           Le nombre maximum de documents retournés
    * @param desiredStorageMetadatas
    *           Les métadonnées désirées
    * @param filters
    *           Liste de filtre à rajouter après l'exécution de la requête
    *           principale
    * @param pageId
    *           Identifie la page à retourner (ou null pour la 1ere page)
    */
   public PaginatedLuceneCriteria(final String luceneQuery, final int limit,
                                  final List<StorageMetadata> desiredStorageMetadatas,
                                  final List<AbstractFilter> filters, final String pageId) {
      super(luceneQuery, limit, desiredStorageMetadatas);
      this.filters = filters;
      this.pageId = pageId;
   }

   /**
    * Liste de filtre à rajouter après l'exécution de la requête principale
    */
   private List<AbstractFilter> filters;

   /**
    * Identifie la page à retourner (ou null pour la 1ere page)
    */
   private String pageId;

   /**
    * @return the filters
    */
   public final List<AbstractFilter> getFilters() {
      return filters;
   }

   /**
    * @param filters
    *           the filters to set
    */
   public final void setFilters(final List<AbstractFilter> filters) {
      this.filters = filters;
   }

   /**
    * @return the lastIdDoc
    */
   public final String getPageId() {
      return pageId;
   }

   /**
    * @param lastIdDoc
    *           the lastIdDoc to set
    */
   public final void setLastIdDoc(final String pageId) {
      this.pageId = pageId;
   }

}
