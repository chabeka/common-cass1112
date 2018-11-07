package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
   * Constructeur par défaut
   */
  public IndexCompositeServiceImpl() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SaeIndexComposite> getAllComputedIndexComposite() {
    // -- Recuperation la liste des index composites
    final List<SaeIndexComposite> listIndexComposites = new ArrayList<>();

    final Set<CompositeIndex> compositeIndexes = storageServiceProvider.getStorageDocumentService().getAllCompositeIndex();
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
  public boolean checkIndexCompositeValid(final SaeIndexComposite indexComposite, final List<String> listShortCodeMetadatas) {
    boolean isIndexValid = false;

    final List<String> listCriteresIndex = new ArrayList<>(Arrays.asList(indexComposite.getName().split("&")));

    // 1- L'indexComposite ne peut pas contenir plus de critères que la
    // requête
    if (listCriteresIndex.size() > listShortCodeMetadatas.size()) {
      isIndexValid = false;
    }
    // 2- Les codes de l'indexComposite doivent être compris dans les
    // critères
    else if (!listShortCodeMetadatas.isEmpty() && listCriteresIndex.size() <= listShortCodeMetadatas.size()) {

      for (final String critere : listCriteresIndex) {
        if (listShortCodeMetadatas.contains(critere)) {
          isIndexValid = true;
        } else {
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
  public SaeIndexComposite getBestIndexComposite(final List<SaeIndexComposite> indexCandidats) {
    // Rechercher l'index avec le plus de criteres
    SaeIndexComposite bestIndexComposite = indexCandidats.get(0);

    for (final SaeIndexComposite saeIndexComposite : indexCandidats) {
      if (saeIndexComposite.getName().split("&").length > bestIndexComposite.getName().split("&").length) {
        bestIndexComposite = saeIndexComposite;
      }
    }
    return bestIndexComposite;
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
   * Retourne une list de codes courts des metadonnees passees en paramtre
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
