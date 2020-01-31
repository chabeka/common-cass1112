package fr.urssaf.astyanaxtest.dao.sae;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;

public class MetadataCF {
   private static final ColumnFamily<String, String> cf = new ColumnFamily<String, String>(
                                                                                           "Metadata",
                                                                                           StringSerializer.get(),
                                                                                           StringSerializer.get());

   public static ColumnFamily<String, String> get() {
      return cf;
   }

}
