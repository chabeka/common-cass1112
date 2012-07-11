/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.serializer;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.AbstractSerializer;
import fr.urssaf.image.commons.cassandra.serializer.XMLSerializer;

/**
 * 
 * 
 */
public class MapSerializer extends
      AbstractSerializer<Map<String, List<String>>> {

   private static final MapSerializer INSTANCE = new MapSerializer();

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, List<String>> fromByteBuffer(ByteBuffer byteBuffer) {
      @SuppressWarnings("unchecked")
      Map<String, List<String>> map = (Map<String, List<String>>) XMLSerializer
            .get().fromByteBuffer(byteBuffer);

      return map;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ByteBuffer toByteBuffer(Map<String, List<String>> map) {
      return XMLSerializer.get().toByteBuffer(map);
   }

   public static MapSerializer get() {
      return INSTANCE;
   }

}
