package fr.urssaf.image.sae.pile.travaux.support;

import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import fr.urssaf.image.sae.pile.travaux.dao.JobHistoryDao;

/**
 * Support pour l'utilisation de {@link JobHistoryDao}
 * 
 * 
 */
public class JobHistorySupport {

   private final JobHistoryDao jobHistoryDao;

   /**
    * 
    * @param jobHistoryDao
    *           DAO de la colonne famille JobHistory
    */
   public JobHistorySupport(JobHistoryDao jobHistoryDao) {

      this.jobHistoryDao = jobHistoryDao;

   }

   /**
    * Créer une trace dans la colonne famille <code>JobHistory</code>
    * 
    * @param idJob
    *           identifiant du job
    * @param timestampTrace
    *           identifiant de la trace
    * @param messageTrace
    *           message de la trace
    * @param clock
    *           horloge en microsecondes de l'insertion de la trace
    */
   public final void ajouterTrace(UUID idJob, UUID timestampTrace,
         String messageTrace, long clock) {

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobHistory = this.jobHistoryDao
            .getJobHistoryTmpl().createUpdater(idJob);

      // Ecriture des colonnes
      jobHistoryDao.ecritColonneTrace(updaterJobHistory, timestampTrace,
            messageTrace, clock);

      // Ecrit en base
      this.jobHistoryDao.getJobHistoryTmpl().update(updaterJobHistory);

   }
}
