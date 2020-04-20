package fr.urssaf.image.sae.pile.travaux.support;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * Support pour l'utilisation de {@link JobRequestDao}
 * 
 * 
 */
@Component
public class JobRequestSupport {

	@Autowired
	private JobRequestDao jobRequestDao;

	public JobRequestSupport() {
		
	}
	
	/**
	 * 
	 * @param jobRequestDao
	 *            DAO de la colonne famille JobRequest
	 */
	public JobRequestSupport(JobRequestDao jobRequestDao) {

		this.jobRequestDao = jobRequestDao;
	}

	/**
	 * Création d'un nouveau traitement.
	 * 
	 * @param jobToCreate
	 *            propriété du nouveau job à créer
	 * @param clock
	 *            horloge de la création du nouveau job
	 */
	public final void ajouterJobDansJobRequest(final JobToCreate jobToCreate, final long clock) {

		// Valeur définie "en dur" par la méthode
		final String state = JobState.CREATED.name();

		// On utilise un ColumnFamilyUpdater, et on renseigne
		// la valeur de la clé dans la construction de l'updater
		final ColumnFamilyUpdater<UUID, String> updaterJobRequest = this.jobRequestDao.getJobRequestTmpl()
				.createUpdater(jobToCreate.getIdJob());

		// Ecriture des colonnes
		jobRequestDao.ecritColonneType(updaterJobRequest, jobToCreate.getType(), clock);
		if (jobToCreate.getParameters() != null) {
			jobRequestDao.ecritColonneParameters(updaterJobRequest, jobToCreate.getParameters(), clock);
		}

		if (jobToCreate.getJobParameters() != null) {
			jobRequestDao.ecritColonneJobParameters(updaterJobRequest, jobToCreate.getJobParameters(), clock);
		}
		jobRequestDao.ecritColonneJobKey(updaterJobRequest, jobToCreate.getJobKey(), clock);
		jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
		jobRequestDao.ecritColonneCreationDate(updaterJobRequest, jobToCreate.getCreationDate(), clock);
		if (jobToCreate.getSaeHost() != null) {
			jobRequestDao.ecritColonneSaeHost(updaterJobRequest, jobToCreate.getSaeHost(), clock);
		}
		if (jobToCreate.getClientHost() != null) {
			jobRequestDao.ecritColonneClientHost(updaterJobRequest, jobToCreate.getClientHost(), clock);
		}
		if (jobToCreate.getDocCount() != null) {
			jobRequestDao.ecritColonneDocCount(updaterJobRequest, jobToCreate.getDocCount(), clock);
		}
		if (jobToCreate.getDocCountTraite() != null) {
			jobRequestDao.ecritColonneDocCountTraite(updaterJobRequest, jobToCreate.getDocCountTraite(), clock);
		}
		if (jobToCreate.getVi() != null) {
			jobRequestDao.ecritColonneVi(updaterJobRequest, jobToCreate.getVi(), clock);
		}

		// Ecrit en base
		this.jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

	}

	/**
	 * Réservation d'un job existant.
	 * 
	 * @param idJob
	 *            identifiant du job existant
	 * @param reservedBy
	 *            Hostname ou IP du serveur qui réserve le job
	 * @param reservationDate
	 *            date de la réservation
	 * @param clock
	 *            horloge de la réservation du job
	 */
	public final void reserverJobDansJobRequest(final UUID idJob, final String reservedBy, final Date reservationDate,
			final long clock) {

		// Valeur définie "en dur" par la méthode
		final String state = JobState.RESERVED.name();

		// On utilise un ColumnFamilyUpdater, et on renseigne
		// la valeur de la clé dans la construction de l'updater
		final ColumnFamilyUpdater<UUID, String> updaterJobRequest = this.jobRequestDao.getJobRequestTmpl()
				.createUpdater(idJob);

		// Ecriture des colonnes
		jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
		jobRequestDao.ecritColonneReservedBy(updaterJobRequest, reservedBy, clock);
		jobRequestDao.ecritColonneReservationDate(updaterJobRequest, reservationDate, clock);

		// Ecrit en base
		this.jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

	}

