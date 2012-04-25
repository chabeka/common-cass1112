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

   public JobHistorySupport(JobHistoryDao jobHistoryDao) {

      this.jobHistoryDao = jobHistoryDao;

   }

   public void ajouterTrace(UUID idJob, UUID timestampTrace,
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
