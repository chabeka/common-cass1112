package fr.urssaf.image.sae.webservices.service.impl;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.EcdeUrlSommaireType;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.webservices.aspect.BuildOrClearMDCAspect;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;
import fr.urssaf.image.sae.webservices.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.webservices.impl.factory.ObjectStorageResponseFactory;
import fr.urssaf.image.sae.webservices.service.WSCaptureMasseService;
import fr.urssaf.image.sae.webservices.service.support.LauncherSupport;
import fr.urssaf.image.sae.webservices.util.MessageRessourcesUtils;

/**
 * Implémentation de {@link WSCaptureMasseService}<br>
 * L'implémentation est annotée par {@link Service}
 * 
 */
@Service
public class WSCaptureMasseServiceImpl implements WSCaptureMasseService {

   private static final Logger LOG = LoggerFactory
         .getLogger(WSCaptureMasseServiceImpl.class);

   @Autowired
   @Qualifier("saeControlesCaptureService")
   private SAEControlesCaptureService controlesService;

   @Autowired
   @Qualifier("captureMasseLauncher")
   private LauncherSupport captureLauncher;

   private final File saeConfigResource;

   /**
    * Le fichier de configuration générale est transmis à chaque lancement d'un
    * traitement de capture en masse
    * 
    * @param saeConfigResource
    *           fichier de configuration générale du SAE
    */
   @Autowired
   public WSCaptureMasseServiceImpl(Resource saeConfigResource) {

      try {
         this.saeConfigResource = saeConfigResource.getFile();
      } catch (IOException e) {

         throw new CaptureMasseRuntimeException(
               "Erreur lors de la lecture du fichier de configuration du SAE.",
               e);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ArchivageMasseResponse archivageEnMasse(ArchivageMasse request)
         throws CaptureAxisFault {

      String prefixeTrc = "archivageEnMasse()";

      synchronized (this) {

         if (captureLauncher.isLaunched()) {

            // ici on retourne un status 412 pour informer que le serveur est
            // occupé
            LOG.debug("{} - {}", prefixeTrc, MessageRessourcesUtils
                  .recupererMessage("ws.bulk.capture.is.busy", null));
            HttpServletResponse resp = (HttpServletResponse) MessageContext
                  .getCurrentMessageContext().getProperty(
                        HTTPConstants.MC_HTTP_SERVLETRESPONSE);

            if (resp != null) {
               resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
               throw new CaptureAxisFault("CaptureMasseRefusee",
                     MessageRessourcesUtils.recupererMessage(
                           "ws.bulk.capture.is.busy", null));
            }

         }
      }

      LOG.debug("{} - Début", prefixeTrc);

      EcdeUrlSommaireType ecdeUrlWs = request.getArchivageMasse()
            .getUrlSommaire();
      String ecdeUrl = ecdeUrlWs.getEcdeUrlSommaireType().toString();
      LOG.debug("{} - URI ECDE: {}", prefixeTrc, ecdeUrl);

      // vérification de l'URL ECDE du sommaire contenu dans la requête SOAP
      try {

         controlesService.checkBulkCaptureEcdeUrl(ecdeUrl);

      } catch (CaptureBadEcdeUrlEx e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (CaptureEcdeUrlFileNotFoundEx e) {
         throw new CaptureAxisFault("CaptureUrlEcdeFichierIntrouvable", e
               .getMessage(), e);
      } catch (CaptureEcdeWriteFileEx e) {
         throw new CaptureAxisFault("CaptureEcdeDroitEcriture", e.getMessage(),
               e);
      }

      // Appel du service, celui-ci doit rendre la main rapidement d'un
      // processus
      String contextLog = MDC.get(BuildOrClearMDCAspect.LOG_CONTEXTE);

      // les trois arguments sont dans l'ordre
      // 1 - le nom du traitement : captureMasse
      // 2 - URL ECDE du sommaire.xml
      // 3 - Le chemin complet du fichier de configuration globale du SAE
      // 4 - UUID du contexte LOGBACK en cours
      LOG.debug("{} - UUID du contexte LOGBACK en cours: {}", prefixeTrc,
            contextLog);
      LOG.debug("{} - Fichier configuration globale du SAE: {}", prefixeTrc,
            saeConfigResource.getAbsolutePath());
      captureLauncher.launch("captureMasse", ecdeUrl, saeConfigResource
            .getAbsolutePath(), contextLog);

      // On prend acte de la demande,
      // le retour se fera via le fichier resultats.xml de l'ECDE
      return ObjectStorageResponseFactory.createArchivageMasseResponse();

   }

}
