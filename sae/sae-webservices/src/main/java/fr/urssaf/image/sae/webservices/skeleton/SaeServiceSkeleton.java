/**
 * SaeServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 *
 * Le fichier est ensuite mis à jour manuellement lors de l'évolution du WSDL
 *
 */
package fr.urssaf.image.sae.webservices.skeleton;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.cirtil.www.saeservice.AjoutNote;
import fr.cirtil.www.saeservice.AjoutNoteResponse;
import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseAvecHash;
import fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.cirtil.www.saeservice.ArchivageUnitairePJ;
import fr.cirtil.www.saeservice.ArchivageUnitairePJResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationAffichable;
import fr.cirtil.www.saeservice.ConsultationAffichableResponse;
import fr.cirtil.www.saeservice.ConsultationGNTGNS;
import fr.cirtil.www.saeservice.ConsultationGNTGNSResponse;
import fr.cirtil.www.saeservice.ConsultationMTOM;
import fr.cirtil.www.saeservice.ConsultationMTOMResponse;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.Copie;
import fr.cirtil.www.saeservice.CopieResponse;
import fr.cirtil.www.saeservice.Deblocage;
import fr.cirtil.www.saeservice.DeblocageResponse;
import fr.cirtil.www.saeservice.DocumentExistant;
import fr.cirtil.www.saeservice.DocumentExistantResponse;
import fr.cirtil.www.saeservice.EtatTraitementsMasse;
import fr.cirtil.www.saeservice.EtatTraitementsMasseResponse;
import fr.cirtil.www.saeservice.GetDocFormatOrigine;
import fr.cirtil.www.saeservice.GetDocFormatOrigineResponse;
import fr.cirtil.www.saeservice.Modification;
import fr.cirtil.www.saeservice.ModificationMasse;
import fr.cirtil.www.saeservice.ModificationMasseResponse;
import fr.cirtil.www.saeservice.ModificationResponse;
import fr.cirtil.www.saeservice.PingRequest;
import fr.cirtil.www.saeservice.PingResponse;
import fr.cirtil.www.saeservice.PingSecureRequest;
import fr.cirtil.www.saeservice.PingSecureResponse;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheNbRes;
import fr.cirtil.www.saeservice.RechercheNbResResponse;
import fr.cirtil.www.saeservice.RechercheParIterateur;
import fr.cirtil.www.saeservice.RechercheParIterateurResponse;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.cirtil.www.saeservice.RecuperationMetadonnees;
import fr.cirtil.www.saeservice.RecuperationMetadonneesResponse;
import fr.cirtil.www.saeservice.Reprise;
import fr.cirtil.www.saeservice.RepriseResponse;
import fr.cirtil.www.saeservice.RestoreMasse;
import fr.cirtil.www.saeservice.RestoreMasseResponse;
import fr.cirtil.www.saeservice.StockageUnitaire;
import fr.cirtil.www.saeservice.StockageUnitaireResponse;
import fr.cirtil.www.saeservice.Suppression;
import fr.cirtil.www.saeservice.SuppressionMasse;
import fr.cirtil.www.saeservice.SuppressionMasseResponse;
import fr.cirtil.www.saeservice.SuppressionResponse;
import fr.cirtil.www.saeservice.Transfert;
import fr.cirtil.www.saeservice.TransfertMasse;
import fr.cirtil.www.saeservice.TransfertMasseResponse;
import fr.cirtil.www.saeservice.TransfertResponse;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureExistingUuuidException;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationAffichableParametrageException;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.exception.copie.SAECopieServiceException;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.webservices.SaeService;
import fr.urssaf.image.sae.webservices.exception.AjoutNoteAxisFault;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;
import fr.urssaf.image.sae.webservices.exception.ConsultationAxisFault;
import fr.urssaf.image.sae.webservices.exception.CopieAxisFault;
import fr.urssaf.image.sae.webservices.exception.DeblocageAxisFault;
import fr.urssaf.image.sae.webservices.exception.DocumentExistantAxisFault;
import fr.urssaf.image.sae.webservices.exception.ErreurInterneAxisFault;
import fr.urssaf.image.sae.webservices.exception.EtatTraitementsMasseAxisFault;
import fr.urssaf.image.sae.webservices.exception.GetDocFormatOrigineAxisFault;
import fr.urssaf.image.sae.webservices.exception.ModificationAxisFault;
import fr.urssaf.image.sae.webservices.exception.RechercheAxis2Fault;
import fr.urssaf.image.sae.webservices.exception.RepriseAxisFault;
import fr.urssaf.image.sae.webservices.exception.RestoreAxisFault;
import fr.urssaf.image.sae.webservices.exception.SuppressionAxisFault;
import fr.urssaf.image.sae.webservices.exception.TransfertAxisFault;
import fr.urssaf.image.sae.webservices.security.exception.SaeAccessDeniedAxisFault;
import fr.urssaf.image.sae.webservices.service.WSCaptureMasseService;
import fr.urssaf.image.sae.webservices.service.WSCaptureService;
import fr.urssaf.image.sae.webservices.service.WSConsultationService;
import fr.urssaf.image.sae.webservices.service.WSCopieService;
import fr.urssaf.image.sae.webservices.service.WSDeblocageService;
import fr.urssaf.image.sae.webservices.service.WSDocumentAttacheService;
import fr.urssaf.image.sae.webservices.service.WSDocumentExistantService;
import fr.urssaf.image.sae.webservices.service.WSEtatJobMasseService;
import fr.urssaf.image.sae.webservices.service.WSMetadataService;
import fr.urssaf.image.sae.webservices.service.WSModificationMasseService;
import fr.urssaf.image.sae.webservices.service.WSModificationService;
import fr.urssaf.image.sae.webservices.service.WSNoteService;
import fr.urssaf.image.sae.webservices.service.WSRechercheService;
import fr.urssaf.image.sae.webservices.service.WSRepriseService;
import fr.urssaf.image.sae.webservices.service.WSRestoreMasseService;
import fr.urssaf.image.sae.webservices.service.WSSuppressionMasseService;
import fr.urssaf.image.sae.webservices.service.WSSuppressionService;
import fr.urssaf.image.sae.webservices.service.WSTransfertMasseService;
import fr.urssaf.image.sae.webservices.service.WSTransfertService;
import fr.urssaf.image.sae.webservices.util.WsMessageRessourcesUtils;

