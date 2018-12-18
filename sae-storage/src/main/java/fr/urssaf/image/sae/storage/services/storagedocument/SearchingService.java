package fr.urssaf.image.sae.storage.services.storagedocument;

import java.io.IOException;
import java.util.List;

import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.PaginatedStorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.PaginatedLuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Fournit les services de recherche de document<BR />
 * Ces services sont :
 * <ul>
 * <li>searchStorageDocumentByLuceneCriteria : service qui permet de faire une
 * recherche par une requête lucene.</li>
 * <li>searchStorageDocumentByUUIDCriteria : service qui permet de faire une
 * recherche de document par UUID</li>
 * <li>searchMetaDatasByUUIDCriteria : service qui permet de faire une recherche
 * des métadonnées par UUID.</li>
 * <li>searchPaginatedStorageDocuments : service qui permet de faire une
 * recherche paginée</li>
 * <li>searchStorageDocumentsInRecycleBean : service qui permet de faire une
 * recherche paginée dans la corbeille</li>
 * </ul>
 */
public interface SearchingService {

   /**
    * Permet de faire une recherche par une requête lucene.
    * 
    * @param luceneCriteria
    *           : La requête Lucene
    * @return Les résultats de la recherche
    * @throws SearchingServiceEx
    *            Exception lévée lorsque la recherche ne se déroule pas bien.
    * @throws QueryParseServiceEx
    *            Exception levée lorsque du parsing de la requête.
    */

   StorageDocuments searchStorageDocumentByLuceneCriteria(
                                                          final LuceneCriteria luceneCriteria)
         throws SearchingServiceEx,
         QueryParseServiceEx;

   /**
    * Permet de faire une recherche de document par UUID.
    * 
    * @param uuidCriteria
    *           : L'UUID du document à rechercher
    * @param forConsultation
    *           True si consultation, false sinon.
    * @return un strorageDocument
    * @throws SearchingServiceEx
    *            Exception lévée lorsque la recherche ne se déroule pas bien.
    */
   StorageDocument searchStorageDocumentByUUIDCriteria(
                                                       final UUIDCriteria uuidCriteria, boolean forConsultation)
         throws SearchingServiceEx;

   /**
    * Permet de faire une recherche des métadonnées par UUID.
    * 
    * @param uuidCriteria
    *           : L'UUID du document à rechercher
    * @return Le resultat de la recherche
    * @throws SearchingServiceEx
    *            Exception lévée lorsque la recherche ne se déroule pas bien.
    */
   StorageDocument searchMetaDatasByUUIDCriteria(final UUIDCriteria uuidCriteria)
         throws SearchingServiceEx;

   /**
    * Permet de faire une recherche paginée.
    * 
    * @param paginatedLuceneCriteria
    *           Objet contenant les critères de recherche
    * @return La liste des documents trouvés
    * @throws QueryParseServiceEx
    *            Une exception est levée lors de la recherche
    * @throws SearchingServiceEx
    *            Une exception est levée lors de la recherche
    */
   PaginatedStorageDocuments searchPaginatedStorageDocuments(
                                                             PaginatedLuceneCriteria paginatedLuceneCriteria)
         throws SearchingServiceEx, QueryParseServiceEx;

   /**
    * Permet de faire une recherche paginée dans la corbeille.
    * 
    * @param paginatedLuceneCriteria
    *           Objet contenant les critères de recherche
    * @return La liste des documents trouvés
    * @throws QueryParseServiceEx
    *            Une exception est levée lors de la recherche
    * @throws SearchingServiceEx
    *            Une exception est levée lors de la recherche
    */
   PaginatedStorageDocuments searchStorageDocumentsInRecycleBean(
                                                                 PaginatedLuceneCriteria paginatedLuceneCriteria)
         throws SearchingServiceEx, QueryParseServiceEx;

   /**
    * Permet de récupérer le contenu d'un document par UUID.
    * 
    * @param uuidCriteria
    *           : L'UUID du document à rechercher
    * @throws IOException
    * @{@link IOException}
    * @return Le contenu du document recherché.
    */
   byte[] searchStorageDocumentContentByUUIDCriteria(UUIDCriteria uUIDCriteria)
         throws IOException;

   /**
    * Permet de faire une recherche paginée, en utilisant le meilleur index possible
    * 
    * @param paginatedLuceneCriteria
    *           Les critères de recherche
    * @param bestIndex
    *           L'index le plus pertinent pour la recherche (liste des codes court des méta composant l'index)
    * @return
    * @throws SearchingServiceEx
    * @throws QueryParseServiceEx
    */
   PaginatedStorageDocuments searchPaginatedStorageDocumentsWithBestIndex(PaginatedLuceneCriteria paginatedLuceneCriteria,
                                                                          List<String> bestIndex)
         throws SearchingServiceEx, QueryParseServiceEx;
}
