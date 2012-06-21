/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.serializer;

import java.nio.ByteBuffer;
import java.util.Map;

import me.prettyprint.cassandra.serializers.AbstractSerializer;
import fr.urssaf.image.commons.cassandra.serializer.XMLSerializer;

/**
 * Sérialiseur / Désérialiseur de Map&lt;String,String&gt;
 * 
 */
public class MapStringSerializer extends
      AbstractSerializer<Map<String, String>> {

   private static final MapStringSerializer INSTANCE = new MapStringSerializer();

   /**
    * {@inheritDoc}
    */
   @Override
   public final Map<String, String> fromByteBuffer(ByteBuffer byteBuffer) {
      @SuppressWarnings("unchecked")
      Map<String, String> map = (Map<String, String>) XMLSerializer.get()
            .fromByteBuffer(byteBuffer);
      return map;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ByteBuffer toByteBuffer(Map<String, String> map) {
      return XMLSerializer.get().toByteBuffer(map);
   }

   /**
    * Renvoie un singleton
    * 
    * @return singleton
    */
   public static MapStringSerializer get() {
      return INSTANCE;
   }
}
