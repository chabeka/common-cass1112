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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.EcdeUrlSommaireType;
import fr.cirtil.www.saeservice.ModificationMasse;
import fr.cirtil.www.saeservice.ModificationMasseResponse;
import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.pile.travaux.exception.JobRequestAlreadyExistsException;
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
import fr.urssaf.image.sae.webservices.exception.ModificationAxisFault;
import fr.urssaf.image.sae.webservices.impl.factory.ObjectStorageResponseFactory;
import fr.urssaf.image.sae.webservices.service.WSModificationMasseService;
import fr.urssaf.image.sae.webservices.util.HostnameUtil;
import fr.urssaf.image.sae.webservices.util.WsMessageRessourcesUtils;
import fr.urssaf.image.sae.webservices.util.WsTraitementMasseCommonsUtils;

/**
 * Classe d'implémentation de l'interface {@link WSModificationMasseService}.
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC et l'annotation @Autowired
 * 
 */
@Service
public class WSModificationMasseServiceImpl implements
WSModificationMasseService {
  private static final Logger LOG = LoggerFactory
      .getLogger(WSModificationMasseServiceImpl.class);

  @Autowired
  private TraitementAsynchroneService traitementService;

  @Autowired
  private EcdeServices ecdeServices;

  @Autowired
  private SAEControleSupportService controleSupport;

  @Autowired
  private WsTraitementMasseCommonsUtils wsTraitementMasseCommonsUtils;

  @Autowired
  private WsMessageRessourcesUtils wsMessageRessourcesUtils;

  /**
   * {@inheritDoc}
   */
  @Override
  public ModificationMasseResponse modificationMasse(
                                                     final ModificationMasse request, final String callerIP)
                                                         throws ModificationAxisFault {

    final String prefixeTrc = "archivageEnMasseAvecHash()";

    LOG.debug("{} - Début", prefixeTrc);

    final EcdeUrlSommaireType ecdeUrlWs = request.getModificationMasse()
        .getUrlSommaire();
    final String ecdeUrl = ecdeUrlWs.getEcdeUrlSommaireType().toString();
    final String hash = request.getModificationMasse().getHash();
    final String typeHash = request.getModificationMasse().getTypeHash();
    final String codeTraitement = request.getModificationMasse().getCodeTraitement();

    LOG.debug("{} - URI ECDE: {}", prefixeTrc, ecdeUrl);
    LOG.debug("{} - Hash: {}", prefixeTrc, hash);
    LOG.debug("{} - Type hash: {}", prefixeTrc, typeHash);
    LOG.debug("{} - Code traitement: {}", prefixeTrc, codeTraitement);


    if (codeTraitement == null || codeTraitement.isEmpty()) {
      throw new ModificationAxisFault("CodeTraitementManquant", wsMessageRessourcesUtils.recupererMessage(
                                                                                                          "code.traitement.sommaire.manquant", null));
    }

    File fileEcde;
    try {
      fileEcde = ecdeServices.convertSommaireToFile(new URI(ecdeUrl));
      LOG.debug("Contrôle de cohérence du hash du fichier sommaire.xml par rapport au hash transmis");

      controleSupport.checkHash(fileEcde, hash, typeHash);
    } catch (final EcdeBadURLException e) {
      throw new ModificationAxisFault("ModificationUrlEcdeIncorrecte", e.getMessage(), e);
    } catch (final EcdeBadURLFormatException e) {
      throw new ModificationAxisFault("ModificationUrlEcdeIncorrecte", e.getMessage(), e);
    } catch (final URISyntaxException e) {
      throw new ModificationAxisFault("ModificationUrlEcdeIncorrecte", e.getMessage(), e);
    } catch (final CaptureMasseSommaireHashException e) {
      throw new ModificationAxisFault("HashSommaireIncorrect", e.getMessage(), e);
    } catch (final CaptureMasseSommaireTypeHashException e) {
      throw new ModificationAxisFault("TypeHashSommaireIncorrect", e.getMessage(), e);
    } catch (final CaptureMasseRuntimeException e) {
      throw new ModificationAxisFault("ModificationUrlEcdeFichierIntrouvable", e.getMessage(), e);
    }

    final Map<String, String> jobParam = new HashMap<>();
    jobParam.put(Constantes.ECDE_URL, ecdeUrl);
    jobParam.put(Constantes.HASH, hash);
    jobParam.put(Constantes.TYPE_HASH, typeHash);
    jobParam.put(Constantes.CODE_TRAITEMENT, codeTraitement);

    final String hName = HostnameUtil.getHostname();

    UUID uuid;
    Integer nbDoc;
    try {
      uuid = wsTraitementMasseCommonsUtils.checkEcdeUrl(ecdeUrl);

      nbDoc = wsTraitementMasseCommonsUtils.getNombreDoc(ecdeUrl);
    } catch (final EcdeBadURLException e) {
      throw new ModificationAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(), e);
    } catch (final EcdeBadURLFormatException e) {
      throw new ModificationAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(), e);
    } catch (final URISyntaxException e) {
      throw new ModificationAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(), e);
    } catch (final CaptureBadEcdeUrlEx e) {
      throw new ModificationAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(), e);
    } catch (final CaptureEcdeUrlFileNotFoundEx e) {
      throw new ModificationAxisFault("CaptureUrlEcdeFichierIntrouvable",
                                      e.getMessage(), e);
    } catch (final CaptureEcdeWriteFileEx e) {
      throw new ModificationAxisFault("CaptureEcdeDroitEcriture", e.getMessage(), e);
    } catch (final ValidationExceptionInvalidFile e) {
      throw new ModificationAxisFault("FormatSommaireIncorrect", e.getMessage(),
                                      e);
    }

    final VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
        .getContext().getAuthentication().getPrincipal();

    final TraitemetMasseParametres parametres = new TraitemetMasseParametres(
                                                                             jobParam, uuid, TYPES_JOB.modification_masse, hName, callerIP, nbDoc,
                                                                             extrait);

    // appel de la méthode d'insertion du job dans la pile des travaux
    try {
      traitementService.ajouterJobModificationMasse(parametres);
    } catch (final JobRequestAlreadyExistsException e) {
      throw new ModificationAxisFault("JobDejaExistant", e.getMessage(), e);
    }

    // On prend acte de la demande,
    // le retour se fera via le fichier resultats.xml de l'ECDE
    return ObjectStorageResponseFactory.createModificationMasseResponse(uuid
                                                                        .toString());
  }

}
