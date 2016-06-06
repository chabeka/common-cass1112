package fr.urssaf.image.sae.webservices.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.EtatTraitementsMasse;
import fr.cirtil.www.saeservice.EtatTraitementsMasseResponse;
import fr.cirtil.www.saeservice.ListeUuidType;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.webservices.exception.EtatTraitementsMasseAxisFault;
import fr.urssaf.image.sae.webservices.service.WSCaptureMasseService;
import fr.urssaf.image.sae.webservices.service.WSEtatJobMasseService;
import fr.urssaf.image.sae.webservices.service.factory.ObjectEtatTraitementMasseFactory;

/**
 * Implémentation de {@link WSCaptureMasseService}<br>
 * L'implémentation est annotée par {@link Service}
 * 
 */
@Service
public class WSEtatJobMasseServiceImpl implements WSEtatJobMasseService {

   private static final Logger LOG = LoggerFactory
         .getLogger(WSEtatJobMasseServiceImpl.class);

   @Autowired
   private TraitementAsynchroneService traitementService;

   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public final EtatTraitementsMasseResponse etatJobMasse(
         EtatTraitementsMasse request, String callerIP)
         throws EtatTraitementsMasseAxisFault {

      String prefixeTrc = "etatJobMasse()";

      LOG.debug("{} - Début", prefixeTrc);

      ListeUuidType listeUuidType = request.getEtatTraitementsMasse()
            .getListeUuid();
      UuidType[] uuids = listeUuidType.getUuid();
      
      List<UUID> listeUuid = new ArrayList<UUID>();
      
      for (UuidType uuidType : uuids) {
         listeUuid.add(UUID.fromString(uuidType.getUuidType()));
      }
      
      
      List<JobRequest> listJob = traitementService.recupererJobs(listeUuid);
      
      // On retourne les jobs demandés
      try {
         return ObjectEtatTraitementMasseFactory
               .createEtatTraitementsMasseResponse(listJob);
      } catch (ParseException e) {
         throw new EtatTraitementsMasseAxisFault(e);
      }

   }

}
