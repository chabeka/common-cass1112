package fr.urssaf.image.sae.webservices.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.XmlReadUtils;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.webservices.aspect.BuildOrClearMDCAspect;

/**
 * Cette classe permet d'externaliser certains traitement commun aux traitements de masse.
 */
@Component
public final class WsTraitementMasseCommonsUtils {

   private static final Logger LOG = LoggerFactory
         .getLogger(WsTraitementMasseCommonsUtils.class);

   @Autowired
   @Qualifier("saeControlesCaptureService")
   private SAEControlesCaptureService controlesService;

   @Autowired
   private EcdeServices ecdeServices;

   @Autowired
   private WsMessageRessourcesUtils wsMessageRessourcesUtils;

   public UUID checkEcdeUrl(String ecdeUrl) throws CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, CaptureEcdeWriteFileEx {
      // vérification de l'URL ECDE du sommaire contenu dans la requête SOAP
      controlesService.checkBulkCaptureEcdeUrl(ecdeUrl);


      // Appel du service, celui-ci doit rendre la main rapidement d'un
      // processus
      String contextLog = MDC.get(BuildOrClearMDCAspect.LOG_CONTEXTE);
      // récupération de l'UUID du traitement
      UUID uuid = UUID.fromString(contextLog);

      return uuid;

   }

   public Integer getNombreDoc(String ecdeUrl) throws EcdeBadURLException,
         EcdeBadURLFormatException, URISyntaxException,
         ValidationExceptionInvalidFile {
      File sommaire;
      URI uri = new URI(ecdeUrl);
      sommaire = ecdeServices.convertSommaireToFile(uri);

      Integer nombreDoc = null;
      int nombreDocuments = 0;
      int nombreComposants = 0;

      try {
         nombreDocuments = XmlReadUtils.compterElements(sommaire, "document");
         nombreComposants = XmlReadUtils.compterElements(sommaire, "composant");

         if ((nombreDocuments + nombreComposants == 0)
               || (nombreComposants > 0 && nombreDocuments > 0)) {
            String message = wsMessageRessourcesUtils.recupererMessage(
                  "capture.masse.sommaire.format.incorrect", null);
            throw new ValidationExceptionInvalidFile(message);
         } else if (nombreDocuments > 0) {
            nombreDoc = nombreDocuments;
         } else {
            nombreDoc = nombreComposants;
         }

      } catch (CaptureMasseRuntimeException e) {
         LOG.warn("impossible d'ouvrir le fichier attendu", e);
      }
      return nombreDoc;
   }

}