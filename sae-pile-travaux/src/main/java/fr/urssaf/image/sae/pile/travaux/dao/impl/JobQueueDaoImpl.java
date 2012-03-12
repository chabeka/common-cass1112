package fr.urssaf.image.sae.pile.travaux.dao.impl;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.pile.travaux.dao.JobQueueDao;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Implémentation du Service DAO {@link JobQueueDao}
 * 
 * 
 */
@Repository
public class JobQueueDaoImpl implements JobQueueDao {

   /**
    * {@inheritDoc}
    */
   @Override
   public final void getJobRequest(UUID jobRequestUUID) {
      // TODO Auto-generated method stub

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<JobRequest> getNonTerminatedJobs(String hostname) {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Iterator<JobRequest> getUnreservedJobRequestIterator() {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void saveJobRequest(JobRequest jobRequest) {
      // TODO Auto-generated method stub

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void updateJobRequest(JobRequest jobRequest) {
      // TODO Auto-generated method stub

   }

}
