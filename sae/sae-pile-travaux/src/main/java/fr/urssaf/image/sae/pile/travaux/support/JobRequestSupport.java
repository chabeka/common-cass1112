package fr.urssaf.image.sae.pile.travaux.support;

import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;

/**
 * Support pour l'utilisation de {@link JobRequestDao}
 * 
 * 
 */
public class JobRequestSupport {

   private final JobRequestDao jobRequestDao;

   public JobRequestSupport(JobRequestDao jobRequestDao) {

      this.jobRequestDao = jobRequestDao;
   }

   public void ajouterJobDansJobRequest(JobToCreate jobToCreate, long clock) {

      // Valeur définie "en dur" par la méthode
      String state = JobState.CREATED.name();

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobRequest = this.jobRequestDao
            .getJobRequestTmpl().createUpdater(jobToCreate.getIdJob());

      // Ecriture des colonnes
      jobRequestDao.ecritColonneType(updaterJobRequest, jobToCreate.getType(),
            clock);
      jobRequestDao.ecritColonneParameters(updaterJobRequest, jobToCreate
            .getParameters(), clock);
      jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
      jobRequestDao.ecritColonneCreationDate(updaterJobRequest, jobToCreate
            .getCreationDate(), clock);
      if (jobToCreate.getSaeHost() != null) {
         jobRequestDao.ecritColonneSaeHost(updaterJobRequest, jobToCreate
               .getSaeHost(), clock);
      }
      if (jobToCreate.getClientHost() != null) {
         jobRequestDao.ecritColonneClientHost(updaterJobRequest, jobToCreate
               .getClientHost(), clock);
      }
      if (jobToCreate.getDocCount() != null) {
         jobRequestDao.ecritColonneDocCount(updaterJobRequest, jobToCreate
               .getDocCount(), clock);
      }

      // Ecrit en base
      this.jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

   }

   public void reserverJobDansJobRequest(UUID idJob, String reservedBy,
         Date reservationDate, long clock) {

      // Valeur définie "en dur" par la méthode
      String state = JobState.RESERVED.name();

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobRequest = this.jobRequestDao
            .getJobRequestTmpl().createUpdater(idJob);

      // Ecriture des colonnes
      jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
      jobRequestDao
            .ecritColonneReservedBy(updaterJobRequest, reservedBy, clock);
      jobRequestDao.ecritColonneReservationDate(updaterJobRequest,
            reservationDate, clock);

      // Ecrit en base
      this.jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

   }

   public void passerEtatEnCoursJobRequest(UUID idJob, Date startingDate,
         long clock) {

      // Valeur définie "en dur" par la méthode
      String state = JobState.STARTING.name();

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobRequest = this.jobRequestDao
            .getJobRequestTmpl().createUpdater(idJob);

      // Ecriture des colonnes
      jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
      jobRequestDao.ecritColonneStartingDate(updaterJobRequest, startingDate,
            clock);

      // Ecrit en base
      this.jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

   }

   public void passerEtatTermineJobRequest(UUID idJob, Date endingDate,
         boolean success, String message, long clock) {

      // Valeur définie "en dur" par la méthode
      String state;
      if (success) {
         state = JobState.SUCCESS.name();
      } else {
         state = JobState.FAILURE.name();
      }

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobRequest = jobRequestDao
            .getJobRequestTmpl().createUpdater(idJob);

      // Ecriture des colonnes
      jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
      jobRequestDao
            .ecritColonneEndingDate(updaterJobRequest, endingDate, clock);

      if (message != null) {

         jobRequestDao.ecritColonneMessage(updaterJobRequest, message, clock);

      }

      // Ecrit en base
      jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

   }

   public void renseignerPidDansJobRequest(UUID idJob, Integer pid, long clock) {

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobRequest = jobRequestDao
            .getJobRequestTmpl().createUpdater(idJob);

      // Ecriture des colonnes
      jobRequestDao.ecritColonnePid(updaterJobRequest, pid, clock);

      // Ecrit en base
      jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

   }
}
