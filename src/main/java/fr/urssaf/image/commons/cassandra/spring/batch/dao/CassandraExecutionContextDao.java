package fr.urssaf.image.commons.cassandra.spring.batch.dao;

import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.item.ExecutionContext;

import fr.urssaf.image.commons.cassandra.spring.batch.serializer.ExecutionContextSerializer;

/**
 * Classe implémentant ExecutionContextDao, qui utilise cassandra.
 * L'implémentation est inspirée de
 * org.springframework.batch.core.repository.dao.JdbcExecutionContextDao
 * 
 * @see org.springframework.batch.core.repository.dao.JdbcExecutionContextDao
 * @author Samuel Carrière
 * 
 */
public class CassandraExecutionContextDao extends AbstractCassandraDAO
      implements ExecutionContextDao {

   /**
    * Constructeur
    * 
    * @param keyspace
    *           : keyspace cassandra
    */
   public CassandraExecutionContextDao(Keyspace keyspace) {
      super(keyspace);
   }

   @Override
   public final ExecutionContext getExecutionContext(JobExecution jobExecution) {
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
   public final ExecutionContext getExecutionContext(StepExecution stepExecution) {
      // Dans l'implémentation cassandra, le contexte est désérialisé en même
      // temps que les autres propriétés du stepExecution.
      return stepExecution.getExecutionContext();
   }

   @Override
   public void saveExecutionContext(JobExecution jobExecution) {
      // Dans l'implémentation cassandra, le contexte est sérialisé en même
      // temps que les autres propriétés du jobExecution.
      // Donc on ne fait rien de plus.
   }

   @Override
   public void saveExecutionContext(StepExecution stepExecution) {
      // Dans l'implémentation cassandra, le contexte est sérialisé en même
      // temps que les autres propriétés du stepExecution.
      // Donc on ne fait rien de plus.
   }

   @Override
   public final void updateExecutionContext(JobExecution jobExecution) {
      Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();
      ColumnFamilyUpdater<Long, String> updater =
      jobExecutionTemplate.createUpdater(jobExecution.getId());
      updater.setByteArray(JE_EXECUTION_CONTEXT_COLUMN,
      oSlz.toBytes(jobExecution.getExecutionContext()));
      jobExecutionTemplate.update(updater);
   }

   @Override
   public void updateExecutionContext(StepExecution stepExecution) {
      // Dans l'implémentation cassandra, le contexte est sérialisé en même
      // temps que les autres propriétés du stepExecution.
      // Donc on ne fait rien de plus.

   }

}
