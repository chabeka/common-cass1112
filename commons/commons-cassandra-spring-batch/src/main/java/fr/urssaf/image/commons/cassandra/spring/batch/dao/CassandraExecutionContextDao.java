/**
 *
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.item.ExecutionContext;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.impl.CassandraExecutionContextDaoCqlImpl;
import fr.urssaf.image.commons.cassandra.spring.batch.daothrift.CassandraExecutionContextDaoThrift;

/**
 *
 *
 */
public class CassandraExecutionContextDao implements ExecutionContextDao {

   /** le nom de la table associée */
   private final String cfName = "jobexecution";

   /** le dao cql */
   private final CassandraExecutionContextDaoCqlImpl jobExeContextDaoCql;

   /** le dao thrift */
   private final CassandraExecutionContextDaoThrift jobExeContextDaoThrift;

   /**
    * @param daoCql
    * @param daoThrift
    */
   public CassandraExecutionContextDao(final CassandraExecutionContextDaoCqlImpl daoCql, final CassandraExecutionContextDaoThrift daoThrift) {
      super();
      this.jobExeContextDaoCql = daoCql;
      this.jobExeContextDaoThrift = daoThrift;
   }

   @Override
   public final ExecutionContext getExecutionContext(final JobExecution jobExecution) {
      if (jobExecution == null) {
         // Le simpleJobRepository appelle cette méthode avec un jobExecution
         // null dans
         // le cas d'une instance qui n'a pas d'exécution associée.
         // Dans ce cas, on renvoie un contexte vide.
         return new ExecutionContext();
      } else {
         // Dans l'implémentation cassandra, le contexte est désérialisé en même
         // temps que les autres propriétés du jobExecution.
         return jobExecution.getExecutionContext();
      }
   }

   @Override
   public final ExecutionContext getExecutionContext(final StepExecution stepExecution) {
      // Dans l'implémentation cassandra, le contexte est désérialisé en même
      // temps que les autres propriétés du stepExecution.
      return stepExecution.getExecutionContext();
   }

   @Override
   public void saveExecutionContext(final JobExecution jobExecution) {
      // Dans l'implémentation cassandra, le contexte est sérialisé en même
      // temps que les autres propriétés du jobExecution.
      // Donc on ne fait rien de plus.
   }

   @Override
   public void saveExecutionContext(final StepExecution stepExecution) {
      // Dans l'implémentation cassandra, le contexte est sérialisé en même
      // temps que les autres propriétés du stepExecution.
      // Donc on ne fait rien de plus.
   }

   @Override
   public void updateExecutionContext(final JobExecution jobExecution) {
      final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

      if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
         jobExeContextDaoCql.updateExecutionContext(jobExecution);
      } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
         jobExeContextDaoThrift.updateExecutionContext(jobExecution);
      } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
         // Pour exemple
         // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
      }

   }

   @Override
   public void updateExecutionContext(final StepExecution stepExecution) {
      // Dans l'implémentation cassandra, le contexte est sérialisé en même
      // temps que les autres propriétés du stepExecution.
      // Donc on ne fait rien de plus.

   }

}
