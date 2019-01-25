package fr.urssaf.image.sae.services.consultation.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.mail.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionParametrageException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionRuntimeException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConvertisseurInitialisationException;
import fr.urssaf.image.sae.format.conversion.service.ConversionService;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.metadata.referential.services.SAEControlMetadataService;
import fr.urssaf.image.sae.metadata.referential.services.SAEConvertMetadataService;
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.services.consultation.SAEConsultationService;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.document.impl.AbstractSAEServices;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationAffichableParametrageException;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;
import fr.urssaf.image.sae.services.util.UntypedMetadataFinderUtils;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Implémentation du service {@link SAEConsultationService}
 */
@Service
@Qualifier("saeConsultationService")
public class SAEConsultationServiceImpl extends AbstractSAEServices implements
                                        SAEConsultationService {
  /**
   *
   */
  private static final String SEPARATOR_STRING = ", ";

  private static final Logger LOG = LoggerFactory
                                                 .getLogger(SAEConsultationServiceImpl.class);

  private final MetadataReferenceDAO referenceDAO;

  @Autowired
  private SAEConvertMetadataService convertService;

  @Autowired
  private SAEControlMetadataService controlService;

  @Autowired
  private PrmdService prmdService;

  private final MappingDocumentService mappingService;

  /**
   * Service permettant de réaliser la conversion des documents.
   */
  @Autowired
  private ConversionService conversionService;

  /**
   * attribution des paramètres de l'implémentation
   *
   * @param referenceDAO
   *          instance du service des métadonnées de référence
   * @param mappingService
   *          instance du service de mapping du SAE
   */
  @Autowired
  public SAEConsultationServiceImpl(final MetadataReferenceDAO referenceDAO,
                                    final MappingDocumentService mappingService) {
    super();
    this.referenceDAO = referenceDAO;
    this.mappingService = mappingService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UntypedDocument consultation(final UUID idArchive)
      throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
      MetaDataUnauthorizedToConsultEx {

    final ConsultParams consultParams = new ConsultParams(idArchive);

    return consultation(consultParams);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UntypedDocument consultation(final ConsultParams consultParams)
      throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
      MetaDataUnauthorizedToConsultEx {

    final UUID idArchive = consultParams.getIdArchive();

    // Traces debug - entrée méthode
    final String prefixeTrc = "consultation()";
    LOG.debug("{} - Début", prefixeTrc);
    LOG.debug("{} - UUID envoyé par l'application cliente : {}",
              prefixeTrc,
              idArchive);
    // Fin des traces debug - entrée méthode

    try {

      // Liste des métadonnées à consulter
      final List<String> metadatas = manageMetaDataNames(consultParams);

      LOG.debug("{} - Liste des métadonnées consultable : \"{}\"",
                prefixeTrc,
                buildMessageFromList(metadatas));

      final List<StorageMetadata> allMeta = new ArrayList<>();
      final Map<String, MetadataReference> listeAllMeta = referenceDAO
                                                                      .getAllMetadataReferences();
      String shortCode;
      for (final String mapKey : listeAllMeta.keySet()) {
        shortCode = listeAllMeta.get(mapKey).getShortCode();
        // On ne récupère la note, le gel et la durée de conservation que si
        // elle est demandée à la consulation (car sinon cela génère un
        // appel à DFCE inutile)
        if (StorageTechnicalMetadatas.NOTE.getShortCode().equals(shortCode)) {
          if (metadatas.contains(StorageTechnicalMetadatas.NOTE
                                                               .getLongCode())) {
            allMeta.add(new StorageMetadata(shortCode));
          }
        } else if (StorageTechnicalMetadatas.GEL.getShortCode()
                                                .equals(
                                                        shortCode)) {
          if (metadatas.contains(StorageTechnicalMetadatas.GEL
                                                              .getLongCode())) {
            allMeta.add(new StorageMetadata(shortCode));
          }
        } else if (StorageTechnicalMetadatas.DUREE_CONSERVATION
                                                               .getShortCode()
                                                               .equals(shortCode)) {
          if (metadatas
                       .contains(StorageTechnicalMetadatas.DUREE_CONSERVATION
                                                                             .getLongCode())) {
            allMeta.add(new StorageMetadata(shortCode));
          }
        } else {
          allMeta.add(new StorageMetadata(shortCode));
        }

      }

      final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, allMeta);

      // On récupère le document à partir de l'UUID, avec toutes les
      // métadonnées du référentiel
      final StorageDocument storageDocument = getStorageDocumentService()
                                                                         .retrieveStorageDocumentByUUID(uuidCriteria);

      UntypedDocument untypedDocument = null;

      // Vérification des droits
      if (storageDocument != null) {
        untypedDocument = mappingService
                                        .storageDocumentToUntypedDocument(storageDocument);

        LOG.debug("{} - Récupération des droits", prefixeTrc);
        final AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
                                                                                     .getContext()
                                                                                     .getAuthentication();
        final List<SaePrmd> saePrmds = token.getSaeDroits().get("consultation");
        LOG.debug("{} - Vérification des droits", prefixeTrc);
        final boolean isPermitted = prmdService.isPermitted(
                                                            untypedDocument.getUMetadatas(),
                                                            saePrmds);

        if (!isPermitted) {
          throw new AccessDeniedException(
                                          "Le document est refusé à la consultation car les droits sont insuffisants");
        }

        // On filtre uniquement sur les métadonnées souhaitées à la
        // consultation
        final List<UntypedMetadata> list = filterMetadatas(metadatas,
                                                           untypedDocument.getUMetadatas());
        untypedDocument.setUMetadatas(list);

      }

      LOG.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode
      return untypedDocument;

    }
    catch (final RetrievalServiceEx e) {

      throw new SAEConsultationServiceException(e);

    }
    catch (final ReferentialException e) {

      throw new SAEConsultationServiceException(e);

    }
    catch (final InvalidSAETypeException e) {

      throw new SAEConsultationServiceException(e);

    }
    catch (final MappingFromReferentialException e) {

      throw new SAEConsultationServiceException(e);

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UntypedDocument consultationAffichable(
                                                final ConsultParams consultParams)
      throws SAEConsultationServiceException,
      UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
      SAEConsultationAffichableParametrageException {

    final UUID idArchive = consultParams.getIdArchive();

    // Traces debug - entrée méthode
    final String prefixeTrc = "consultationAffichable()";
    LOG.debug("{} - Début", prefixeTrc);
    LOG.debug("{} - UUID envoyé par l'application cliente : {}",
              prefixeTrc,
              idArchive);
    // Fin des traces debug - entrée méthode

    try {
      final List<String> metadatas = manageMetaDataNames(consultParams);

      LOG.debug("{} - Liste des métadonnées consultable : \"{}\"",
                prefixeTrc,
                buildMessageFromList(metadatas));

      final List<StorageMetadata> allMeta = new ArrayList<>();

      final Map<String, MetadataReference> listeAllMeta = referenceDAO
                                                                      .getAllMetadataReferences();
      String shortCode;
      for (final String mapKey : listeAllMeta.keySet()) {
        shortCode = listeAllMeta.get(mapKey).getShortCode();
        // On ne récupère la note, le gel et la durée de conservation que
        // si elle est demandée à la consulation (car sinon cela génère
        // un appel à DFCE inutile)
        if (StorageTechnicalMetadatas.NOTE.getShortCode().equals(shortCode)) {
          if (metadatas.contains(StorageTechnicalMetadatas.NOTE
                                                               .getLongCode())) {
            allMeta.add(new StorageMetadata(shortCode));
          }
        } else if (StorageTechnicalMetadatas.GEL.getShortCode()
                                                .equals(
                                                        shortCode)) {
          if (metadatas.contains(StorageTechnicalMetadatas.GEL
                                                              .getLongCode())) {
            allMeta.add(new StorageMetadata(shortCode));
          }
        } else if (StorageTechnicalMetadatas.DUREE_CONSERVATION
                                                               .getShortCode()
                                                               .equals(shortCode)) {
          if (metadatas
                       .contains(StorageTechnicalMetadatas.DUREE_CONSERVATION
                                                                             .getLongCode())) {
            allMeta.add(new StorageMetadata(shortCode));
          }
        } else {
          allMeta.add(new StorageMetadata(shortCode));
        }
      }

      final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, allMeta);

      // On récupère le document à partir de l'UUID, avec toutes les
      // métadonnées du référentiel
      final StorageDocument storageDocument = getStorageDocumentService()
                                                                         .retrieveStorageDocumentByUUID(uuidCriteria);

      UntypedDocument untypedDocument = null;

      if (storageDocument != null) {
        untypedDocument = mappingService
                                        .storageDocumentToUntypedDocument(storageDocument);

        LOG.debug("{} - Récupération des droits", prefixeTrc);
        final AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
                                                                                     .getContext()
                                                                                     .getAuthentication();
        final List<SaePrmd> saePrmds = token.getSaeDroits().get("consultation");
        LOG.debug("{} - Vérification des droits", prefixeTrc);
        final boolean isPermitted = prmdService.isPermitted(
                                                            untypedDocument.getUMetadatas(),
                                                            saePrmds);

        if (!isPermitted) {
          throw new AccessDeniedException(
                                          "Le document est refusé à la consultation car les droits sont insuffisants");
        }

        // recuperation de l'identifiant de format
        final String idFormat = UntypedMetadataFinderUtils.valueMetadataFinder(
                                                                               untypedDocument.getUMetadatas(),
                                                                               "FormatFichier");

        // On filtre uniquement sur les métadonnées souhaitées à la
        // consultation
        final List<UntypedMetadata> list = filterMetadatas(metadatas,
                                                           untypedDocument.getUMetadatas());
        untypedDocument.setUMetadatas(list);

        // recupere le train de byte au format natif
        final InputStream inputContent = untypedDocument.getContent()
                                                        .getInputStream();
        final byte[] byteArray = org.apache.commons.io.IOUtils
                                                              .toByteArray(inputContent);

        // conversion du fichier
        final byte[] fichierConverti = conversionService.convertirFichier(
                                                                          idFormat,
                                                                          byteArray,
                                                                          consultParams.getNumeroPage(),
                                                                          consultParams.getNombrePages());

        // remplacement du fichier d'origine par le fichier converti
        // TODO : voir comment remplacer le application/pdf
        final ByteArrayDataSource dataSource = new ByteArrayDataSource(
                                                                       fichierConverti,
                                                                       "application/pdf");
        final DataHandler dataHandler = new DataHandler(dataSource);
        untypedDocument.setContent(dataHandler);
      }

      LOG.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode
      return untypedDocument;

    }
    catch (final RetrievalServiceEx e) {

      throw new SAEConsultationServiceException(e);

    }
    catch (final ReferentialException e) {

      throw new SAEConsultationServiceException(e);

    }
    catch (final InvalidSAETypeException e) {

      throw new SAEConsultationServiceException(e);

    }
    catch (final MappingFromReferentialException e) {

      throw new SAEConsultationServiceException(e);

    }
    catch (final IOException ex) {
      throw new SAEConsultationServiceException(ex);
    }
    catch (final ConvertisseurInitialisationException ex) {
      throw new SAEConsultationServiceException(ex);
    }
    catch (final UnknownFormatException ex) {
      throw new SAEConsultationServiceException(ex);
    }
    catch (final ConversionParametrageException ex) {
      throw new SAEConsultationAffichableParametrageException(
                                                              ex.getMessage(),
                                                              ex);
    }
    catch (final ConversionRuntimeException ex) {
      throw new SAEConsultationServiceException(ex);
    }
  }

  /**
   * @param metadatas
   * @param uMetadatas
   * @return
   */
  private List<UntypedMetadata> filterMetadatas(final List<String> metadatas,
                                                final List<UntypedMetadata> uMetadatas) {

    final List<UntypedMetadata> metaList = uMetadatas;
    UntypedMetadata currentMetadata;

    for (int index = uMetadatas.size() - 1; index >= 0; index--) {
      currentMetadata = metaList.get(index);

      if (!metadatas.contains(currentMetadata.getLongCode())) {
        metaList.remove(index);
      }
    }

    return metaList;
  }

  /**
   * Retourne la liste des metadatas à renvoyer en fonction de celles stockées
   * dans l'objet de paramétrage
   *
   * @param consultParams
   *          paramètres de consultation
   * @return la liste des metadatas à consulter
   * @throws UnknownDesiredMetadataEx
   *           levée lorsque la metadata paramétrée n'existe pas
   * @throws ReferentialException
   *           levée lorsqu'au moins une metadata n'existe pas
   * @throws MetaDataUnauthorizedToConsultEx
   *           levée lorsqu'au moins une metadata n'est pas consultable
   */
  private List<String> manageMetaDataNames(final ConsultParams consultParams)
      throws UnknownDesiredMetadataEx, ReferentialException,
      MetaDataUnauthorizedToConsultEx {

    final List<String> keyList = new ArrayList<>();

    if (CollectionUtils.isEmpty(consultParams.getMetadonnees())) {

      final Map<String, MetadataReference> map = referenceDAO
                                                             .getDefaultConsultableMetadataReferences();

      for (final MetadataReference metaRef : map.values()) {
        keyList.add(metaRef.getLongCode());
      }

    } else {

      try {
        controlService.controlLongCodeExist(consultParams.getMetadonnees());

      }
      catch (final LongCodeNotFoundException longExcept) {
        final String message = ResourceMessagesUtils.loadMessage(
                                                                 "consultation.metadonnees.inexistante",
                                                                 StringUtils.join(longExcept.getListCode(), SEPARATOR_STRING));
        throw new UnknownDesiredMetadataEx(message, longExcept);
      }

      try {
        controlService.controlLongCodeIsAFConsultation(consultParams
                                                                    .getMetadonnees());
      }
      catch (final LongCodeNotFoundException longNotFoundEx) {
        final String message = ResourceMessagesUtils.loadMessage(
                                                                 "consultation.metadonnees.non.consultable",
                                                                 StringUtils.join(
                                                                                  longNotFoundEx.getListCode(),
                                                                                  SEPARATOR_STRING));
        throw new MetaDataUnauthorizedToConsultEx(message, longNotFoundEx);
      }

      keyList.addAll(consultParams.getMetadonnees());

    }

    return keyList;
  }

  /**
   * Construit une chaîne qui comprends l'ensemble des objets à afficher dans
   * les logs. <br>
   * Exemple : "UntypedMetadata[code long:=Titre,value=Attestation],
   * UntypedMetadata[code long:=DateCreation,value=2011-09-01],
   * UntypedMetadata[code long:=ApplicationProductrice,value=ADELAIDE]"
   *
   * @param <T>
   *          le type d'objet
   * @param list
   *          : liste des objets à afficher.
   * @return Une chaîne qui représente l'ensemble des objets à afficher.
   */
  private <T> String buildMessageFromList(final Collection<T> list) {
    final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
                                                             ToStringStyle.SIMPLE_STYLE);
    for (final T o : Utils.nullSafeIterable(list)) {
      if (o != null) {
        toStrBuilder.append(o.toString());
      }
    }
    return toStrBuilder.toString();
  }
}
