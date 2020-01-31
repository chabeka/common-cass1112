package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.LongSerializer;
import com.netflix.astyanax.serializers.StringSerializer;

public class StepExecutionCF {
   private final static ColumnFamily<Long, String> cf = new ColumnFamily<Long, String>(
         "StepExecution", LongSerializer.get(), StringSerializer.get());

   public static ColumnFamily<Long, String> get() {
      return cf;
   }

}
