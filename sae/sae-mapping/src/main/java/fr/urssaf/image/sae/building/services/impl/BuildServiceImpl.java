package fr.urssaf.image.sae.building.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.activation.DataHandler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.building.services.BuildService;
import fr.urssaf.image.sae.mapping.utils.Utils;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.AbstractFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.PaginatedLuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Classe qui fournit des services de construction des objets métiers.
 */
@Service
@Qualifier("buildService")
public class BuildServiceImpl implements BuildService {

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final LuceneCriteria buildStorageLuceneCriteria(
                                                          final String luceneQuery, final int limit,
                                                          final List<SAEMetadata> metadatas) {
      final List<StorageMetadata> desiredMetadata = new ArrayList<>();
      for (final SAEMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         desiredMetadata.add(new StorageMetadata(metadata.getShortCode()));

      }
      return new LuceneCriteria(luceneQuery, limit, desiredMetadata);
   }

   @Override
   public PaginatedLuceneCriteria buildStoragePaginatedLuceneCriteria(
                                                                      final String luceneQuery, final int limit, final List<SAEMetadata> metadatas,
                                                                      final List<AbstractFilter> filters, final String pageId) {
      final List<StorageMetadata> desiredMetadata = new ArrayList<>();
      for (final SAEMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         desiredMetadata.add(new StorageMetadata(metadata.getShortCode()));

      }
      return new PaginatedLuceneCriteria(luceneQuery,
                                         limit,
                                         desiredMetadata,
                                         filters,
                                         pageId);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final UUIDCriteria buildStorageUuidCriteria(final UUID uuid,
                                                      final List<SAEMetadata> metadatas) {
      final List<StorageMetadata> desiredMetadata = new ArrayList<>();
      for (final SAEMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         desiredMetadata.add(new StorageMetadata(metadata.getShortCode()));

      }
      return new UUIDCriteria(uuid, desiredMetadata);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final UntypedDocument buildUntypedDocument(final DataHandler content,
                                                     final Map<String, String> metadatas) {
      final List<UntypedMetadata> uMetadatas = new ArrayList<>();
      for (final Entry<String, String> uMetadata : Utils.nullSafeMap(metadatas)
                                                        .entrySet()) {
         uMetadatas.add(new UntypedMetadata(uMetadata.getKey(),
                                            uMetadata
                                                     .getValue()));
      }
      return new UntypedDocument(content, uMetadatas);

   }

}
