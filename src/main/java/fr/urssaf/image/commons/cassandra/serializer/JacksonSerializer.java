package fr.urssaf.image.commons.cassandra.serializer;

import java.nio.ByteBuffer;

import me.prettyprint.cassandra.serializers.AbstractSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;

import org.apache.commons.lang.SerializationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * Classe de sérialisation d'un objet en json, qui utilise jackson
 * 
 * @param <T>
 *           le type d'objet à sérialiser/désérialiser
 */
public class JacksonSerializer<T> extends AbstractSerializer<T> {

   private final ObjectMapper mapper = new ObjectMapper();
   private final Class<T> clazz;

   /**
    * Constructeur
    * 
    * @param clazz
    *           : la classe qu'on sérialise/désérialise
    */
   public JacksonSerializer(Class<T> clazz) {
      super();
      this.clazz = clazz;
   }

   /**
    * Constructeur
    * 
    * @param clazz
    *           la classe qu'on sérialise/désérialise
    * @param serializeNullValues
    *           indicateur de sérialisation de valeurs à null
    *           <ul>
    *           <li>true : sérialisation des valeurs à null</li>
    *           <li>false : pas de sérialisation des valeurs à null</li>
    *           </ul>
    */
   public JacksonSerializer(Class<T> clazz, boolean serializeNullValues) {
      super();
      this.clazz = clazz;
      if (!serializeNullValues) {
         mapper.getSerializationConfig().setSerializationInclusion(
               Inclusion.NON_NULL);
      }
   }

   @Override
   public final T fromByteBuffer(ByteBuffer byteBuffer) {
      String json = StringSerializer.get().fromByteBuffer(byteBuffer);
      try {
         return mapper.readValue(json, clazz);
      } catch (Exception e) {
         String message = "Erreur lors de la désérialisation de la chaine "
               + json + " vers la classe " + clazz.getName();
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

   /**
    * Renvoie une représentation en String de l'objet
    * 
    * @param obj
    *           Objet à sérialiser
    * @return l'objet sérialisé, en String
    */
   public final String toString(Object obj) {
      try {
         String str = mapper.writeValueAsString(obj);
         return str;
      } catch (Exception e) {
         throw new SerializationException(e);
      }
   }

}