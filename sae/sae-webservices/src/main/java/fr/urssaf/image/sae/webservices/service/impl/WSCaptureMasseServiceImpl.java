package fr.urssaf.image.sae.webservices.service.impl;

import java.io.File;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
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

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseAvecHash;
import fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.EcdeUrlSommaireType;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.model.CaptureMasseParametres;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireHashException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireTypeHashException;
import fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeControleSupport;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.services.util.XmlReadUtils;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
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

   @Autowired
   private EcdeServices ecdeServices;
   
   @Autowired
   private EcdeControleSupport controleSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final ArchivageMasseResponse archivageEnMasse(ArchivageMasse request,
         String callerIP) throws CaptureAxisFault {

      String prefixeTrc = "archivageEnMasse()";

      LOG.debug("{} - Début", prefixeTrc);

      EcdeUrlSommaireType ecdeUrlWs = request.getArchivageMasse()
            .getUrlSommaire();
      String ecdeUrl = ecdeUrlWs.getEcdeUrlSommaireType().toString();
      LOG.debug("{} - URI ECDE: {}", prefixeTrc, ecdeUrl);

      String hName = getHostName();
      
      UUID id= checkEcdeUrl(ecdeUrl);
      
      Integer nbDoc = getNombreDoc(ecdeUrl);
      
      VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      
      CaptureMasseParametres parametres = new CaptureMasseParametres(ecdeUrl,
            id, hName, callerIP, nbDoc, extrait);

      // appel de la méthode d'insertion du job dans la pile des travaux
      traitementService.ajouterJobCaptureMasse(parametres);

      // On prend acte de la demande,
      // le retour se fera via le fichier resultats.xml de l'ECDE
      return ObjectStorageResponseFactory.createArchivageMasseResponse();

   }

   @Override
   public ArchivageMasseAvecHashResponse archivageEnMasseAvecHash(
         ArchivageMasseAvecHash request, String callerIP)
         throws CaptureAxisFault {
      
      String prefixeTrc = "ArchivageMasseAvecHashResponse()";

      LOG.debug("{} - Début", prefixeTrc);

      EcdeUrlSommaireType ecdeUrlWs = request.getArchivageMasseAvecHash()
            .getUrlSommaire();
      String ecdeUrl = ecdeUrlWs.getEcdeUrlSommaireType().toString();
      String hash = request.getArchivageMasseAvecHash().getHash();
      String typeHash = request.getArchivageMasseAvecHash().getTypeHash();
      
      LOG.debug("{} - URI ECDE: {}", prefixeTrc, ecdeUrl);
      LOG.debug("{} - HASH: {}", prefixeTrc, hash);
      LOG.debug("{} - TYPE HASH: {}", prefixeTrc, typeHash);
      
      File fileEcde;
      try {
         fileEcde = ecdeServices
         .convertSommaireToFile(new URI(ecdeUrl));
         controleSupport.checkHash(fileEcde, hash, typeHash);
      } catch (EcdeBadURLException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (EcdeBadURLFormatException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (URISyntaxException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      }catch (CaptureMasseSommaireHashException e) {
         throw new CaptureAxisFault("HashSommaireIncorrect", e.getMessage(),
               e);
      } catch (CaptureMasseSommaireTypeHashException e) {
         throw new CaptureAxisFault("TypeHashSommaireIncorrect", e.getMessage(),
               e);
      }
            
      Map<String,String> jobParam = new HashMap<String, String>();
      jobParam.put(Constantes.ECDE_URL, ecdeUrl);
      jobParam.put(Constantes.HASH, hash);
      jobParam.put(Constantes.TYPE_HASH, typeHash);

      String hName = getHostName();
      
      UUID id= checkEcdeUrl(ecdeUrl);
      
      
      Integer nbDoc = getNombreDoc(ecdeUrl);
      
      VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      

      CaptureMasseParametres parametres = new CaptureMasseParametres(jobParam,
            id, hName, callerIP, nbDoc, extrait);

      // appel de la méthode d'insertion du job dans la pile des travaux
      traitementService.ajouterJobCaptureMasse(parametres);

      // On prend acte de la demande,
      // le retour se fera via le fichier resultats.xml de l'ECDE
      return ObjectStorageResponseFactory.createArchivageMasseAvecHashResponse();
   }

   
   private UUID checkEcdeUrl(String ecdeUrl) throws CaptureAxisFault{
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
      

      return uuid;

   }
   
   private String getHostName(){
      String hostName = null;

      try {
         InetAddress address = InetAddress.getLocalHost();
         hostName = address.getHostName();

      } catch (UnknownHostException e) {
         LOG
               .warn(
                     "Impossible de récupérer les informations relatives à la machine locale",
                     e);
      }
      return hostName;
   }
   
   private Integer getNombreDoc(String ecdeUrl) throws CaptureAxisFault{
      File sommaire;
      try {
         URI uri = new URI(ecdeUrl);
         sommaire = ecdeServices.convertSommaireToFile(uri);
      } catch (EcdeBadURLException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (EcdeBadURLFormatException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (URISyntaxException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      }

      Integer nombreDoc = null;

      try {
         nombreDoc = XmlReadUtils.compterElements(sommaire, "document");
         
      } catch (CaptureMasseRuntimeException e) {
         LOG.warn("impossible d'ouvrir le fichier attendu", e);
      }
      return nombreDoc;
   }
}
