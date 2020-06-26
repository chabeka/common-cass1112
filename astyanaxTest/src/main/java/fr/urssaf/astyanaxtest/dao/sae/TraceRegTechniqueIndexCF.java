package fr.urssaf.astyanaxtest.dao.sae;

import java.util.UUID;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.serializers.TimeUUIDSerializer;

public class TraceRegTechniqueIndexCF {
   public static final ColumnFamily<String, UUID> cf = new ColumnFamily<String, UUID>(
         "TraceRegTechniqueIndex",
         StringSerializer.get(),
         TimeUUIDSerializer.get());

   public static ColumnFamily<String, UUID> get() {
      return cf;
   }

}
