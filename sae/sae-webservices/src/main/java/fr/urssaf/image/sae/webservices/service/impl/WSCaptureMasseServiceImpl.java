package fr.urssaf.image.sae.webservices.service.impl;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseAvecHash;
import fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.EcdeUrlSommaireType;
import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.capturemasse.controles.SAEControleSupportService;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireHashException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireTypeHashException;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;
import fr.urssaf.image.sae.webservices.impl.factory.ObjectStorageResponseFactory;
import fr.urssaf.image.sae.webservices.service.WSCaptureMasseService;
import fr.urssaf.image.sae.webservices.util.HostnameUtil;
import fr.urssaf.image.sae.webservices.util.WsTraitementMasseCommonsUtils;

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
   private TraitementAsynchroneService traitementService;

   @Autowired
   private EcdeServices ecdeServices;

   @Autowired
   private SAEControleSupportService controleSupport;

   @Autowired
   private WsTraitementMasseCommonsUtils wsTraitementMasseCommonsUtils;

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

      String hName = HostnameUtil.getHostname();

      UUID identifiant;
      Integer nbDoc;
      try {
         identifiant = wsTraitementMasseCommonsUtils.checkEcdeUrl(ecdeUrl);

         nbDoc = wsTraitementMasseCommonsUtils.getNombreDoc(ecdeUrl);
      } catch (EcdeBadURLException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (EcdeBadURLFormatException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (URISyntaxException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (CaptureBadEcdeUrlEx e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (CaptureEcdeUrlFileNotFoundEx e) {
         throw new CaptureAxisFault("CaptureUrlEcdeFichierIntrouvable",
               e.getMessage(), e);
      } catch (CaptureEcdeWriteFileEx e) {
         throw new CaptureAxisFault("CaptureEcdeDroitEcriture", e.getMessage(),
               e);
      } catch (ValidationExceptionInvalidFile e) {
         throw new CaptureAxisFault("FormatSommaireIncorrect", e.getMessage(),
               e);
      }

      VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
            .getContext().getAuthentication().getPrincipal();

      HashMap<String, String> params = new HashMap<String, String>();
      params.put(Constantes.ECDE_URL, ecdeUrl);
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            params, identifiant, TYPES_JOB.capture_masse,
            hName, callerIP, nbDoc, extrait);

      // appel de la méthode d'insertion du job dans la pile des travaux
      traitementService.ajouterJobCaptureMasse(parametres);

      // On prend acte de la demande,
      // le retour se fera via le fichier resultats.xml de l'ECDE
      return ObjectStorageResponseFactory.createArchivageMasseResponse();

   }

   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public final ArchivageMasseAvecHashResponse archivageEnMasseAvecHash(
         ArchivageMasseAvecHash request, String callerIP)
         throws CaptureAxisFault {

      String prefixeTrc = "archivageEnMasseAvecHash()";

      LOG.debug("{} - Début", prefixeTrc);

      EcdeUrlSommaireType ecdeUrlWs = request.getArchivageMasseAvecHash()
            .getUrlSommaire();
      String ecdeUrl = ecdeUrlWs.getEcdeUrlSommaireType().toString();
      String hash = request.getArchivageMasseAvecHash().getHash();
      String typeHash = request.getArchivageMasseAvecHash().getTypeHash();

      LOG.debug("{} - URI ECDE: {}", prefixeTrc, ecdeUrl);
      LOG.debug("{} - Hash: {}", prefixeTrc, hash);
      LOG.debug("{} - Type hash: {}", prefixeTrc, typeHash);

      File fileEcde;
      try {
         fileEcde = ecdeServices.convertSommaireToFile(new URI(ecdeUrl));
         LOG.debug("Contrôle de cohérence du hash du fichier sommaire.xml par rapport au hash transmis");

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
      } catch (CaptureMasseSommaireHashException e) {
         throw new CaptureAxisFault("HashSommaireIncorrect", e.getMessage(), e);
      } catch (CaptureMasseSommaireTypeHashException e) {
         throw new CaptureAxisFault("TypeHashSommaireIncorrect",
               e.getMessage(), e);
      } catch (CaptureMasseRuntimeException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeFichierIntrouvable",
               e.getMessage(), e);
      }

      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put(Constantes.ECDE_URL, ecdeUrl);
      jobParam.put(Constantes.HASH, hash);
      jobParam.put(Constantes.TYPE_HASH, typeHash);

      String hName = HostnameUtil.getHostname();

      UUID uuid;
      Integer nbDoc;
      try {
         uuid = wsTraitementMasseCommonsUtils.checkEcdeUrl(ecdeUrl);
         
         nbDoc = wsTraitementMasseCommonsUtils.getNombreDoc(ecdeUrl);
      } catch (EcdeBadURLException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (EcdeBadURLFormatException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (URISyntaxException e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (CaptureBadEcdeUrlEx e) {
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
      } catch (CaptureEcdeUrlFileNotFoundEx e) {
         throw new CaptureAxisFault("CaptureUrlEcdeFichierIntrouvable",
               e.getMessage(), e);
      } catch (CaptureEcdeWriteFileEx e) {
         throw new CaptureAxisFault("CaptureEcdeDroitEcriture", e.getMessage(),
               e);
      } catch (ValidationExceptionInvalidFile e) {
         throw new CaptureAxisFault("FormatSommaireIncorrect", e.getMessage(),
               e);
      }


      VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
            .getContext().getAuthentication().getPrincipal();

      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParam, uuid, TYPES_JOB.capture_masse, hName,
            callerIP, nbDoc, extrait);

      // appel de la méthode d'insertion du job dans la pile des travaux
      traitementService.ajouterJobCaptureMasse(parametres);

      // On prend acte de la demande,
      // le retour se fera via le fichier resultats.xml de l'ECDE
      return ObjectStorageResponseFactory
            .createArchivageMasseAvecHashResponse(uuid.toString());
   }

}
