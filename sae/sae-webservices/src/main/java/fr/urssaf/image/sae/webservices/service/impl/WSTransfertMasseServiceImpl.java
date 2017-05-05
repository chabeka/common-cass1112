/**
 * 
 */
package fr.urssaf.image.sae.webservices.service.impl;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.EcdeUrlSommaireType;
import fr.cirtil.www.saeservice.TransfertMasse;
import fr.cirtil.www.saeservice.TransfertMasseResponse;
import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.capturemasse.controles.SAEControleSupportService;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireHashException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireTypeHashException;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.XmlReadUtils;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.webservices.aspect.BuildOrClearMDCAspect;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;
import fr.urssaf.image.sae.webservices.exception.TransfertAxisFault;
import fr.urssaf.image.sae.webservices.impl.factory.ObjectStorageResponseFactory;
import fr.urssaf.image.sae.webservices.service.WSTransfertMasseService;
import fr.urssaf.image.sae.webservices.util.HostnameUtil;
import fr.urssaf.image.sae.webservices.util.WsMessageRessourcesUtils;

/**
 * Classe d'implémentation de l'interface {@link WSTransfertMasseService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC et l'annotation @Autowired.
 * 
 */
@Service
public class WSTransfertMasseServiceImpl implements WSTransfertMasseService {
      
   private static final Logger LOG = LoggerFactory
         .getLogger(WSTransfertMasseServiceImpl.class);
   
   @Autowired
   private EcdeServices ecdeServices;
   
   @Autowired
   private SAEControleSupportService controleSupport;
   
   @Autowired
   @Qualifier("saeControlesCaptureService")
   private SAEControlesCaptureService controlesService;

   @Autowired
   private TraitementAsynchroneService traitementService;
   
   @Autowired
   private WsMessageRessourcesUtils wsMessageRessourcesUtils;


   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public final TransfertMasseResponse transfertEnMasse(
         TransfertMasse request, String callerIP)
         throws TransfertAxisFault {

      String prefixeTrc = "transfertEnMasse()";

      LOG.debug("{} - Début", prefixeTrc);

      EcdeUrlSommaireType ecdeUrlWs = request.getTransfertMasse()
            .getUrlSommaire();
      String ecdeUrl = ecdeUrlWs.getEcdeUrlSommaireType().toString();
      String hash = request.getTransfertMasse().getHash();
      String typeHash = request.getTransfertMasse().getTypeHash();

      LOG.debug("{} - URL ECDE: {}", prefixeTrc, ecdeUrl);
      LOG.debug("{} - Hash: {}", prefixeTrc, hash);
      LOG.debug("{} - Type hash: {}", prefixeTrc, typeHash);

      File fileEcde;
      try {
         fileEcde = ecdeServices.convertSommaireToFile(new URI(ecdeUrl));
         LOG.debug("Contrôle de cohérence du hash du fichier sommaire.xml par rapport au hash transmis");

         controleSupport.checkHash(fileEcde, hash, typeHash);
      } catch (EcdeBadURLException e) {
         throw new TransfertAxisFault("TransfertMasseUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (EcdeBadURLFormatException e) {
         throw new TransfertAxisFault("TransfertMasseUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (URISyntaxException e) {
         throw new TransfertAxisFault("TransfertMasseUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (CaptureMasseSommaireHashException e) {
         throw new TransfertAxisFault("HashSommaireIncorrect", e.getMessage(), e);
      } catch (CaptureMasseSommaireTypeHashException e) {
         throw new TransfertAxisFault("TypeHashSommaireIncorrect",
               e.getMessage(), e);
      } catch (CaptureMasseRuntimeException e) {
         throw new TransfertAxisFault("TransfertMasseUrlEcdeFichierIntrouvable",
               e.getMessage(), e);
      }

      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put(Constantes.ECDE_URL, ecdeUrl);
      jobParam.put(Constantes.HASH, hash);
      jobParam.put(Constantes.TYPE_HASH, typeHash);

      String hName = HostnameUtil.getHostname();

      UUID uuid = checkEcdeUrl(ecdeUrl);

      Integer nbDoc = getNombreDoc(ecdeUrl);

      VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
            .getContext().getAuthentication().getPrincipal();
      
      // Paramètres de traitements de transfert en masse
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParam, uuid, TYPES_JOB.transfert_masse, hName,
            callerIP, nbDoc, extrait);

      // appel de la méthode d'insertion du job de transfert en masse de documents dans la pile des travaux
      traitementService.ajouterJobTransfertMasse(parametres);

      // On prend acte de la demande,
      // le retour se fera via le fichier resultats.xml de l'ECDE
      return ObjectStorageResponseFactory
            .createTransfertMasseResponse(uuid.toString());
   }
   
   /**
    * Vérification de l'URL ECDE du sommaire contenu dans la requête SOAP
    * @param ecdeUrl
    * @return
    * @throws CaptureAxisFault
    */
   private UUID checkEcdeUrl(String ecdeUrl) throws TransfertAxisFault {
      // 
      try {
         controlesService.checkBulkCaptureEcdeUrl(ecdeUrl);
      } catch (CaptureBadEcdeUrlEx e) {
         throw new TransfertAxisFault("TransfertUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (CaptureEcdeUrlFileNotFoundEx e) {
         throw new TransfertAxisFault("TransfertUrlEcdeFichierIntrouvable",
               e.getMessage(), e);
      } catch (CaptureEcdeWriteFileEx e) {
         throw new TransfertAxisFault("TransfertEcdeDroitEcriture", e.getMessage(),
               e);
      }

      // Appel du service, celui-ci doit rendre la main rapidement d'un
      // processus
      String contextLog = MDC.get(BuildOrClearMDCAspect.LOG_CONTEXTE);
      // récupération de l'UUID du traitement
      UUID uuid = UUID.fromString(contextLog);

      return uuid;

   }

   private Integer getNombreDoc(String ecdeUrl) throws TransfertAxisFault {
      File sommaire;
      try {
         URI uri = new URI(ecdeUrl);
         sommaire = ecdeServices.convertSommaireToFile(uri);
      } catch (EcdeBadURLException e) {
         throw new TransfertAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (EcdeBadURLFormatException e) {
         throw new TransfertAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (URISyntaxException e) {
         throw new TransfertAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      }

      Integer nombreDoc = null;
      int nombreDocuments = 0;
      int nombreComposants = 0;

      try {
         nombreDocuments = XmlReadUtils.compterElements(sommaire, "documentMultiAction");
         nombreComposants = XmlReadUtils.compterElements(sommaire, "composant");

         if ((nombreDocuments + nombreComposants == 0)
               || (nombreComposants > 0 && nombreDocuments > 0)) {
            String message = wsMessageRessourcesUtils.recupererMessage(
                  "capture.masse.sommaire.format.incorrect", null);
            throw new TransfertAxisFault("FormatSommaireIncorrect", message);
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
