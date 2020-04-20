package fr.urssaf.image.sae.lotinstallmaj.serializer;

import java.nio.ByteBuffer;
import java.util.List;

import fr.urssaf.image.commons.cassandra.serializer.XMLSerializer;
import me.prettyprint.cassandra.serializers.AbstractSerializer;

/**
 * Serializer de liste de String
 * 
 */
public final class ListSerializer extends AbstractSerializer<List<String>> {

   private ListSerializer() {
      super();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> fromByteBuffer(ByteBuffer byteBuffer) {
      @SuppressWarnings("unchecked")
      List<String> obj = (List<String>) XMLSerializer.get().fromByteBuffer(
            byteBuffer);

      return obj;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ByteBuffer toByteBuffer(List<String> obj) {
      return XMLSerializer.get().toByteBuffer(obj);
   }

   /**
    * @return l'instance de {@link ListSerializer}
    */
   public static ListSerializer get() {
      return ListSerializerHolder.INSTANCE;
   }

   private static final class ListSerializerHolder {
      private ListSerializerHolder() {
      }

      @SuppressWarnings("PMD.AccessorClassGeneration")
      private static final ListSerializer INSTANCE = new ListSerializer();
   }
}
