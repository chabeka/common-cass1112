package fr.urssaf.image.sae.pile.travaux.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.dao.JobHistoryDao;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobHistoryDaoCql;
import fr.urssaf.image.sae.pile.travaux.model.JobHistoryCql;

/**
 * Support pour l'utilisation de {@link JobHistoryDao}
 */
public class JobHistorySupportCql {

  private final IJobHistoryDaoCql jobHistoryDaoCql;

  /**
   * @param jobHistoryDao
   *          DAO de la colonne famille JobHistory
   */
  public JobHistorySupportCql(final IJobHistoryDaoCql jobHistoryDaoCql) {

    this.jobHistoryDaoCql = jobHistoryDaoCql;

  }

  /**
   * Créer une trace dans la colonne famille <code>JobHistory</code>
   *
   * @param idJob
   *          identifiant du job
   * @param timestampTrace
   *          identifiant de la trace
   * @param messageTrace
   *          message de la trace
   * @param clock
   *          horloge en microsecondes de l'insertion de la trace
   */
  public final void ajouterTrace(final UUID idJob, final UUID timestampTrace, final String messageTrace) {

    final Map<UUID, String> trace = new HashMap<>();
    trace.put(timestampTrace, messageTrace);
    boolean isPresent = false;
    if (jobHistoryDaoCql.existsById(idJob)) {
      // Si job present dans la base de donnée, on le modifie en ajoutant la nouvelle trace
      final Optional<JobHistoryCql> opt = jobHistoryDaoCql.findWithMapperById(idJob);
      if (opt.isPresent()) {
        final JobHistoryCql jobHInDB = opt.get();
        jobHInDB.getTrace().put(timestampTrace, messageTrace);
        jobHistoryDaoCql.save(jobHInDB);
        isPresent = true;
      }
    }
    // Nouvelle trace
    if (!isPresent) {
      final JobHistoryCql newJobH = new JobHistoryCql();
      newJobH.setIdjob(timestampTrace);
      newJobH.setTrace(trace);
      jobHistoryDaoCql.save(newJobH);
    }
  }

  /**
   * Suppression de l'historique d'un job
   *
   * @param idJob
   *          identifiant du job
   * @param clock
   *          horloge de suppression du job
   */
  public final void supprimerHistorique(final UUID idJob, final long clock) {
    jobHistoryDaoCql.deleteById(idJob);
  }
}
