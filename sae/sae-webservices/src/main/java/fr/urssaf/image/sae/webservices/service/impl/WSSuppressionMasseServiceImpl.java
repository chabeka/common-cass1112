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

import fr.cirtil.www.saeservice.RequeteRechercheType;
import fr.cirtil.www.saeservice.SuppressionMasse;
import fr.cirtil.www.saeservice.SuppressionMasseResponse;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.webservices.aspect.BuildOrClearMDCAspect;
import fr.urssaf.image.sae.webservices.exception.SuppressionAxisFault;
import fr.urssaf.image.sae.webservices.impl.factory.ObjectStorageResponseFactory;
import fr.urssaf.image.sae.webservices.service.WSCaptureMasseService;
import fr.urssaf.image.sae.webservices.service.WSSuppressionMasseService;
import fr.urssaf.image.sae.webservices.util.HostnameUtil;

/**
 * Implémentation de {@link WSCaptureMasseService}<br>
 * L'implémentation est annotée par {@link Service}
 * 
 */
@Service
public class WSSuppressionMasseServiceImpl implements WSSuppressionMasseService {

   private static final Logger LOG = LoggerFactory
         .getLogger(WSSuppressionMasseServiceImpl.class);

   @Autowired
   private TraitementAsynchroneService traitementService;
   
   /**
    * Nom du job d'un traitement de suppression en masse
    */
   public static final String SUPPRESSION_MASSE_JN = "suppression_masse";


   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public final SuppressionMasseResponse suppressionEnMasse(
         SuppressionMasse request, String callerIP) throws SuppressionAxisFault {

      String prefixeTrc = "suppressionEnMasse()";

      LOG.debug("{} - Début", prefixeTrc);

      RequeteRechercheType requeteWS = request.getSuppressionMasse()
            .getRequete();
      String requete = requeteWS.getRequeteRechercheType();

      LOG.debug("{} - Requete: {}", prefixeTrc, requete);

      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put(Constantes.REQUETE, requete);

      String hName = HostnameUtil.getHostname();

      VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
            .getContext().getAuthentication().getPrincipal();

      // Récupération de l'UUID du traitement
      String contextLog = MDC.get(BuildOrClearMDCAspect.LOG_CONTEXTE);
      // récupération de l'UUID du traitement
      UUID uuid = UUID.fromString(contextLog);

      // Création du job de suppression dans la pile
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(jobParam,
            uuid, SUPPRESSION_MASSE_JN, hName, callerIP, null, extrait);

       // appel de la méthode d'insertion du job dans la pile des travaux
       traitementService.ajouterJob(parametres);

      // On prend acte de la demande
      return ObjectStorageResponseFactory.createSuppressionMasseResponse(uuid
            .toString());


   }

}