	/**
	 * Démarre un job.
	 * 
	 * @param idJob
	 *            identifiant du job
	 * @param startingDate
	 *            date de démarrage
	 * @param clock
	 *            horloge du démarrage du job
	 */
	public final void passerEtatEnCoursJobRequest(final UUID idJob, final Date startingDate, final long clock) {

		// Valeur définie "en dur" par la méthode
		final String state = JobState.STARTING.name();

		// On utilise un ColumnFamilyUpdater, et on renseigne
		// la valeur de la clé dans la construction de l'updater
		final ColumnFamilyUpdater<UUID, String> updaterJobRequest = this.jobRequestDao.getJobRequestTmpl()
				.createUpdater(idJob);

		// Ecriture des colonnes
		jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
		jobRequestDao.ecritColonneStartingDate(updaterJobRequest, startingDate, clock);

		// Ecrit en base
		this.jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

	}

	/**
	 * Conclusion du job.
	 * 
	 * @param idJob
	 *            identifiant du job
	 * @param endingDate
	 *            date de fin du job
	 * @param success
	 *            <code>true</code> si le job a réussi, <code>false</false>
	 *            sinon
	 * @param message
	 *            message de conclusion du job
	 * @param nbDocTraite
	 *            Nombre de documents traités
	 * 
	 * @param clock
	 *            horloge de conclusion du job
	 */
	public final void passerEtatTermineJobRequest(final UUID idJob, final Date endingDate, final boolean success,
			final String message, final int nbDocTraite, final long clock) {

		// Valeur définie "en dur" par la méthode
		String state;
		if (success) {
			state = JobState.SUCCESS.name();
		} else {
			state = JobState.FAILURE.name();
		}

		// On utilise un ColumnFamilyUpdater, et on renseigne
		// la valeur de la clé dans la construction de l'updater
		final ColumnFamilyUpdater<UUID, String> updaterJobRequest = jobRequestDao.getJobRequestTmpl()
				.createUpdater(idJob);

		// Ecriture des colonnes
		jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
		jobRequestDao.ecritColonneEndingDate(updaterJobRequest, endingDate, clock);

		if (nbDocTraite > 0) {
			jobRequestDao.ecritColonneDocCountTraite(updaterJobRequest, nbDocTraite, clock);
		}

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
	 *            identifiant du job
	 * @param pid
	 *            PID du processus
	 * @param clock
	 *            horloge de l'ajout du PID
	 */
	public final void renseignerPidDansJobRequest(final UUID idJob, final Integer pid, final long clock) {

		// On utilise un ColumnFamilyUpdater, et on renseigne
		// la valeur de la clé dans la construction de l'updater
		final ColumnFamilyUpdater<UUID, String> updaterJobRequest = jobRequestDao.getJobRequestTmpl()
				.createUpdater(idJob);

		// Ecriture des colonnes
		jobRequestDao.ecritColonnePid(updaterJobRequest, pid, clock);

		// Ecrit en base
		jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

	}

	/**
	 * Ajoute un flag de vérification du job dans la pile des travaux.
	 * 
	 * @param idJob
	 *            identifiant du job
	 * @param toCheckFlag
	 *            <code>true</code> si le job doit être vérifier
	 * @param raison
	 *            message pour indiquer la raison pour laquelle le job doit être
	 *            vérifier
	 * @param clock
	 *            horloge de l'ajout du flag
	 */
	public final void renseignerCheckFlagDansJobRequest(final UUID idJob, final Boolean toCheckFlag,
			final String raison, final long clock) {

		// On utilise un ColumnFamilyUpdater, et on renseigne
		// la valeur de la clé dans la construction de l'updater
		final ColumnFamilyUpdater<UUID, String> updater = jobRequestDao.getJobRequestTmpl().createUpdater(idJob);

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
	 *            identifiant du Job
	 * @param clock
	 *            horloge de la suppression
	 */
	public final void deleteJobRequest(final UUID idJob, final long clock) {

		// Création du Mutator
		final Mutator<UUID> mutator = this.jobRequestDao.createMutator();

		// suppression du JobRequest
		this.jobRequestDao.mutatorSuppressionJobRequest(mutator, idJob, clock);

		// Execution de la commande
		mutator.execute();

	}

	/**
	 * Met à blanc toutes les informations d'un traitement préalable
	 * 
	 * @param idJob
	 *            identifiant du job à mettre à jour
	 * @param etat
	 *            etat du travail
	 * @param clock
	 *            horloge de la remise à zéro
	 */
	public final void resetJob(final UUID idJob, final String etat, final long clock) {

		// On utilise un ColumnFamilyUpdater, et on renseigne
		// la valeur de la clé dans la construction de l'updater
		final ColumnFamilyUpdater<UUID, String> updaterJobRequest = this.jobRequestDao.getJobRequestTmpl()
				.createUpdater(idJob);

		// On passe l'état à CREATED
		this.jobRequestDao.ecritColonneState(updaterJobRequest, "CREATED", clock);
		// Ecrit en base
		this.jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

		final Mutator<UUID> mutator = this.jobRequestDao.createMutator();

		// On supprime la colonne reservationDate
		mutator.addDeletion(idJob, "JobRequest", "reservationDate", StringSerializer.get());
		// On supprime la colonne reservedBy
		mutator.addDeletion(idJob, "JobRequest", "reservedBy", StringSerializer.get());

		// Si le job est à l'état STARTING, on supprime :
		// - la colonne startingDate
		// - la colonne pid
		// - la colonne endingDate
		// - la colonne message

		mutator.addDeletion(idJob, JobRequestDao.JOBREQUEST_CFNAME, "startingDate", StringSerializer.get());
		mutator.addDeletion(idJob, JobRequestDao.JOBREQUEST_CFNAME, "pid", StringSerializer.get());
		mutator.addDeletion(idJob, JobRequestDao.JOBREQUEST_CFNAME, "endingDate", StringSerializer.get());
		mutator.addDeletion(idJob, JobRequestDao.JOBREQUEST_CFNAME, "message", StringSerializer.get());

		mutator.execute();

	}

	/**
	 * Ajoute le nombre de docs traités dans la pile des travaux.
	 * 
	 * @param idJob
	 *            identifiant du job
	 * @param nbDocs
	 *            Nombre de docs traités
	 * @param clock
	 *            horloge de l'ajout du PID
	 */
	public final void renseignerDocCountDansJobRequest(UUID idJob, Integer nbDocs, long clock) {

		// On utilise un ColumnFamilyUpdater, et on renseigne
		// la valeur de la clé dans la construction de l'updater
		ColumnFamilyUpdater<UUID, String> updaterJobRequest = jobRequestDao.getJobRequestTmpl().createUpdater(idJob);

		// Ecriture des colonnes
		jobRequestDao.ecritColonneDocCount(updaterJobRequest, nbDocs, clock);

		// Ecrit en base
		jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

	}

	/**
	 * Ajoute le nombre de docs traités dans la pile des travaux.
	 * 
	 * @param idJob
	 *            identifiant du job
	 * @param nbDocs
	 *            Nombre de docs traités
	 * @param clock
	 *            horloge de l'ajout du PID
	 */
	public final void renseignerDocCountTraiteDansJobRequest(UUID idJob, Integer nbDocs, long clock) {

		// On utilise un ColumnFamilyUpdater, et on renseigne
		// la valeur de la clé dans la construction de l'updater
		ColumnFamilyUpdater<UUID, String> updaterJobRequest = jobRequestDao.getJobRequestTmpl().createUpdater(idJob);

		// Ecriture des colonnes
		jobRequestDao.ecritColonneDocCountTraite(updaterJobRequest, nbDocs, clock);

		// Ecrit en base
		jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

	}

	/**
	 * Permet de changer l'état du statut dans la jobRequest
	 * 
	 * @param idJob
	 *            identifiant du job
	 * @param state
	 *            l'état cible du job
	 * @param endingDate
	 *            date de fin du job
	 * @param message
	 *            message de conclusion du job
	 * @param clock
	 *            horloge de conclusion du job
	 */
	public final void changerEtatJobRequest(UUID idJob, String state, Date endingDate, String message, long clock) {
		// On utilise un ColumnFamilyUpdater, et on renseigne
		// la valeur de la clé dans la construction de l'updater
		ColumnFamilyUpdater<UUID, String> updaterJobRequest = jobRequestDao.getJobRequestTmpl().createUpdater(idJob);

		// Ecriture des colonnes
		jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
		jobRequestDao.ecritColonneEndingDate(updaterJobRequest, endingDate, clock);

		if (message != null) {
			jobRequestDao.ecritColonneMessage(updaterJobRequest, message, clock);
		}

		// Ecrit en base
		jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);
	}

	/**
	 * @param result
	 * @return
	 */
	public JobRequest createJobRequestFromResult(final ColumnFamilyResult<UUID, String> result) {
		return jobRequestDao.createJobRequestFromResult(result);
	}

	/**
	 * @return
	 */
	public ColumnFamilyTemplate<UUID, String> getJobRequestTmpl() {
		return jobRequestDao.getJobRequestTmpl();

	}

}
