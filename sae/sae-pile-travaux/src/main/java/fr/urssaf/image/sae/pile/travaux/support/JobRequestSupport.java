package fr.urssaf.image.sae.pile.travaux.support;

import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;
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

   /**
    * 
    * @param jobRequestDao
    *           DAO de la colonne famille JobRequest
    */
   public JobRequestSupport(JobRequestDao jobRequestDao) {

      this.jobRequestDao = jobRequestDao;
   }

   /**
    * Création d'un nouveau traitement.
    * 
    * @param jobToCreate
    *           propriété du nouveau job à créer
    * @param clock
    *           horloge de la création du nouveau job
    */
   public final void ajouterJobDansJobRequest(JobToCreate jobToCreate,
         long clock) {

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
      if (jobToCreate.getJobParameters() != null) {
         jobRequestDao.ecritColonneJobParameters(updaterJobRequest, jobToCreate
               .getJobParameters(), clock);
      }
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

      if (jobToCreate.getVi() != null) {
         jobRequestDao.ecritColonneVi(updaterJobRequest, jobToCreate.getVi(),
               clock);
      }

      // Ecrit en base
      this.jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

   }

   /**
    * Réservation d'un job existant.
    * 
    * @param idJob
    *           identifiant du job existant
    * @param reservedBy
    *           Hostname ou IP du serveur qui réserve le job
    * @param reservationDate
    *           date de la réservation
    * @param clock
    *           horloge de la réservation du job
    */
   public final void reserverJobDansJobRequest(UUID idJob, String reservedBy,
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

   /**
    * Démarre un job.
    * 
    * @param idJob
    *           identifiant du job
    * @param startingDate
    *           date de démarrage
    * @param clock
    *           horloge du démarrage du job
    */
   public final void passerEtatEnCoursJobRequest(UUID idJob, Date startingDate,
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

   /**
    * Conclusion du job.
    * 
    * @param idJob
    *           identifiant du job
    * @param endingDate
    *           date de fin du job
    * @param success
    *           <code>true</code> si le job a réussi, <code>false</false> sinon
    * @param message
    *           message de conclusion du job
    * @param clock
    *           horloge de conclusion du job
    */
   public final void passerEtatTermineJobRequest(UUID idJob, Date endingDate,
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

   /**
    * Ajoute le PID du processus du job dans la pile des travaux.
    * 
    * @param idJob
    *           identifiant du job
    * @param pid
    *           PID du processus
    * @param clock
    *           horloge de l'ajout du PID
    */
   public final void renseignerPidDansJobRequest(UUID idJob, Integer pid,
         long clock) {

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobRequest = jobRequestDao
            .getJobRequestTmpl().createUpdater(idJob);

      // Ecriture des colonnes
      jobRequestDao.ecritColonnePid(updaterJobRequest, pid, clock);

      // Ecrit en base
      jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

   }

   /**
    * Ajoute un flag de vérification du job dans la pile des travaux.
    * 
    * @param idJob
    *           identifiant du job
    * @param toCheckFlag
    *           <code>true</code> si le job doit être vérifier
    * @param raison
    *           message pour indiquer la raison pour laquelle le job doit être
    *           vérifier
    * @param clock
    *           horloge de l'ajout du flag
    */
   public final void renseignerCheckFlagDansJobRequest(UUID idJob,
         Boolean toCheckFlag, String raison, long clock) {

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updater = jobRequestDao
            .getJobRequestTmpl().createUpdater(idJob);

      // Ecriture des colonnes
      jobRequestDao.ecritColonneToCheckFlag(updater, toCheckFlag, clock);
      jobRequestDao.ecritColonneToCheckFlagRaison(updater, raison, clock);

      // Ecrit en base
      jobRequestDao.getJobRequestTmpl().update(updater);

   }

   /**
    * Suppression de JobRequest
    * 
    * @param idJob
    *           identifiant du Job
    * @param clock
    *           horloge de la suppression
    */
   public final void deleteJobRequest(UUID idJob, long clock) {

      // Création du Mutator
      Mutator<UUID> mutator = this.jobRequestDao.createMutator();

      // suppression du JobRequest
      this.jobRequestDao.mutatorSuppressionJobRequest(mutator, idJob, clock);

      // Execution de la commande
      mutator.execute();

   }

   /**
    * Met à blanc toutes les informations d'un traitement préalable
    * 
    * @param idJob
    *           identifiant du job à mettre à jour
    */
   public void resetJob(UUID idJob, String etat, long clock) {

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobRequest = this.jobRequestDao
            .getJobRequestTmpl().createUpdater(idJob);

      // On passe l'état à CREATED
      this.jobRequestDao.ecritColonneState(updaterJobRequest, "CREATED", clock);
      // Ecrit en base
      this.jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

      Mutator<UUID> mutator = this.jobRequestDao.createMutator();

      // On supprime la colonne reservationDate
      mutator.addDeletion(idJob, "JobRequest", "reservationDate",
            StringSerializer.get());
      // On supprime la colonne reservedBy
      mutator.addDeletion(idJob, "JobRequest", "reservedBy", StringSerializer
            .get());

      // Si le job est à l'état STARTING, on supprime :
      // - la colonne startingDate
      // - la colonne pid
      // - la colonne endingDate
      // - la colonne message

      mutator.addDeletion(idJob, "JobRequest", "startingDate", StringSerializer
            .get());
      mutator.addDeletion(idJob, "JobRequest", "pid", StringSerializer.get());
      mutator.addDeletion(idJob, "JobRequest", "endingDate", StringSerializer
            .get());
      mutator.addDeletion(idJob, "JobRequest", "message", StringSerializer
            .get());

      mutator.execute();

   }
}