/**
 * Skeleton du web service coté serveur<br>
 * <br>
 * La configuration se trouve dans le fichier <code>services.xml</code><br>
 * <br>
 * Code généré la 1ère fois à partir du plugin maven
 * <code>axis2-wsdl2code-maven-plugin</code><br>
 * <br>
 * La classe doit ensuite être mise à jour manuellement selon l'évolution du
 * WSDL.
 */
@Component
public class SaeServiceSkeleton implements SaeServiceSkeletonInterface {
  private static final Logger LOG = LoggerFactory.getLogger(SaeServiceSkeleton.class);

  private static final String STOCKAGE_INDISPO = "StockageIndisponible";

  private static final String MES_STOCKAGE = "ws.dfce.stockage";

  private final SaeService service;

  @Autowired
  private WSConsultationService consultation;

  @Autowired
  private WSRechercheService search;

  @Autowired
  private WSCaptureService capture;

  @Autowired
  private WSCaptureMasseService captureMasse;

  @Autowired
  private WSSuppressionMasseService suppressionMasse;

  @Autowired
  private WSRestoreMasseService restoreMasse;

  @Autowired
  private WSModificationService modificationService;

  @Autowired
  private WSSuppressionService suppressionService;

  @Autowired
  private WSTransfertService transfertService;

  @Autowired
  private WSMetadataService wsMetadataService;
  @Autowired
  private WSNoteService noteService;

  @Autowired
  private WSDocumentAttacheService documentAttacheService;

  @Autowired
  private WSEtatJobMasseService etatJobMasseService;

  // @Autowired
  // private DfceInfoService dfceInfoService;
  @Autowired
  private WsMessageRessourcesUtils wsMessageRessourcesUtils;

  @Autowired
  private WSCopieService copieService;

  @Autowired
  WSDocumentExistantService documentExistantService;

  @Autowired
  private WSTransfertMasseService transfertMasse;

  @Autowired
  private WSDeblocageService deblocageService;

  @Autowired
  private WSRepriseService repriseService;

  @Autowired
  private WSModificationMasseService modificationMasse;

  /**
   * Durée maximum d'une requete soap
   */
  private final int dureeMaxRequete;

