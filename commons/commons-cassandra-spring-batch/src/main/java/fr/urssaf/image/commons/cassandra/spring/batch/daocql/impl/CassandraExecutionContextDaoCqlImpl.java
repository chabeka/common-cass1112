/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql.impl;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobTranslateUtils;

/**
 * TODO (AC75095028) Description du type
 */
@Component
public class CassandraExecutionContextDaoCqlImpl implements ExecutionContextDao {

   @Autowired
   IJobExecutionDaoCql jobExeDaoCql;

   /**
    * Constructeur
    *
    */
   public CassandraExecutionContextDaoCqlImpl() {

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
   public final void updateExecutionContext(final JobExecution jobExecution) {
      final JobExecutionCql executionCql = JobTranslateUtils.JobExecutionToJobExecutionCql(jobExecution);
      jobExeDaoCql.saveWithMapper(executionCql);
   }

   @Override
   public void updateExecutionContext(final StepExecution stepExecution) {
      // Dans l'implémentation cassandra, le contexte est sérialisé en même
      // temps que les autres propriétés du stepExecution.
      // Donc on ne fait rien de plus.

   }

}
