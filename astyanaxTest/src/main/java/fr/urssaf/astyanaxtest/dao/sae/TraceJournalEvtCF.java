package fr.urssaf.astyanaxtest.dao.sae;

import java.util.UUID;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.serializers.TimeUUIDSerializer;

public class TraceJournalEvtCF {
   public static final ColumnFamily<UUID, String> cf = new ColumnFamily<UUID, String>(
         "TraceJournalEvt",
         TimeUUIDSerializer.get(),
         StringSerializer.get());

   public static ColumnFamily<UUID, String> get() {
      return cf;
   }

}
