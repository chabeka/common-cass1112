package fr.urssaf.image.sae.ordonnanceur.service.impl;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.ordonnanceur.service.JobFailureService;

/**
 * Implémentation du service {@link JobFailureService}.<br>
 * <br>
 * La persistance des échec se fait pour en mémoire.
 * 
 * 
 */
@Service
public class JobFailureServiceImpl implements JobFailureService {

   private static final Logger LOG = LoggerFactory
         .getLogger(JobFailureService.class);

   private final Map<UUID, JobFailureInfo> jobsFailure = new ConcurrentHashMap<UUID, JobFailureInfo>();

   private final Set<UUID> jobsUUIDFailure = new HashSet<UUID>();

   /**
    * {@inheritDoc}
    */
   @Override
   public final void ajouterEchec(UUID idJob, Throwable echec) {

      synchronized (this) {

         if (!jobsFailure.containsKey(idJob)) {

            jobsFailure.put(idJob, new JobFailureInfo());
         }

         JobFailureInfo failureInfo = jobsFailure.get(idJob);
         failureInfo.addFailure(echec);

         if (failureInfo.countAnomalie >= MAX_ANOMALIE) {

            String msgException = "{0} - le traitement n°{1} a dépassé le nombre maximum d''anomalies ({2}) tolérées par l''ordonnanceur. La dernière anomalie est :";

            LOG.error(MessageFormat.format(msgException, "ajouterEchec", idJob,
                  MAX_ANOMALIE), failureInfo.lastAnomalie);

            jobsUUIDFailure.add(idJob);
         }

      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Set<UUID> findJobEchec() {

      return Collections.unmodifiableSet(jobsUUIDFailure);
   }

   protected final Map<UUID, JobFailureInfo> getJobsFailure() {

      return Collections.unmodifiableMap(jobsFailure);
   }

   protected static class JobFailureInfo {

      private int countAnomalie;

      private Throwable lastAnomalie;

      private void addFailure(Throwable lastAnomalie) {
         this.countAnomalie++;
         this.lastAnomalie = lastAnomalie;

      }

      protected final int getCountAnomalie() {
         return this.countAnomalie;
      }

      protected final Throwable getLastAnomalie() {
         return this.lastAnomalie;
      }

   }

}
