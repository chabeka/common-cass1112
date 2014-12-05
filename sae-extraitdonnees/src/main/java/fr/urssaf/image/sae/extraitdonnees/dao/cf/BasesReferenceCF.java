package fr.urssaf.image.sae.extraitdonnees.dao.cf;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;

/**
 * Column Family BasesReference
 */
public final class BasesReferenceCF {

   private BasesReferenceCF() {
   }

   public static final StringSerializer KEY_SERIALIZER = StringSerializer.get();

   public static final StringSerializer COLUMN_SERIALIZER = StringSerializer
         .get();

   public static final ColumnFamily<String, String> CF_BASES_REFERENCES = new ColumnFamily<String, String>(
         "BasesReference", KEY_SERIALIZER, COLUMN_SERIALIZER);
}
