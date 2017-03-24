package fr.urssaf.image.commons.exemples.cassandra.grossecolonne.dao;

import java.util.UUID;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;

import org.springframework.batch.item.ExecutionContext;

import fr.urssaf.image.commons.exemples.cassandra.grossecolonne.serializer.ExecutionContextSerializer;

public class JobExecutionDao {

   
   public static final String CF_NAME = "JobExecution";
   
   protected static final String COL_EXECUTION_CONTEXT = "executionContext";
   
   private static final int MAX_ATTIBUTS = 100;
   
   
   public ColumnFamilyTemplate<Long, String> createCFTemplate(Keyspace keyspace) {
      
      ColumnFamilyTemplate<Long, String> template = new ThriftColumnFamilyTemplate<Long, String>(
            keyspace,
            CF_NAME,
            LongSerializer.get(),
            StringSerializer.get());
      
      template.setCount(MAX_ATTIBUTS);
      
      return template; 
      
   }
   
   
   public void ecritExecutionContext(
         ColumnFamilyUpdater<Long, String> updater,
         ExecutionContext executionContext
         // ,long clock
         ) {
      
      Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();
      
      updater.setByteArray(
            COL_EXECUTION_CONTEXT,
            oSlz.toBytes(executionContext));
      
   }
   
   
   public ExecutionContext createExecutionContextFromResult(
         ColumnFamilyResult<Long, String> result) {
      
      if (result == null || !result.hasResults()) {
         return null;
      }
      
      Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();
      
      ExecutionContext executionContext = oSlz.fromBytes(result
            .getByteArray(COL_EXECUTION_CONTEXT));
      
      return executionContext;
      
   }
   
   
   
}
