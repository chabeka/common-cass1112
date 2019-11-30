package fr.urssaf.image.sae.pile.travaux.support;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobRequestDaoCql;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;

/**
 * Support pour l'utilisation de {@link JobRequestDao}
 */
@Component
public class JobRequestSupportCql {

   @Autowired
   private IJobRequestDaoCql jobRequestDaoCql;

   
   public JobRequestSupportCql() {
   }
   
   public JobRequestSupportCql(IJobRequestDaoCql jobRequestDaoCql) {
	   this.jobRequestDaoCql = jobRequestDaoCql;
   }

/**
    * Création d'un nouveau traitement.
    *
    * @param jobToCreate
    *           propriété du nouveau job à créer
    * @param clock
    *           horloge de la création du nouveau job
    */
   public final void ajouterJobDansJobRequest(final JobToCreate jobToCreate) {

      final JobRequestCql job = new JobRequestCql();

      job.setIdJob(jobToCreate.getIdJob());
      job.setState(JobState.CREATED.name());
      job.setType(jobToCreate.getType());
      job.setParameters(jobToCreate.getParameters());
      job.setJobParameters(jobToCreate.getJobParameters());
      job.setJobKey(jobToCreate.getJobKey());
      job.setCreationDate(jobToCreate.getCreationDate());
      job.setSaeHost(jobToCreate.getSaeHost());
      job.setClientHost(jobToCreate.getClientHost());
      job.setDocCount(jobToCreate.getDocCount());
      job.setDocCountTraite(jobToCreate.getDocCountTraite());
      job.setVi(jobToCreate.getVi());
      jobRequestDaoCql.saveWithMapper(job);

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
   public final void reserverJobDansJobRequest(final UUID idJob, final String reservedBy,
                                               final Date reservationDate) {

      Assert.notNull(idJob, "L'id du job doit être fournit");
      Assert.notNull(reservedBy, "Le host du serveur de reservation doit être renseigné");
      Assert.notNull(reservationDate, "La date de reservation doit être renseignée");

      final Optional<JobRequestCql> opt = jobRequestDaoCql.findWithMapperById(idJob);
      Assert.notNull(opt.orElse(null), "L'id fournit ne correspond à aucun job");
      final JobRequestCql job = opt.get();
      job.setState(JobState.RESERVED.name());
      job.setReservedBy(reservedBy);
      job.setReservationDate(reservationDate);
      jobRequestDaoCql.saveWithMapper(job);
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
   public final void passerEtatEnCoursJobRequest(final UUID idJob, final Date startingDate) {

      final Optional<JobRequestCql> opt = jobRequestDaoCql.findWithMapperById(idJob);
      Assert.notNull(opt.orElse(null), "L'id fournit ne correspond à aucun job");
      final JobRequestCql job = opt.get();
      job.setState(JobState.STARTING.name());
      job.setStartingDate(startingDate);
      jobRequestDaoCql.saveWithMapper(job);
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
    * @param nbDocTraite
    *           Nombre de documents traités
    * @param clock
    *           horloge de conclusion du job
    */
   public final void passerEtatTermineJobRequest(final UUID idJob, final Date endingDate,
                                                 final boolean success, final String message, final int nbDocTraite) {

      final Optional<JobRequestCql> opt = jobRequestDaoCql.findWithMapperById(idJob);
      Assert.notNull(opt.orElse(null), "L'id fournit ne correspond à aucun job");
      final JobRequestCql job = opt.get();
      if (success) {
         job.setState(JobState.SUCCESS.name());
      } else {
         job.setState(JobState.FAILURE.name());
      }

      job.setEndingDate(endingDate);

      if (nbDocTraite > 0) {
         job.setDocCountTraite(nbDocTraite);
      }

      if (message != null) {
         job.setMessage(message);
      }

      jobRequestDaoCql.saveWithMapper(job);
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
   public final void renseignerPidDansJobRequest(final UUID idJob, final Integer pid) {

      final Optional<JobRequestCql> opt = jobRequestDaoCql.findWithMapperById(idJob);
      Assert.notNull(opt.orElse(null), "L'id fournit ne correspond à aucun job");
      final JobRequestCql job = opt.get();

      job.setPid(pid);

      jobRequestDaoCql.saveWithMapper(job);
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
   public final void renseignerCheckFlagDansJobRequest(final UUID idJob,
                                                       final Boolean toCheckFlag, final String raison) {

      final Optional<JobRequestCql> opt = jobRequestDaoCql.findWithMapperById(idJob);
      Assert.notNull(opt.orElse(null), "L'id fournit ne correspond à aucun job");
      final JobRequestCql job = opt.get();
      job.setToCheckFlag(toCheckFlag);
      job.setToCheckFlagRaison(raison);

      jobRequestDaoCql.saveWithMapper(job);
   }

   /**
    * Suppression de JobRequest
    *
    * @param idJob
    *           identifiant du Job
    * @param clock
    *           horloge de la suppression
    */
   public final void deleteJobRequest(final UUID idJob) {

      jobRequestDaoCql.deleteById(idJob);
   }

   /**
    * Met à blanc toutes les informations d'un traitement préalable
    *
    * @param idJob
    *           identifiant du job à mettre à jour
    * @param etat
    *           etat du travail
    * @param clock
    *           horloge de la remise à zéro
    */
   public final void resetJob(final UUID idJob, final String etat) {

      final Optional<JobRequestCql> opt = jobRequestDaoCql.findWithMapperById(idJob);
      Assert.notNull(opt.orElse(null), "L'id fournit ne correspond à aucun job");
      final JobRequestCql job = opt.get();

      job.setState(JobState.CREATED.name());

      // On supprime la colonne reservationDate
      job.setReservationDate(null);
      // On supprime la colonne reservedBy
      job.setReservedBy(null);

      // Si le job est à l'état STARTING, on supprime :
      // - la colonne startingDate
      // - la colonne pid
      // - la colonne endingDate
      // - la colonne message

      job.setStartingDate(null);
      job.setPid(null);
      job.setEndingDate(null);
      job.setMessage(null);

      jobRequestDaoCql.saveWithMapper(job);

   }

   /**
    * Ajoute le nombre de docs traités dans la pile des travaux.
    *
    * @param idJob
    *           identifiant du job
    * @param nbDocs
    *           Nombre de docs traités
    * @param clock
    *           horloge de l'ajout du PID
    */
   public final void renseignerDocCountDansJobRequest(final UUID idJob, final Integer nbDocs) {

      final Optional<JobRequestCql> opt = jobRequestDaoCql.findWithMapperById(idJob);
      Assert.notNull(opt.orElse(null), "L'id fournit ne correspond à aucun job");
      final JobRequestCql job = opt.get();

      job.setDocCount(nbDocs);

      jobRequestDaoCql.saveWithMapper(job);

   }

   /**
    * Ajoute le nombre de docs traités dans la pile des travaux.
    *
    * @param idJob
    *           identifiant du job
    * @param nbDocs
    *           Nombre de docs traités
    * @param clock
    *           horloge de l'ajout du PID
    */
   public final void renseignerDocCountTraiteDansJobRequest(final UUID idJob,
                                                            final Integer nbDocs) {

      final Optional<JobRequestCql> opt = jobRequestDaoCql.findWithMapperById(idJob);
      Assert.notNull(opt.orElse(null), "L'id fournit ne correspond à aucun job");
      final JobRequestCql job = opt.get();

      job.setDocCountTraite(nbDocs);

      jobRequestDaoCql.saveWithMapper(job);

   }

   /**
    * Permet de changer l'état du statut dans la jobRequest
    *
    * @param idJob
    *           identifiant du job
    * @param state
    *           l'état cible du job
    * @param endingDate
    *           date de fin du job
    * @param message
    *           message de conclusion du job
    * @param clock
    *           horloge de conclusion du job
    */
   public final void changerEtatJobRequest(final UUID idJob, final String state, final Date endingDate, final String message) {

      final Optional<JobRequestCql> opt = jobRequestDaoCql.findWithMapperById(idJob);
      Assert.notNull(opt.orElse(null), "L'id fournit ne correspond à aucun job");
      final JobRequestCql job = opt.get();

      job.setState(JobState.valueOf(state).name());
      job.setEndingDate(endingDate);
      job.setMessage(message);

      jobRequestDaoCql.saveWithMapper(job);

   }

   public JobRequestCql getJobRequest(final UUID idJob) {
      return jobRequestDaoCql.findWithMapperById(idJob).orElse(null);
   }

   public Iterator<JobRequestCql> findAll() {
      return jobRequestDaoCql.findAllWithMapper();
   }

   public UUID getJobRequestIdByJobKey(final byte[] jobKey) {
      final Optional<JobRequestCql> opt = jobRequestDaoCql.getJobRequestIdByJobKey(jobKey);
      return opt.orElse(null).getIdJob();
   }

	public void setJobRequestDaoCql(IJobRequestDaoCql jobRequestDaoCql) {
		this.jobRequestDaoCql = jobRequestDaoCql;
	}
     
}
