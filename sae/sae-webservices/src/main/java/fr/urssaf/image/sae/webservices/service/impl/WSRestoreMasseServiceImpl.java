package fr.urssaf.image.sae.webservices.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.RestoreMasse;
import fr.cirtil.www.saeservice.RestoreMasseResponse;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.webservices.aspect.BuildOrClearMDCAspect;
import fr.urssaf.image.sae.webservices.exception.RestoreAxisFault;
import fr.urssaf.image.sae.webservices.impl.factory.ObjectStorageResponseFactory;
import fr.urssaf.image.sae.webservices.service.WSCaptureMasseService;
import fr.urssaf.image.sae.webservices.service.WSRestoreMasseService;
import fr.urssaf.image.sae.webservices.util.HostnameUtil;

/**
 * Implémentation de {@link WSCaptureMasseService}<br>
 * L'implémentation est annotée par {@link Service}
 * 
 */
@Service
public class WSRestoreMasseServiceImpl implements WSRestoreMasseService {

   private static final Logger LOG = LoggerFactory
         .getLogger(WSRestoreMasseServiceImpl.class);

   @Autowired
   private TraitementAsynchroneService traitementService;
   
   
   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public final RestoreMasseResponse restoreEnMasse(RestoreMasse request,
         String callerIP) throws RestoreAxisFault {

      String prefixeTrc = "restoreEnMasse()";

      LOG.debug("{} - Début", prefixeTrc);

      UuidType uuidTraitementWS = request.getRestoreMasse().getUuid();

      String uuidTraitement = uuidTraitementWS.getUuidType().toString();

      LOG.debug("{} - UUID traitement : {}", prefixeTrc, uuidTraitement);

      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put(Constantes.UUID_TRAITEMENT_RESTORE, uuidTraitement);

      String hName = HostnameUtil.getHostname();

      VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
            .getContext().getAuthentication().getPrincipal();

      // Récupération de l'UUID du traitement
      String contextLog = MDC.get(BuildOrClearMDCAspect.LOG_CONTEXTE);
      // récupération de l'UUID du traitement
      UUID uuid = UUID.fromString(contextLog);

      // Création du job de restore dans la pile
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParam, uuid, TYPES_JOB.restore_masse, hName, callerIP, null, extrait);

      // appel de la méthode d'insertion du job dans la pile des travaux
      traitementService.ajouterJobRestoreMasse(parametres);

      // On prend acte de la demande
      return ObjectStorageResponseFactory.createRestoreMasseResponse(uuid
            .toString());

   }

}
