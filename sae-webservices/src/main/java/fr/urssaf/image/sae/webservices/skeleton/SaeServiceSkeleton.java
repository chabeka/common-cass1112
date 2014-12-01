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
import fr.cirtil.www.saeservice.ConsultationMTOM;
import fr.cirtil.www.saeservice.ConsultationMTOMResponse;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.Modification;
import fr.cirtil.www.saeservice.ModificationResponse;
import fr.cirtil.www.saeservice.PingRequest;
import fr.cirtil.www.saeservice.PingResponse;
import fr.cirtil.www.saeservice.PingSecureRequest;
import fr.cirtil.www.saeservice.PingSecureResponse;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheNbResResponse;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.cirtil.www.saeservice.RecuperationMetadonnees;
import fr.cirtil.www.saeservice.RecuperationMetadonneesResponse;
import fr.cirtil.www.saeservice.Suppression;
import fr.cirtil.www.saeservice.SuppressionResponse;
import fr.cirtil.www.saeservice.Transfert;
import fr.cirtil.www.saeservice.TransfertResponse;
import fr.urssaf.image.sae.exploitation.service.DfceInfoService;
import fr.urssaf.image.sae.webservices.SaeService;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;
import fr.urssaf.image.sae.webservices.exception.ConsultationAxisFault;
import fr.urssaf.image.sae.webservices.exception.ErreurInterneAxisFault;
import fr.urssaf.image.sae.webservices.exception.ModificationAxisFault;
import fr.urssaf.image.sae.webservices.exception.RechercheAxis2Fault;
import fr.urssaf.image.sae.webservices.exception.SuppressionAxisFault;
import fr.urssaf.image.sae.webservices.exception.TransfertAxisFault;
import fr.urssaf.image.sae.webservices.security.exception.SaeAccessDeniedAxisFault;
import fr.urssaf.image.sae.webservices.service.WSCaptureMasseService;
import fr.urssaf.image.sae.webservices.service.WSCaptureService;
import fr.urssaf.image.sae.webservices.service.WSConsultationService;
import fr.urssaf.image.sae.webservices.service.WSMetadataService;
import fr.urssaf.image.sae.webservices.service.WSModificationService;
import fr.urssaf.image.sae.webservices.service.WSRechercheService;
import fr.urssaf.image.sae.webservices.service.WSSuppressionService;
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
   private WSModificationService modificationService;

   @Autowired
   private WSSuppressionService suppressionService;
   
   @Autowired
   private WSTransfertService transfertService;

   @Autowired
   private WSMetadataService metadataService;

   @Autowired
   private DfceInfoService dfceInfoService;

   @Autowired
   private WsMessageRessourcesUtils wsMessageRessourcesUtils;

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
               "Une erreur interne à l'application est survenue lors de la recherche.",
               "ErreurInterneRecherche", ex);
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
            throw new ConsultationAxisFault(wsMessageRessourcesUtils
                  .recupererMessage(MES_STOCKAGE, null), STOCKAGE_INDISPO);

         }
      } catch (ConsultationAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new ConsultationAxisFault(
               "Une erreur interne à l'application est survenue lors de la consultation.",
               "ErreurInterneConsultation", ex);
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
            throw new ConsultationAxisFault(wsMessageRessourcesUtils
                  .recupererMessage(MES_STOCKAGE, null), STOCKAGE_INDISPO);

         }
      } catch (ConsultationAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new ConsultationAxisFault(
               "Une erreur interne à l'application est survenue lors de la consultation.",
               "ErreurInterneConsultation", ex);
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

         ModificationResponse response = modificationService
               .modification(request);

         LOG.debug("{} - fin", trcPrefix);

         return response;

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

         SuppressionResponse response = suppressionService.suppression(request);

         LOG.debug("{} - fin", trcPrefix);

         return response;

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

         //-- Transfert du document 
         TransfertResponse response = transfertService.transfert(request);

         LOG.debug("{} - fin", trcPrefix);

         return response;

      } catch (TransfertAxisFault e) {
         logSoapFault(e);
         throw e;
      }  catch (AccessDeniedException ex) {
         throw new SaeAccessDeniedAxisFault(ex);
      } catch (RuntimeException e) {
         logRuntimeException(e);
         String erreur = "Une erreur interne à l'application est survenue lors du transfert";
         throw new TransfertAxisFault("ErreurInterneTransfert", erreur, e);
      }
   }
   
   @Override
   public final ConsultationAffichableResponse consultationAffichableSecure(ConsultationAffichable request) throws AxisFault {
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "Opération consultationAffichableSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         boolean dfceUp = dfceInfoService.isDfceUp();
         if (dfceUp) {

            ConsultationAffichableResponse response = consultation.consultationAffichable(request);

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            return response;

         } else {

            LOG.debug("{} - Sortie", prefixeTrc);
            setCodeHttp412();
            throw new ConsultationAxisFault(wsMessageRessourcesUtils
                  .recupererMessage(MES_STOCKAGE, null), STOCKAGE_INDISPO);

         }
      } catch (ConsultationAxisFault ex) {
         logSoapFault(ex);
         throw ex;
      } catch (AccessDeniedException exception) {
         throw new SaeAccessDeniedAxisFault(exception);
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new ConsultationAxisFault(
               "Une erreur interne à l'application est survenue lors de la consultation.",
               "ErreurInterneConsultation", ex);
      }
   }

   @Override
   public RechercheNbResResponse rechercheNbResSecure(Recherche request)
         throws AxisFault {
      try {
         //-- Traces debug - entrée méthode
         String prefixeTrc = "Opération rechercheNbResSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         
         if(dfceInfoService.isDfceUp()){
            RechercheNbResResponse response = search.searchWithNbRes(request);
            //-- Traces debug - sortie méthode
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
      } catch (RuntimeException ex) {
         logRuntimeException(ex);
         throw new RechercheAxis2Fault(
               "Une erreur interne à l'application est survenue lors de la recherche.",
               "ErreurInterneRecherche", ex);
      }
   }
}
