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

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletResponse;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import fr.urssaf.image.sae.exploitation.service.DfceInfoService;
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
 * 
 */
@Component
public class SaeServiceSkeleton implements SaeServiceSkeletonInterface {

   private final SaeService service;
   private static final Logger LOG = LoggerFactory
         .getLogger(SaeServiceSkeleton.class);

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
   private WSMetadataService metadataService;

   @Autowired
   private WSNoteService noteService;

   @Autowired
   private WSDocumentAttacheService documentAttacheService;

   @Autowired
   private WSEtatJobMasseService etatJobMasseService;

   @Autowired
   private DfceInfoService dfceInfoService;

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

   private static final String STOCKAGE_INDISPO = "StockageIndisponible";
   private static final String MES_STOCKAGE = "ws.dfce.stockage";

   /**
    * Instanciation du service {@link SaeService}
    * 
    * @param service
    *           implémentation des services web
    */
   @Autowired
   public SaeServiceSkeleton(SaeService service) {

      Assert.notNull(service, "service is required");

      this.service = service;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final PingResponse ping(PingRequest pingRequest) {

      PingResponse response = new PingResponse();

      response.setPingString(service.ping());

      return response;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final PingSecureResponse pingSecure(PingSecureRequest pingRequest) {

      PingSecureResponse response = new PingSecureResponse();

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
   public final ArchivageUnitaireResponse archivageUnitaireSecure(
         ArchivageUnitaire request) throws CaptureAxisFault,
         SaeAccessDeniedAxisFault {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération archivageUnitaireSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            ArchivageUnitaireResponse response = capture
                  .archivageUnitaire(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new CaptureAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }
      } catch (CaptureAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new CaptureAxisFault(
               "ErreurInterneCapture",
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
   public final ArchivageUnitairePJResponse archivageUnitairePJSecure(
         ArchivageUnitairePJ request) throws AxisFault {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération archivageUnitairePJSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            ArchivageUnitairePJResponse response = capture
                  .archivageUnitairePJ(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new CaptureAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }
      } catch (CaptureAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new CaptureAxisFault(
               "ErreurInterneCapture",
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
   public final ArchivageMasseResponse archivageMasseSecure(
         ArchivageMasse request, String callerIP) throws CaptureAxisFault,
         SaeAccessDeniedAxisFault {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération archivageMasseSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         // l'opération web service n'interagit pas avec DFCE
         // il n'est pas nécessaire de vérifier si DFCE est Up
         ArchivageMasseResponse response = captureMasse.archivageEnMasse(
               request, callerIP);

         // Traces debug - sortie méthode
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode

         return response;

      } catch (CaptureAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new CaptureAxisFault(
               "ErreurInterneCapture",
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
   public final RechercheResponse rechercheSecure(Recherche request)
         throws RechercheAxis2Fault, SaeAccessDeniedAxisFault {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération rechercheSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            RechercheResponse response = search.search(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new RechercheAxis2Fault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }
      } catch (RechercheAxis2Fault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new RechercheAxis2Fault(
               "ErreurInterneRecherche",
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
   public final ConsultationResponse consultationSecure(Consultation request)
         throws ConsultationAxisFault, SaeAccessDeniedAxisFault {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération consultationSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            ConsultationResponse response = consultation.consultation(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new ConsultationAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }
      } catch (ConsultationAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new ConsultationAxisFault(
               "ErreurInterneConsultation",
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
   public final ConsultationMTOMResponse consultationMTOMSecure(
         ConsultationMTOM request) throws ConsultationAxisFault,
         SaeAccessDeniedAxisFault {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération consultationMTOMSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            ConsultationMTOMResponse responseMTOM = consultation
                  .consultationMTOM(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return responseMTOM;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();

            throw new ConsultationAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }
      } catch (ConsultationAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new ConsultationAxisFault(
               "ErreurInterneConsultation",
               "Une erreur interne à l'application est survenue lors de la consultation.",
               ex);
      }
   }

   @Override
   public final CopieResponse copieSecure(Copie request) throws CopieAxisFault,
         SaeAccessDeniedAxisFault, ArchiveInexistanteEx,
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
         String prefixeTrc = "Opération copieSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            CopieResponse response = copieService.copie(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new CopieAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }
      } catch (CopieAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new CopieAxisFault(
               "ErreurInterneCopie",
               "Une erreur interne à l'application est survenue lors de la copie.",
               ex);
      }
   }

   @Override
   public final DocumentExistantResponse documentExistant(
         DocumentExistant request) throws DocumentExistantAxisFault,
         SearchingServiceEx, ConnectionServiceEx {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération copieSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            DocumentExistantResponse response = documentExistantService
                  .documentExistant(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new DocumentExistantAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }
      } catch (DocumentExistantAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new DocumentExistantAxisFault(
               "ErreurInterneCopie",
               "Une erreur interne à l'application est survenue lors de la vérification d'un document.",
               ex);
      }
   }

   private void logSoapFault(AxisFault fault) {
      LOG.warn("Une exception AxisFault a été levée", fault);
   }

   private void logRuntimeException(RuntimeException exception) {
      LOG.warn("Une exception RuntimeException a été levée", exception);
   }

   /**
    * Methode qui set le code de la reponse HTTP à 412<br>
    * si DFCE is down.
    * 
    * 
    */
   private void setCodeHttp412() {
      HttpServletResponse response = (HttpServletResponse) MessageContext
            .getCurrentMessageContext().getProperty(
                  HTTPConstants.MC_HTTP_SERVLETRESPONSE);

      if (response != null) {
         response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);

         try {
            // on force le status a 412
            response.flushBuffer();

         } catch (IOException e) {
            throw new RuntimeException(e);
         }
      }
   }

   /**
    * {@inheritDoc}
    * 
    * @throws CaptureAxisFault
    * @throws SaeAccessDeniedAxisFault
    */
   @Override
   public final ArchivageMasseAvecHashResponse archivageMasseAvecHashSecure(
         ArchivageMasseAvecHash request, String callerIP)
         throws CaptureAxisFault, SaeAccessDeniedAxisFault {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération archivageMasseAvecHashSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         // l'opération web service n'interagit pas avec DFCE
         // il n'est pas nécessaire de vérifier si DFCE est Up
         ArchivageMasseAvecHashResponse response = captureMasse
               .archivageEnMasseAvecHash(request, callerIP);

         // Traces debug - sortie méthode
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode

         return response;

      } catch (CaptureAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new CaptureAxisFault(
               "ErreurInterneCapture",
               "Une erreur interne à l'application est survenue lors de la capture.",
               ex);
      }
   }

   @Override
   public final ModificationResponse modificationSecure(Modification request)
         throws AxisFault {

      try {

         String trcPrefix = "modificationSecure";
         LOG.debug("{} - début", trcPrefix);

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            ModificationResponse response = modificationService
                  .modification(request);

            LOG.debug("{} - fin", trcPrefix);

            return response;

         } else {

            LOG.debug("{} - Sortie", trcPrefix);
            setCodeHttp412();
            throw new ModificationAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
         }

      } catch (ModificationAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException ex) {
         throw new SaeAccessDeniedAxisFault(ex);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new ModificationAxisFault(
               "ErreurInterneModification",
               "Une erreur interne à l'application est survenue lors de la modification",
               ex);
      }
   }

   @Override
   public final SuppressionResponse suppressionSecure(Suppression request)
         throws AxisFault {

      try {

         String trcPrefix = "suppressionSecure";
         LOG.debug("{} - début", trcPrefix);

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            SuppressionResponse response = suppressionService
                  .suppression(request);

            LOG.debug("{} - fin", trcPrefix);

            return response;

         } else {

            LOG.debug("{} - Sortie", trcPrefix);
            setCodeHttp412();
            throw new SuppressionAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
         }

      } catch (SuppressionAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException ex) {
         throw new SaeAccessDeniedAxisFault(ex);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new SuppressionAxisFault(
               "ErreurInterneSuppression",
               "Une erreur interne à l'application est survenue lors de la suppression",
               ex);
      }

   }

   @Override
   public final RecuperationMetadonneesResponse recuperationMetadonneesSecure(
         RecuperationMetadonnees request) throws AxisFault {

      try {

         String trcPrefix = "recuperationMetadonneesSecure";
         LOG.debug("{} - début", trcPrefix);

         RecuperationMetadonneesResponse response = metadataService
               .recupererMetadonnees();

         LOG.debug("{} - fin", trcPrefix);

         return response;

      } catch (ErreurInterneAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException ex) {
         throw new SaeAccessDeniedAxisFault(ex);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new ErreurInterneAxisFault(ex);
      }
   }

   @Override
   public TransfertResponse transfertSecure(Transfert request) throws AxisFault {

      try {

         String trcPrefix = "transfertSecure";
         LOG.debug("{} - début", trcPrefix);

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            // -- Transfert du document
            TransfertResponse response = transfertService.transfert(request);

            LOG.debug("{} - fin", trcPrefix);

            return response;

         } else {

            LOG.debug("{} - Sortie", trcPrefix);
            setCodeHttp412();
            throw new TransfertAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
         }

      } catch (TransfertAxisFault e) {
         logSoapFault(e);
         throw e;
      } catch (AccessDeniedException ex) {
         throw new SaeAccessDeniedAxisFault(ex);
      } catch (RuntimeException e) {
         logRuntimeException(e);
         String erreur = "Une erreur interne à l'application est survenue lors du transfert";
         throw new TransfertAxisFault("ErreurInterneTransfert", erreur, e);
      }
   }

   @Override
   public final ConsultationAffichableResponse consultationAffichableSecure(
         ConsultationAffichable request) throws AxisFault {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération consultationAffichableSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            ConsultationAffichableResponse response = consultation
                  .consultationAffichable(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new ConsultationAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }
      } catch (ConsultationAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new ConsultationAxisFault(
               "ErreurInterneConsultation",
               "Une erreur interne à l'application est survenue lors de la consultation.",
               ex);
      }
   }

   @Override
   public RechercheNbResResponse rechercheNbResSecure(RechercheNbRes request)
         throws AxisFault {
      try {
         // -- Traces debug - entrée méthode
         String prefixeTrc = "Opération rechercheNbResSecure()";
         LOG.debug("{} - Début", prefixeTrc);

         if (dfceInfoService.isDfceUp()) {
            RechercheNbResResponse response = search.searchWithNbRes(request);
            // -- Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);

            return response;
         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new RechercheAxis2Fault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));
         }
      } catch (RechercheAxis2Fault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new RechercheAxis2Fault(
               "ErreurInterneRecherche",
               "Une erreur interne à l'application est survenue lors de la recherche.",
               ex);
      }
   }

   @Override
   public RechercheParIterateurResponse rechercheParIterateurSecure(
         RechercheParIterateur request) throws AxisFault {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération rechercheParIterateurSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            RechercheParIterateurResponse response = search
                  .rechercheParIterateur(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new RechercheAxis2Fault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }
      } catch (RechercheAxis2Fault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new RechercheAxis2Fault(
               "ErreurInterneRecherche",
               "Une erreur interne à l'application est survenue lors de la recherche.",
               ex);
      }
   }

