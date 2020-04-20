package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.exceptions.IndexCompositeException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.model.SaeIndexComposite;
import fr.urssaf.image.sae.metadata.referential.services.IndexCompositeService;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import net.docubase.toolkit.model.reference.CompositeIndex;

@Service
public class IndexCompositeServiceImpl implements IndexCompositeService {

   /**
    * Service permettant d'exécuter la recherche paginée
    */
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider storageServiceProvider;

   @Autowired
   private MetadataReferenceDAO referenceDAO;

   /**
    * {@inheritDoc}
    */
   @Override
   public List<SaeIndexComposite> getAllComputedIndexComposite() {
      // -- Récupération la liste des index composites
      final List<SaeIndexComposite> listIndexComposites = new ArrayList<>();

      final Set<CompositeIndex> compositeIndexes = storageServiceProvider.getStorageDocumentService().getAllIndexComposite();
      final Iterator<CompositeIndex> iter = compositeIndexes.iterator();
      while (iter.hasNext()) {
         final CompositeIndex index = iter.next();
         final SaeIndexComposite saeIndexComposite = new SaeIndexComposite(index);
         // On ne récupère que les indexComposite indexés
         if (index.isComputed()) {
            listIndexComposites.add(saeIndexComposite);
         }
      }

      return listIndexComposites;

   }

   @Override
   public boolean checkIndexCompositeValid(final SaeIndexComposite indexComposite, final Collection<String> listShortCodeMetadatas) {
      boolean isIndexValid = true;

      final String[] listCriteresIndex = indexComposite.getName().split("&");

      // 1- L'indexComposite ne peut pas contenir plus de critères que la
      // requête
      if (listCriteresIndex.length > listShortCodeMetadatas.size()) {
         isIndexValid = false;
      }
      // 2- Les codes de l'indexComposite doivent être compris dans les
      // critères
      else {

         for (final String critere : listCriteresIndex) {
            if (!listShortCodeMetadatas.contains(critere)) {
               isIndexValid = false;
               break;
            }
         }

      }
      return isIndexValid;
   }

   @Override
   public boolean isIndexCompositeValid(final SaeIndexComposite indexComposite, final List<SAEMetadata> listSaeMetadatas) {

      return checkIndexCompositeValid(indexComposite, getListShortCodeMetadata(listSaeMetadatas));

   }

   @Override
   public List<SAEMetadata> untypedMetadatasToCodeSaeMetadatas(final List<UntypedMetadata> metadatas)
         throws IndexCompositeException {

      final List<SAEMetadata> saeMetadatas = new ArrayList<>();
      for (final UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO
                                                            .getByLongCode(metadata.getLongCode());

            saeMetadatas.add(new SAEMetadata(reference.getLongCode(),
                                             reference.getShortCode(),
                                             null));
         }
         catch (final ReferentialException refExcpt) {
            throw new IndexCompositeException("Erreur de récupération de la métadonnée " + metadata.getLongCode(), refExcpt);
         }
      }

      return saeMetadatas;

   }

   @Override
   public List<String> untypedMetadatasToShortCodeMetadatas(final List<UntypedMetadata> metadatas)
         throws IndexCompositeException {

      final List<String> result = new ArrayList<>();
      for (final UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO.getByLongCode(metadata.getLongCode());
            if (reference != null) {
               result.add(reference.getShortCode());
            }

         }
         catch (final ReferentialException refExcpt) {
            throw new IndexCompositeException("Erreur de récupération de la métadonnée " + metadata.getLongCode(), refExcpt);
         }
      }

      return result;
   }

   @Override
   public List<String> longCodeMetadatasToShortCodeMetadatas(final Collection<String> metadatas)
         throws IndexCompositeException {

      final List<String> result = new ArrayList<>();
      for (final String longCode : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO.getByLongCode(longCode);

            result.add(reference.getShortCode());
         }
         catch (final ReferentialException refExcpt) {
            throw new IndexCompositeException("Erreur de récupération de la métadonnée " + longCode, refExcpt);
         }
      }

      return result;
   }

   @Override
   public SaeIndexComposite getBestIndexComposite(final List<SaeIndexComposite> indexCandidats) {
      if (indexCandidats.isEmpty()) {
         return null;
      }
      // Rechercher l'index avec le plus de critères
      SaeIndexComposite bestIndex = indexCandidats.get(0);
      int bestIndexElementsCount = bestIndex.getCategories().size();
      for (final SaeIndexComposite currentIndex : indexCandidats) {
         final int currentIndexElementsCount = currentIndex.getCategories().size();
         if (currentIndexElementsCount > bestIndexElementsCount) {
            bestIndex = currentIndex;
            bestIndexElementsCount = currentIndexElementsCount;
         }
      }
      return bestIndex;
   }

   @Override
   public String getBestIndexForQuery(final Collection<String> shortCodeRequiredMetadatas)
         throws IndexCompositeException {
      if (shortCodeRequiredMetadatas == null) {
         return null;
      }
      // Récupération de la liste complète des index composites
      final List<SaeIndexComposite> candidats = getAllComputedIndexComposite();

      // On en garde que ceux qui conviennent
      final List<SaeIndexComposite> allowedCompositeIndex = new ArrayList<>();
      for (final SaeIndexComposite index : candidats) {
         if (checkIndexCompositeValid(index, shortCodeRequiredMetadatas)) {
            allowedCompositeIndex.add(index);
         }
      }
      if (!allowedCompositeIndex.isEmpty()) {
         // On prend le plus pertinent
         final SaeIndexComposite indexCompositeCandidat = getBestIndexComposite(allowedCompositeIndex);
         return indexCompositeCandidat.getName();
      } else {
         // On n'a pas trouvé d'index composite qui convient. On cherche donc un index simple
         for (final String metadata : shortCodeRequiredMetadatas) {
            if (isIndexedMetadataByShortCode(metadata)) {
               return metadata;
            }
         }

      }
      // On n'a rien trouvé !
      return null;
   }

   @Override
   public boolean isIndexedMetadata(final UntypedMetadata metadata) throws IndexCompositeException {

      boolean isMetadataIndexed = false;

      try {
         final MetadataReference reference = referenceDAO
                                                         .getByLongCode(metadata.getLongCode());
         isMetadataIndexed = reference.getIsIndexed();

      }
      catch (final ReferentialException refExcpt) {
         throw new IndexCompositeException("Erreur de récupération de la métadonnée " + metadata.getLongCode(), refExcpt);
      }

      return isMetadataIndexed;
   }

   @Override
   public boolean isIndexedMetadataByShortCode(final String shortCodeMetadata) throws IndexCompositeException {

      boolean isMetadataIndexed = false;

      try {
         final MetadataReference reference = referenceDAO
                                                         .getByShortCode(shortCodeMetadata);
         isMetadataIndexed = reference.getIsIndexed();

      }
      catch (final ReferentialException refExcpt) {
         throw new IndexCompositeException("Erreur de récupération de la métadonnée " + shortCodeMetadata, refExcpt);
      }

      return isMetadataIndexed;
   }

   /**
    * Retourne une liste de codes courts des métadonnées passées en paramètre
    * 
    * @param listMetadata
    * @return
    */
   private List<String> getListShortCodeMetadata(final List<SAEMetadata> listMetadata) {

      final List<String> listShortCodeMetadata = new ArrayList<>();
      for (final SAEMetadata saeMetadata : listMetadata) {
         listShortCodeMetadata.add(saeMetadata.getShortCode());
      }
      return listShortCodeMetadata;

   }

}
