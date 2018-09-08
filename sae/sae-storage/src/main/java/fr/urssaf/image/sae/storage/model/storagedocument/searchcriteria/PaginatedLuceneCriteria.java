package fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria;

import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.AbstractFilter;

/**
 * Classe concrète contenant les critères pour une recherche paginée
 * 
 * 
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
    * @param lastIdDoc
    *           Identifiant du dernier document de la page précédente
    * 
    * @param codeCourtVaryingMeta
    *           Code court de la métadonnée variable
    */
   public PaginatedLuceneCriteria(String luceneQuery, int limit,
         List<StorageMetadata> desiredStorageMetadatas,
         List<AbstractFilter> filters, UUID lastIdDoc,
         String codeCourtVaryingMeta) {
      super(luceneQuery, limit, desiredStorageMetadatas);
      this.filters = filters;
      this.lastIdDoc = lastIdDoc;
      this.codeCourtVaryingMeta = codeCourtVaryingMeta;

   }

   /**
    * Nom de la métadonnée variable
    */
   private String codeCourtVaryingMeta;

   /**
    * Liste de filtre à rajouter après l'exécution de la requête principale
    */
   private List<AbstractFilter> filters;

   /**
    * Identifiant du dernier document de la page précédente
    */
   private UUID lastIdDoc;

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
   public final void setFilters(List<AbstractFilter> filters) {
      this.filters = filters;
   }

   /**
    * @return the lastIdDoc
    */
   public final UUID getLastIdDoc() {
      return lastIdDoc;
   }

   /**
    * @param lastIdDoc
    *           the lastIdDoc to set
    */
   public final void setLastIdDoc(UUID lastIdDoc) {
      this.lastIdDoc = lastIdDoc;
   }

   /**
    * @return the codeCourtVaryingMeta
    */
   public String getCodeCourtVaryingMeta() {
      return codeCourtVaryingMeta;
   }

   /**
    * @param codeCourtVaryingMeta
    *           the codeCourtVaryingMeta to set
    */
   public void setCodeCourtVaryingMeta(String codeCourtVaryingMeta) {
      this.codeCourtVaryingMeta = codeCourtVaryingMeta;
   }

}