   @Override
   public AjoutNoteResponse ajoutNoteSecure(AjoutNote request) throws AxisFault {

      try {
         // Traces debug - entrée méthode
         String prefixeTrc = "Opération ajoutNoteSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            AjoutNoteResponse response = noteService.ajoutNote(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new AjoutNoteAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }

      } catch (AjoutNoteAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new AjoutNoteAxisFault(
               "ErreurInterneAjoutNote",
               "Une erreur interne à l'application est survenue lors de l'ajout d'une note.",
               ex);
      }

   }

   @Override
   public StockageUnitaireResponse stockageUnitaireSecure(
         StockageUnitaire request) throws AxisFault {
      try {
         // Traces debug - entrée méthode
         String prefixeTrc = "Opération stockageUnitaireSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            StockageUnitaireResponse response = capture
                  .stockageUnitaire(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new CaptureAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }

      } catch (CaptureAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new CaptureAxisFault(
               "ErreurInterneCapture",
               "Une erreur interne à l'application est survenue lors de la capture.",
               ex);
      }
   }

   @Override
   public GetDocFormatOrigineResponse getDocFormatOrigineSecure(
         GetDocFormatOrigine request) throws AxisFault {
      try {
         // Traces debug - entrée méthode
         String prefixeTrc = "Opération getDocFormatOrigineSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            GetDocFormatOrigineResponse response = documentAttacheService
                  .getDocFormatOrigine(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new GetDocFormatOrigineAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }

      } catch (GetDocFormatOrigineAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new GetDocFormatOrigineAxisFault(
               "ErreurInterneGetDocFormatOrigine",
               "Une erreur interne à l'application est survenue lors de la récupération du document au format d'origine.",
               ex);
      }
   }

