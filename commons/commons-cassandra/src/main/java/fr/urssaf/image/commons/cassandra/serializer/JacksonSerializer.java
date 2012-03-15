package fr.urssaf.image.commons.cassandra.serializer;

import java.nio.ByteBuffer;

import me.prettyprint.cassandra.serializers.AbstractSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;

import org.apache.commons.lang.SerializationException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Classe de sérialisation d'un objet en json, qui utilise
 * jackson
 *
 */
public class JacksonSerializer<T> extends AbstractSerializer<T> {

   private static final ObjectMapper mapper = new ObjectMapper();
   private final Class<T> clazz;
   
   public JacksonSerializer(Class<T> clazz) {
      super();
      this.clazz = clazz;
   }


   @Override
   public final T fromByteBuffer(ByteBuffer byteBuffer) {
      String json = StringSerializer.get().fromByteBuffer(byteBuffer);
      try {
         return mapper.readValue(json, clazz);
      } catch (Exception e) {
         String message = "Erreur lors de la désérialisation de la chaine " + json +
         " vers la classe " + clazz.getName();
         throw new SerializationException(message, e);
      }
   }

   @Override
   public final ByteBuffer toByteBuffer(Object obj) {
      try {
         String str = mapper.writeValueAsString(obj);
         return StringSerializer.get().toByteBuffer(str);
      } catch (Exception e) {
         throw new SerializationException(e);  
      }
   }

   public final String toString(Object obj) {
      try {
         String str = mapper.writeValueAsString(obj);
         return str;
      } catch (Exception e) {
         throw new SerializationException(e);  
      }
   }
   
}