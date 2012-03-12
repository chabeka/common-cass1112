package fr.urssaf.image.sae.pile.travaux.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;

/**
 * Implémentation du service {@link JobQueueService}
 * 
 * 
 */
@Service
public class JobQueueServiceImpl implements JobQueueService {

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addJob(UUID idJob, String type, String parametres,
         Date dateDemande) {
      // TODO Auto-generated method stub

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void endingJob(UUID idJob, boolean succes,
         Date dateFinTraitement) {
      // TODO Auto-generated method stub

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void reserveJob(UUID idJob, String hostname,
         Date dateReservation) throws JobDejaReserveException,
         JobInexistantException {
      // TODO Auto-generated method stub

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void startingJob(UUID idJob, Date dateDebutTraitement) {
      // TODO Auto-generated method stub

   }

}
