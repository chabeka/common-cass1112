package fr.urssaf.image.sae.services.indexComposite.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.referential.model.SaeIndexComposite;
import fr.urssaf.image.sae.metadata.referential.support.SaeIndexCompositeSupport;
import fr.urssaf.image.sae.service.indexComposite.IndexCompositeService;
import fr.urssaf.image.sae.storage.dfce.exception.DocumentTypeException;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import net.docubase.toolkit.model.reference.CompositeIndex;

@Service
public class IndexCompositeServiceImpl implements IndexCompositeService {

  SaeIndexCompositeSupport serviceSupport;

  @Autowired
  @Qualifier("dfceServices")
  private DFCEServices dfceServices;

  @Autowired
  private MappingDocumentService mappingDocumentService;

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

    // -- Ouverture de la connexion à DFCE;
    final boolean startActive = dfceServices.isServerUp();

    // -- Recuperation la liste des index composites
    final List<SaeIndexComposite> listIndexComposites = new ArrayList<>();

    try {
      if (!startActive) {
        dfceServices.connectTheFistTime();
      }

      if (dfceServices.getCnxParams() != null) {
        final Set<CompositeIndex> compositeIndexes = dfceServices.fetchAllCompositeIndex();
        final Iterator<CompositeIndex> iter = compositeIndexes.iterator();
        while (iter.hasNext()) {
          final CompositeIndex index = iter.next();
          final SaeIndexComposite saeIndexComposite = new SaeIndexComposite(index);
          // On ne récupère que les indexComposite indexés
          if (index.isComputed()) {
            listIndexComposites.add(saeIndexComposite);
          }
        }
      }
    }
    catch (final ConnectionServiceEx exception) {
      throw new DocumentTypeException("impossible de se connecter à DFCE", exception);
    }
    finally {
      if (!startActive) {
        dfceServices.closeConnexion();
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
      throws InvalidSAETypeException, MappingFromReferentialException {

    return mappingDocumentService.untypedMetadatasToCodeSaeMetadatas(metadatas);

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
  public boolean isIndexedMetadata(final UntypedMetadata metadata) throws MappingFromReferentialException {

    return mappingDocumentService.isIndexedMetadata(metadata);
  }

  @Override
  public boolean isIndexedMetadataByShortCode(final String shortCodeMetadata) throws MappingFromReferentialException {

    return mappingDocumentService.isIndexedMetadataByShortCode(shortCodeMetadata);
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
