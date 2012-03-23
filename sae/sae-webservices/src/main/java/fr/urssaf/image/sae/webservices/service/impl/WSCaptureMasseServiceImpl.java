package fr.urssaf.image.sae.webservices.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.EcdeUrlSommaireType;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.webservices.aspect.BuildOrClearMDCAspect;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;
import fr.urssaf.image.sae.webservices.impl.factory.ObjectStorageResponseFactory;
import fr.urssaf.image.sae.webservices.service.WSCaptureMasseService;

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
   private TraitementAsynchroneService traitementService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final ArchivageMasseResponse archivageEnMasse(ArchivageMasse request)
         throws CaptureAxisFault {

      String prefixeTrc = "archivageEnMasse()";

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
      // récupération de l'UUID du traitement
      UUID uuid = UUID.fromString(contextLog);

      // appel de la méthode d'insertion du job dans la pile des travaux
      traitementService.ajouterJobCaptureMasse(ecdeUrl, uuid);

      // On prend acte de la demande,
      // le retour se fera via le fichier resultats.xml de l'ECDE
      return ObjectStorageResponseFactory.createArchivageMasseResponse();

   }

}
