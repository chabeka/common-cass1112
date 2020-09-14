/**
 *
 */
package fr.urssaf.image.sae.services.suppression.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.document.impl.AbstractSAEServices;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.suppression.SuppressionException;
import fr.urssaf.image.sae.services.suppression.SAESuppressionService;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe implémentant l'interface {@link SAESuppressionService}. Cette classe
 * est un singleton et peut être accessible par le système d'injection IOC avec
 * l'annotation @Autowired
 *
 */
@Service
public class SAESuppressionServiceImpl extends AbstractSAEServices implements SAESuppressionService {

  private static final Logger LOG = LoggerFactory.getLogger(SAESuppressionServiceImpl.class);

  @Autowired
  @Qualifier("storageDocumentService")
  private StorageDocumentService storageService;

  @Autowired
  private MetadataReferenceDAO referenceDAO;

  @Autowired
  private PrmdService prmdService;

  @Autowired
  private MappingDocumentService mappingService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void suppression(final UUID idArchive) throws SuppressionException, ArchiveInexistanteEx {
    final String trcPrefix = "suppression";
    LOG.debug("{} - début", trcPrefix);
    // L'idArchive ne peut être null à ce niveau il a été contrôlé par l'aspect au niveau du paramètre d'entrée
    LOG.debug("{} - Début de suppression du document {}", new Object[] {trcPrefix, idArchive.toString()});

    try {
      LOG.debug("{} - recherche du document", trcPrefix);
      final List<StorageMetadata> allMeta = new ArrayList<>();
      final Map<String, MetadataReference> listeAllMeta = referenceDAO.getAllMetadataReferencesPourVerifDroits();
      for (final Map.Entry<String, MetadataReference> entry : listeAllMeta.entrySet()) {
        allMeta.add(new StorageMetadata(entry.getValue().getShortCode()));
      }
      final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, allMeta);
      final List<StorageMetadata> listeStorageMeta = getStorageDocumentService()
          .retrieveStorageDocumentMetaDatasByUUID(uuidCriteria);

      // On vérifie si le document n'est pas gelé
      final String frozenDocMsgException = "Le document {0} est gelé et ne peut pas être traité.";

      final List<StorageMetadata> listeMetadataDocument = getListeStorageMetadatasWithGel(idArchive);
      if (isFrozenDocument(listeMetadataDocument)) {
        throw new SuppressionException(
                                       StringUtils.replace(frozenDocMsgException, "{0}", idArchive.toString()));
      }

      if (listeStorageMeta.size() == 0) {
        final String message = StringUtils.replace(
                                                   "Il n'existe aucun document pour l'identifiant d'archivage '{0}'", "{0}", idArchive.toString());
        throw new ArchiveInexistanteEx(message);
      }
      final List<UntypedMetadata> listeUMeta = mappingService.storageMetadataToUntypedMetadata(listeStorageMeta);

      // Vérification des droits
      LOG.debug("{} - Récupération des droits", trcPrefix);
      final AuthenticationToken token = (AuthenticationToken) SecurityContextHolder.getContext()
          .getAuthentication();
      final List<SaePrmd> saePrmds = token.getSaeDroits().get("suppression");
      LOG.debug("{} - Vérification des droits", trcPrefix);
      final boolean isPermitted = prmdService.isPermitted(listeUMeta, saePrmds);

      if (!isPermitted) {
        throw new AccessDeniedException(
            "Le document est refusé à la suppression car les droits sont insuffisants");

      }

      LOG.debug("{} - suppression du document", trcPrefix);
      storageService.deleteStorageDocument(idArchive);

    } catch (final DeletionServiceEx exception) {
      throw new SuppressionException(exception);
    } catch (final ReferentialException exception) {
      throw new SuppressionException(exception);
    } catch (final InvalidSAETypeException exception) {
      throw new SuppressionException(exception);
    } catch (final MappingFromReferentialException exception) {
      throw new SuppressionException(exception);
    } catch (final RetrievalServiceEx exception) {
      throw new SuppressionException(exception);
    }

    LOG.debug("{} - Suppression du document {} terminée", new Object[] { trcPrefix, idArchive.toString() });
    LOG.debug("{} - fin", trcPrefix);
  }

  /**
   * Méthode permettant de générer la liste des métadonnées storage contenant
   * la métadonnée GEL
   * 
   * @param idArchive
   * @return
   * @throws ReferentialException
   * @throws RetrievalServiceEx
   */
  public List<StorageMetadata> getListeStorageMetadatasWithGel(final UUID idArchive)
      throws ReferentialException, RetrievalServiceEx {
    // On récupère la liste de toutes les méta du référentiel sauf la
    // Note, le Gel et la durée de conservation inutile pour les droits
    // et générant des accès DFCE inutiles
    final List<StorageMetadata> allMeta = new ArrayList<>();
    final Map<String, MetadataReference> listeAllMeta = referenceDAO.getAllMetadataReferencesPourVerifDroits();
    for (final Map.Entry<String, MetadataReference> entry : listeAllMeta.entrySet()) {
      allMeta.add(new StorageMetadata(entry.getValue().getShortCode()));
    }
    // Ajout de la meta GEL puisque non récupéré avant
    allMeta.add(new StorageMetadata(StorageTechnicalMetadatas.GEL.getShortCode()));

    // Création des critéres
    final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, allMeta);

    // Recherche du document par critére
    return getStorageDocumentService().retrieveStorageDocumentMetaDatasByUUID(uuidCriteria);
  }

}