   @Override
   public RestoreMasseResponse restoreMasseSecure(RestoreMasse request,
         String callerIP) throws AxisFault {

      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération restoreMasseSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         // l'opération web service n'interagit pas avec DFCE
         // il n'est pas nécessaire de vérifier si DFCE est Up
         RestoreMasseResponse response = restoreMasse.restoreEnMasse(request,
               callerIP);

         // Traces debug - sortie méthode
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode

         return response;

      } catch (RestoreAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new RestoreAxisFault(
               "ErreurInterneRestore",
               "Une erreur interne à l'application est survenue lors de la restore.",
               ex);
      }

   }

   @Override
   public SuppressionMasseResponse suppressionMasseSecure(
         SuppressionMasse request, String callerIP) throws AxisFault {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération suppressionMasseSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         // l'opération web service n'interagit pas avec DFCE
         // il n'est pas nécessaire de vérifier si DFCE est Up
         SuppressionMasseResponse response = suppressionMasse
               .suppressionEnMasse(request, callerIP);

         // Traces debug - sortie méthode
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode

         return response;

      } catch (SuppressionAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new SuppressionAxisFault(
               "ErreurInterneSuppression",
               "Une erreur interne à l'application est survenue lors de la suppression.",
               ex);
      }
   }

   @Override
   public EtatTraitementsMasseResponse etatTraitementsMasse(
         EtatTraitementsMasse request, String callerIP) throws AxisFault {

      try {
         // Traces debug - entrée méthode
         String prefixeTrc = "Opération etatTraitementsMasse()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         // l'opération web service n'interagit pas avec DFCE
         // il n'est pas nécessaire de vérifier si DFCE est Up
         EtatTraitementsMasseResponse response = etatJobMasseService
               .etatJobMasse(request, callerIP);

         // Traces debug - sortie méthode
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode

         return response;

      } catch (EtatTraitementsMasseAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new EtatTraitementsMasseAxisFault(
               "ErreurInterneEtatTraitementsMasse",
               "Une erreur interne à l'application est survenue lors de la récupération des états des traitements de masse.",
               ex);
      }

   }

   @Override
   public ConsultationGNTGNSResponse consultationGNTGNSSecure(
         ConsultationGNTGNS request) throws SearchingServiceEx,
         ConnectionServiceEx, SAEConsultationServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         SAEConsultationAffichableParametrageException, RemoteException {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération copieSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            ConsultationGNTGNSResponse response = consultation
                  .consultationGNTGNS(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new ConsultationAxisFault(STOCKAGE_INDISPO,
                  wsMessageRessourcesUtils.recupererMessage(MES_STOCKAGE, null));

         }
      } catch (ConsultationAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      }
   }

   @Override
   public ModificationMasseResponse modificationMasseSecure(
         ModificationMasse request, String callerIP) throws AxisFault {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération archivageMasseAvecHashSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         // l'opération web service n'interagit pas avec DFCE
         // il n'est pas nécessaire de vérifier si DFCE est Up
         ModificationMasseResponse response = modificationMasse
               .modificationMasse(request, callerIP);

         // Traces debug - sortie méthode
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode

         return response;

      } catch (ModificationAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new CaptureAxisFault(
               "ErreurInterneCapture",
               "Une erreur interne à l'application est survenue lors de la capture.",
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
   public final TransfertMasseResponse transfertMasseSecure(
         TransfertMasse request, String callerIP) throws TransfertAxisFault,
         SaeAccessDeniedAxisFault {
      try {
         String prefixeTrc = "Opération transfertMasseSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         TransfertMasseResponse response = transfertMasse.transfertEnMasse(
               request, callerIP);
         // Traces debug - sortie méthode
         LOG.debug("{} - Sortie", prefixeTrc);
         return response;
      } catch (TransfertAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new TransfertAxisFault(
               "ErreurInterneTransfert",
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
   public final DeblocageResponse deblocageSecure(Deblocage request,
         String callerIP) throws DeblocageAxisFault, SaeAccessDeniedAxisFault,
         JobInexistantException {
      try {
         String prefixeTrc = "Opération deblocageSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         DeblocageResponse response = deblocageService.deblocage(request,
               callerIP);
         // Traces debug - sortie méthode
         LOG.debug("{} - Sortie", prefixeTrc);
         return response;
      } catch (JobInexistantException ex) {
         ex.printStackTrace();
         LOG.warn("échec de déblocage: Job inexistant");
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new DeblocageAxisFault(
               "ErreurInterneDeblocage",
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
   public RepriseResponse repriseSecure(Reprise request, String callerIP)
         throws AxisFault, JobInexistantException {
      // TODO 
      try {
         String prefixeTrc = "Opération repriseSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         RepriseResponse response = repriseService.reprise(request,
               callerIP);
         // Traces debug - sortie méthode
         LOG.debug("{} - Sortie", prefixeTrc);
         return response;
      } catch (JobInexistantException ex) {
         ex.printStackTrace();
         LOG.warn("échec de reprise du job: Job inexistant");
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new RepriseAxisFault(
               "ErreurInterneReprise",
               "Une erreur interne à l'application est survenue lors de la reprise du job.",
               ex);
      }
   }

}