  /**
   * Instanciation du service {@link SaeService}
   *
   * @param service
   *          implémentation des services web
   */
  @Autowired
  public SaeServiceSkeleton(final SaeService service, @Value("${sae.duree.max.requete.soap}") final int dureeMaxRequete) {
    Assert.notNull(service, "service is required");

    this.service = service;
    this.dureeMaxRequete = dureeMaxRequete;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PingResponse ping(final PingRequest pingRequest) {
    final PingResponse response = new PingResponse();

    response.setPingString(service.ping());

    return response;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PingSecureResponse pingSecure(final PingSecureRequest pingRequest) {
    final PingSecureResponse response = new PingSecureResponse();

    response.setPingString(service.pingSecure());

    return response;
  }

  /**
   * {@inheritDoc}
   *
   * @throws CaptureAxisFault
   * @throws SaeAccessDeniedAxisFault
   */
  @Override
  public ArchivageUnitaireResponse archivageUnitaireSecure(final ArchivageUnitaire request)
      throws CaptureAxisFault, SaeAccessDeniedAxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération archivageUnitaireSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final ArchivageUnitaireResponse response = capture.archivageUnitaire(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final CaptureAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final ConnectionServiceEx ex) {
      final CaptureAxisFault spf = new CaptureAxisFault(STOCKAGE_INDISPO,
                                                        wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new CaptureAxisFault("ErreurInterneCapture",
                                 "Une erreur interne à l'application est survenue lors de la capture.",
                                 ex);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws CaptureAxisFault
   */
  @Override
  public ArchivageUnitairePJResponse archivageUnitairePJSecure(final ArchivageUnitairePJ request)
      throws AxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération archivageUnitairePJSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final ArchivageUnitairePJResponse response = capture.archivageUnitairePJ(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final CaptureAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final ConnectionServiceEx ex) {
      final CaptureAxisFault spf = new CaptureAxisFault(STOCKAGE_INDISPO,
                                                        wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new CaptureAxisFault("ErreurInterneCapture",
                                 "Une erreur interne à l'application est survenue lors de la capture.",
                                 ex);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws CaptureAxisFault
   * @throws SaeAccessDeniedAxisFault
   */
  @Override
  public ArchivageMasseResponse archivageMasseSecure(final ArchivageMasse request, final String callerIP)
      throws CaptureAxisFault, SaeAccessDeniedAxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération archivageMasseSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode

      // l'opération web service n'interagit pas avec DFCE
      // il n'est pas nécessaire de vérifier si DFCE est Up
      final ArchivageMasseResponse response = captureMasse.archivageEnMasse(request,
                                                                            callerIP);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final CaptureAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new CaptureAxisFault("ErreurInterneCapture",
                                 "Une erreur interne à l'application est survenue lors de la capture.",
                                 ex);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws RechercheAxis2Fault
   * @throws SaeAccessDeniedAxisFault
   */
  @Override
  public RechercheResponse rechercheSecure(final Recherche request)
      throws RechercheAxis2Fault, SaeAccessDeniedAxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération rechercheSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final RechercheResponse response = search.search(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final RechercheAxis2Fault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final ConnectionServiceEx ex) {
      final RechercheAxis2Fault spf = new RechercheAxis2Fault(STOCKAGE_INDISPO,
                                                              wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new RechercheAxis2Fault("ErreurInterneRecherche",
                                    "Une erreur interne à l'application est survenue lors de la recherche.",
                                    ex);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws SaeAccessDeniedAxisFault
   */
  @Override
  public ConsultationResponse consultationSecure(final Consultation request)
      throws ConsultationAxisFault, SaeAccessDeniedAxisFault {
    // Traces debug - entrée méthode
    final String prefixeTrc = "Opération consultationSecure()";

    try {
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final ConsultationResponse response = consultation.consultation(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final ConsultationAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final ConnectionServiceEx ex) {
      final ConsultationAxisFault spf = new ConsultationAxisFault(STOCKAGE_INDISPO,
                                                                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new ConsultationAxisFault("ErreurInterneConsultation",
                                      "Une erreur interne à l'application est survenue lors de la consultation.",
                                      ex);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws SaeAccessDeniedAxisFault
   */
  @Override
  public ConsultationMTOMResponse consultationMTOMSecure(final ConsultationMTOM request)
      throws ConsultationAxisFault, SaeAccessDeniedAxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération consultationMTOMSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final ConsultationMTOMResponse responseMTOM = consultation.consultationMTOM(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return responseMTOM;
    }
    catch (final ConsultationAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final ConnectionServiceEx ex) {
      final ConsultationAxisFault spf = new ConsultationAxisFault(STOCKAGE_INDISPO,
                                                                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, ex));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new ConsultationAxisFault("ErreurInterneConsultation",
                                      "Une erreur interne à l'application est survenue lors de la consultation.",
                                      ex);
    }
  }

  @Override
  public CopieResponse copieSecure(final Copie request)
      throws CopieAxisFault, SaeAccessDeniedAxisFault, ArchiveInexistanteEx,
      SAEConsultationServiceException, SAECaptureServiceEx,
      ReferentialRndException, UnknownCodeRndEx, ReferentialException,
      SAECopieServiceException, UnknownDesiredMetadataEx,
      MetaDataUnauthorizedToConsultEx, RequiredStorageMetadataEx,
      InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
      DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
      RequiredArchivableMetadataEx, NotArchivableMetadataEx,
      UnknownHashCodeEx, EmptyFileNameEx, MetadataValueNotInDictionaryEx,
      UnknownFormatException, ValidationExceptionInvalidFile,
      UnexpectedDomainException, InvalidPagmsCombinaisonException,
      CaptureExistingUuuidException {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération copieSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final CopieResponse response = copieService.copie(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final CopieAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final ConnectionServiceEx ex) {
      final CopieAxisFault spf = new CopieAxisFault(STOCKAGE_INDISPO,
                                                    wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new CopieAxisFault("ErreurInterneCopie",
                               "Une erreur interne à l'application est survenue lors de la copie.",
                               ex);
    }
  }

  @Override
  public DocumentExistantResponse documentExistant(final DocumentExistant request)
      throws DocumentExistantAxisFault, SearchingServiceEx,
      ConnectionServiceEx {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération copieSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final DocumentExistantResponse response = documentExistantService.documentExistant(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final DocumentExistantAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final ConnectionServiceEx ex) {
      final DocumentExistantAxisFault spf = new DocumentExistantAxisFault(STOCKAGE_INDISPO,
                                                                          wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new DocumentExistantAxisFault("ErreurInterneCopie",
                                          "Une erreur interne à l'application est survenue lors de la vérification d'un document.",
                                          ex);
    }
  }

  private void logSoapFault(final AxisFault fault) {
    LOG.warn("Une exception AxisFault a été levée", fault);
  }

  private void logRuntimeException(final RuntimeException exception) {
    LOG.warn("Une exception RuntimeException a été levée", exception);
  }

  /**
   * {@inheritDoc}
   *
   * @throws CaptureAxisFault
   * @throws SaeAccessDeniedAxisFault
   */
  @Override
  public  ArchivageMasseAvecHashResponse archivageMasseAvecHashSecure(final ArchivageMasseAvecHash request, final String callerIP)
      throws CaptureAxisFault, SaeAccessDeniedAxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération archivageMasseAvecHashSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode

      // l'opération web service n'interagit pas avec DFCE
      // il n'est pas nécessaire de vérifier si DFCE est Up
      final ArchivageMasseAvecHashResponse response = captureMasse.archivageEnMasseAvecHash(request,
                                                                                            callerIP);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final CaptureAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new CaptureAxisFault("ErreurInterneCapture",
                                 "Une erreur interne à l'application est survenue lors de la capture.",
                                 ex);
    }
  }

  @Override
  public ModificationResponse modificationSecure(final Modification request)
      throws AxisFault {
    try {
      final String trcPrefix = "modificationSecure";
      LOG.debug("{} - début", trcPrefix);

      final ModificationResponse response = modificationService.modification(request);

      LOG.debug("{} - fin", trcPrefix);

      return response;
    }
    catch (final ModificationAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException ex) {
      throw new SaeAccessDeniedAxisFault(ex);
    }
    catch (final ConnectionServiceEx ex) {
      final ModificationAxisFault spf = new ModificationAxisFault(STOCKAGE_INDISPO,
                                                                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new ModificationAxisFault("ErreurInterneModification",
                                      "Une erreur interne à l'application est survenue lors de la modification",
                                      ex);
    }
  }

  @Override
  public SuppressionResponse suppressionSecure(final Suppression request)
      throws AxisFault {
    try {
      final String trcPrefix = "suppressionSecure";
      LOG.debug("{} - début", trcPrefix);

      final SuppressionResponse response = suppressionService.suppression(request);

      LOG.debug("{} - fin", trcPrefix);

      return response;
    }
    catch (final SuppressionAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException ex) {
      throw new SaeAccessDeniedAxisFault(ex);
    }
    catch (final ConnectionServiceEx ex) {
      final SuppressionAxisFault spf = new SuppressionAxisFault(STOCKAGE_INDISPO,
                                                                wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new SuppressionAxisFault("ErreurInterneSuppression",
                                     "Une erreur interne à l'application est survenue lors de la suppression",
                                     ex);
    }
  }

  @Override
  public RecuperationMetadonneesResponse recuperationMetadonneesSecure(final RecuperationMetadonnees request)
      throws AxisFault {
    try {
      final String trcPrefix = "recuperationMetadonneesSecure";
      LOG.debug("{} - début", trcPrefix);

      final RecuperationMetadonneesResponse response = wsMetadataService.recupererMetadonnees();

      LOG.debug("{} - fin", trcPrefix);

      return response;
    }
    catch (final ErreurInterneAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException ex) {
      throw new SaeAccessDeniedAxisFault(ex);
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new ErreurInterneAxisFault(ex);
    }
  }

  @Override
  public TransfertResponse transfertSecure(final Transfert request)
      throws AxisFault {
    try {
      final String trcPrefix = "transfertSecure";
      LOG.debug("{} - début", trcPrefix);

      // -- Transfert du document
      final TransfertResponse response = transfertService.transfert(request);

      LOG.debug("{} - fin", trcPrefix);

      return response;
    }
    catch (final TransfertAxisFault e) {
      logSoapFault(e);
      throw e;
    }
    catch (final AccessDeniedException ex) {
      throw new SaeAccessDeniedAxisFault(ex);
    }
    catch (final ConnectionServiceEx ex) {
      final TransfertAxisFault spf = new TransfertAxisFault(STOCKAGE_INDISPO,
                                                            wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException e) {
      logRuntimeException(e);

      final String erreur = "Une erreur interne à l'application est survenue lors du transfert";
      throw new TransfertAxisFault("ErreurInterneTransfert", erreur, e);
    }
  }

  @Override
  public ConsultationAffichableResponse consultationAffichableSecure(final ConsultationAffichable request)
      throws AxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération consultationAffichableSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final ConsultationAffichableResponse response = consultation.consultationAffichable(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final ConsultationAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final ConnectionServiceEx ex) {
      final ConsultationAxisFault spf = new ConsultationAxisFault(STOCKAGE_INDISPO,
                                                                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new ConsultationAxisFault("ErreurInterneConsultation",
                                      "Une erreur interne à l'application est survenue lors de la consultation.",
                                      ex);
    }
  }

  @Override
  public RechercheNbResResponse rechercheNbResSecure(final RechercheNbRes request)
      throws AxisFault {
    try {
      // -- Traces debug - entrée méthode
      final String prefixeTrc = "Opération rechercheNbResSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      final RechercheNbResResponse response = search.searchWithNbRes(request);
      // -- Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      return response;
    }
    catch (final RechercheAxis2Fault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final ConnectionServiceEx ex) {
      final RechercheAxis2Fault spf = new RechercheAxis2Fault(STOCKAGE_INDISPO,
                                                              wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new RechercheAxis2Fault("ErreurInterneRecherche",
                                    "Une erreur interne à l'application est survenue lors de la recherche.",
                                    ex);
    }
  }

  @Override
  public RechercheParIterateurResponse rechercheParIterateurSecure(final RechercheParIterateur request)
      throws AxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération rechercheParIterateurSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final RechercheParIterateurResponse response = search.rechercheParIterateur(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final RechercheAxis2Fault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final ConnectionServiceEx ex) {
      final RechercheAxis2Fault spf = new RechercheAxis2Fault(STOCKAGE_INDISPO,
                                                              wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      if (ex.getCause() != null &&
          ex.getCause().toString().contains(ResourceMessagesUtils.loadMessage("search.syntax.lucene.range.exception"))) {
        logRuntimeException(ex);
        throw new RechercheAxis2Fault(
                                      "ErreurInterneRecherche",
                                      ResourceMessagesUtils.loadMessage("search.syntax.lucene.range.error"),
                                      ex);
      } else {
        logRuntimeException(ex);
        throw new RechercheAxis2Fault(
                                      "ErreurInterneRecherche",
                                      "Une erreur interne à l'application est survenue lors de la recherche.",
                                      ex);
      }

    }

  }

  @Override
  public AjoutNoteResponse ajoutNoteSecure(final AjoutNote request)
      throws AxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération ajoutNoteSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final AjoutNoteResponse response = noteService.ajoutNote(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final AjoutNoteAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final ConnectionServiceEx ex) {
      final AjoutNoteAxisFault spf = new AjoutNoteAxisFault(STOCKAGE_INDISPO,
                                                            wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new AjoutNoteAxisFault("ErreurInterneAjoutNote",
                                   "Une erreur interne à l'application est survenue lors de l'ajout d'une note.",
                                   ex);
    }
  }

  @Override
  public StockageUnitaireResponse stockageUnitaireSecure(final StockageUnitaire request)
      throws AxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération stockageUnitaireSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final StockageUnitaireResponse response = capture.stockageUnitaire(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final CaptureAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final ConnectionServiceEx ex) {
      final CaptureAxisFault spf = new CaptureAxisFault(STOCKAGE_INDISPO,
                                                        wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new CaptureAxisFault("ErreurInterneCapture",
                                 "Une erreur interne à l'application est survenue lors de la capture.",
                                 ex);
    }
  }

  @Override
  public GetDocFormatOrigineResponse getDocFormatOrigineSecure(final GetDocFormatOrigine request)
      throws AxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération getDocFormatOrigineSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final GetDocFormatOrigineResponse response = documentAttacheService.getDocFormatOrigine(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final GetDocFormatOrigineAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final ConnectionServiceEx ex) {
      final GetDocFormatOrigineAxisFault spf = new GetDocFormatOrigineAxisFault(STOCKAGE_INDISPO,
                                                                                wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new GetDocFormatOrigineAxisFault("ErreurInterneGetDocFormatOrigine",
                                             "Une erreur interne à l'application est survenue lors de la récupération du document au format d'origine.",
                                             ex);
    }
  }

  @Override
  public RestoreMasseResponse restoreMasseSecure(final RestoreMasse request,
                                                 final String callerIP)
                                                     throws AxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération restoreMasseSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode

      // l'opération web service n'interagit pas avec DFCE
      // il n'est pas nécessaire de vérifier si DFCE est Up
      final RestoreMasseResponse response = restoreMasse.restoreEnMasse(request,
                                                                        callerIP);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final RestoreAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new RestoreAxisFault("ErreurInterneRestore",
                                 "Une erreur interne à l'application est survenue lors de la restore.",
                                 ex);
    }
  }

  @Override
  public SuppressionMasseResponse suppressionMasseSecure(final SuppressionMasse request, final String callerIP)
      throws AxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération suppressionMasseSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode

      // l'opération web service n'interagit pas avec DFCE
      // il n'est pas nécessaire de vérifier si DFCE est Up
      final SuppressionMasseResponse response = suppressionMasse.suppressionEnMasse(request,
                                                                                    callerIP);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final SuppressionAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new SuppressionAxisFault("ErreurInterneSuppression",
                                     "Une erreur interne à l'application est survenue lors de la suppression.",
                                     ex);
    }
  }

  @Override
  public EtatTraitementsMasseResponse etatTraitementsMasse(final EtatTraitementsMasse request, final String callerIP)
      throws AxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération etatTraitementsMasse()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode

      // l'opération web service n'interagit pas avec DFCE
      // il n'est pas nécessaire de vérifier si DFCE est Up
      final EtatTraitementsMasseResponse response = etatJobMasseService.etatJobMasse(request,
                                                                                     callerIP);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final EtatTraitementsMasseAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new EtatTraitementsMasseAxisFault("ErreurInterneEtatTraitementsMasse",
                                              "Une erreur interne à l'application est survenue lors de la récupération des états des traitements de masse.",
                                              ex);
    }
  }

  @Override
  public ConsultationGNTGNSResponse consultationGNTGNSSecure(final ConsultationGNTGNS request)
      throws SearchingServiceEx, ConnectionServiceEx,
      SAEConsultationServiceException, UnknownDesiredMetadataEx,
      MetaDataUnauthorizedToConsultEx,
      SAEConsultationAffichableParametrageException, RemoteException {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération copieSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode
      final ConsultationGNTGNSResponse response = consultation.consultationGNTGNS(request);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final ConsultationAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final ConnectionServiceEx ex) {
      final ConsultationAxisFault spf = new ConsultationAxisFault(STOCKAGE_INDISPO,
                                                                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
      logSoapFault(spf);

      throw spf;
    }
  }

  @Override
  public ModificationMasseResponse modificationMasseSecure(final ModificationMasse request, final String callerIP)
      throws AxisFault {
    try {
      // Traces debug - entrée méthode
      final String prefixeTrc = "Opération archivageMasseAvecHashSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      // Fin des traces debug - entrée méthode

      // l'opération web service n'interagit pas avec DFCE
      // il n'est pas nécessaire de vérifier si DFCE est Up
      final ModificationMasseResponse response = modificationMasse.modificationMasse(request,
                                                                                     callerIP);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Fin des traces debug - sortie méthode
      return response;
    }
    catch (final ModificationAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new ModificationAxisFault("ErreurInterneModification",
                                      "Une erreur interne à l'application est survenue lors de la modification de masse.",
                                      ex);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws TransfertAxisFault
   * @throws SaeAccessDeniedAxisFault
   */
  @Override
  public TransfertMasseResponse transfertMasseSecure(final TransfertMasse request, final String callerIP)
      throws TransfertAxisFault, SaeAccessDeniedAxisFault {
    try {
      final String prefixeTrc = "Opération transfertMasseSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      final TransfertMasseResponse response = transfertMasse.transfertEnMasse(request,
                                                                              callerIP);
      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      return response;
    }
    catch (final TransfertAxisFault ex) {
      logSoapFault(ex);
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new TransfertAxisFault("ErreurInterneTransfert",
                                   "Une erreur interne à l'application est survenue lors de transfert en masse.",
                                   ex);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws DeblocageAxisFault
   * @throws SaeAccessDeniedAxisFault
   * @throws DeblocageAxisFault
   */
  @Override
  public  DeblocageResponse deblocageSecure(final Deblocage request,
                                            final String callerIP)
                                                throws DeblocageAxisFault, SaeAccessDeniedAxisFault,
                                                JobInexistantException {
    try {
      final String prefixeTrc = "Opération deblocageSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      final DeblocageResponse response = deblocageService.deblocage(request,
                                                                    callerIP);
      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      return response;
    }
    catch (final JobInexistantException ex) {
      LOG.warn("JobInexistantException:{}",
               ExceptionUtils
               .getFullStackTrace(ex));
      LOG.warn("échec de déblocage: Job inexistant");
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new DeblocageAxisFault("ErreurInterneDeblocage",
                                   "Une erreur interne à l'application est survenue lors de déblocage de job.",
                                   ex);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws RepriseAxisFault
   * @throws SaeAccessDeniedAxisFault
   * @throws DeblocageAxisFault
   */
  @Override
  public RepriseResponse repriseSecure(final Reprise request, final String callerIP)
      throws AxisFault, JobInexistantException {
    // TODO
    try {
      final String prefixeTrc = "Opération repriseSecure()";
      LOG.debug("{} - Début", prefixeTrc);

      final RepriseResponse response = repriseService.reprise(request, callerIP);
      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      return response;
    }
    catch (final JobInexistantException ex) {
      LOG.warn("JobInexistantException:{}",
               ExceptionUtils
               .getFullStackTrace(ex));
      LOG.warn("échec de reprise du job: Job inexistant");
      throw ex;
    }
    catch (final AccessDeniedException exception) {
      throw new SaeAccessDeniedAxisFault(exception);
    }
    catch (final RuntimeException ex) {
      logRuntimeException(ex);
      throw new RepriseAxisFault("ErreurInterneReprise",
                                 "Une erreur interne à l'application est survenue lors de la reprise du job.",
                                 ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getDureeMaxRequete() {
    return dureeMaxRequete;
  }

}
