/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.serializer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.AbstractSerializer;
import fr.urssaf.image.commons.cassandra.serializer.XMLSerializer;

/**
 * Sérialiseur / Désérialiseur de List&lt;String&gt;
 * 
 */
public class ListStringSerializer extends AbstractSerializer<List<String>> {

   private static final ListStringSerializer INSTANCE = new ListStringSerializer();

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<String> fromByteBuffer(ByteBuffer byteBuffer) {
      @SuppressWarnings("unchecked")
      List<String> list = (List<String>) XMLSerializer.get().fromByteBuffer(
            byteBuffer);
      return new ArrayList<String>(list);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ByteBuffer toByteBuffer(List<String> list) {
      return XMLSerializer.get().toByteBuffer(list);
   }

   /**
    * Renvoie un singleton
    * 
    * @return singleton
    */
   public static ListStringSerializer get() {
      return INSTANCE;
   }
}
