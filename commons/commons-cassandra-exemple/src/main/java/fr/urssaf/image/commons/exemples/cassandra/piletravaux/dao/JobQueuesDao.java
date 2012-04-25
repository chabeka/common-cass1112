package fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao;

import java.util.UUID;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao.serializer.JobQueueSerializer;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.modele.JobQueue;

public class JobQueuesDao {

   private static final String JOBSQUEUE_CFNAME = "JobsQueue";
   
   private static final int MAX_JOB_ATTIBUTS = 100;
   
   private static final int TTL = 2592000; // 2592000 secondes, soit 30 jours
   
   
   
   public ColumnFamilyTemplate<String, String> createCFTemplate(Keyspace keyspace) {
      
      // Propriété de clé:
      //  - Type de la valeur : String
      //  - Serializer de la valeur : StringSerializer
      
      
      ColumnFamilyTemplate<String, String> jobQueuesTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace,
            JOBSQUEUE_CFNAME,
            StringSerializer.get(),
            StringSerializer.get());
      
      jobQueuesTmpl.setCount(MAX_JOB_ATTIBUTS);
      
      return jobQueuesTmpl;
      
   }
   
   
   
   public Mutator<String> createMutator(Keyspace keyspace) {
      
      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());
      
      return mutator;
            
   }
   
   
   @SuppressWarnings("unchecked")
   private void addColumn(
         ColumnFamilyUpdater<String, String> updater,
         Object colName, 
         Object value,
         Serializer nameSerializer,
         Serializer valueSerializer,
         long clock) {
      
      HColumn<String, Object> column = HFactory.createColumn(
            colName, 
            value,
            nameSerializer, 
            valueSerializer);
      
      column.setTtl(TTL);
      column.setClock(clock);
      updater.setColumn(column);
      
   }
   
   
   public void ecritColonneJobQueue(
         ColumnFamilyUpdater<String, String> updater,
         UUID idJob,
         JobQueue jobQueue, 
         long clock) {
      
      addColumn(updater, idJob, jobQueue,UUIDSerializer.get(),JobQueueSerializer.get(), clock);
      
   }
   
   
   public void mutatorAjouterInsertionJobQueue(
         Mutator<String> mutator,
         String hostnameOuJobsWaiting,
         JobQueue jobQueue,
         long clock) {
      
      HColumn<UUID, JobQueue> col = HFactory.createColumn(
            jobQueue.getIdJob(),
            jobQueue, 
            UUIDSerializer.get(),
            JobQueueSerializer.get());
      
      col.setTtl(TTL);
      col.setClock(clock);
      
      mutator.addInsertion(hostnameOuJobsWaiting, JOBSQUEUE_CFNAME, col);
      
   }
   
   
   public void mutatorAjouterSuppressionJobQueue(
         Mutator<String> mutator,
         String hostnameOuJobsWaiting,
         UUID idJob,
         long clock) {
      
      mutator.addDeletion(hostnameOuJobsWaiting, JOBSQUEUE_CFNAME, idJob,UUIDSerializer.get(),clock);
      
   }
   
}
