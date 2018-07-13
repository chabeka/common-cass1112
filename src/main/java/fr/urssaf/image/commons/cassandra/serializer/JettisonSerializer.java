package fr.urssaf.image.commons.cassandra.serializer;

import java.nio.ByteBuffer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import me.prettyprint.cassandra.serializers.AbstractSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;

/**
 * Classe de sérialisation d'un objet en json, qui utilise
 * xstream avec le driver Jettisson
 *
 */
public class JettisonSerializer extends AbstractSerializer<Object> {

   private final XStream xstream;
   private static final JettisonSerializer INSTANCE = new JettisonSerializer();

   /**
    * Constructeur
    */
   public JettisonSerializer() {
      super();
      HierarchicalStreamDriver driver = new JettisonMappedXmlDriver();
      xstream = new XStream(driver);
   }

   @Override
   public final Object fromByteBuffer(ByteBuffer byteBuffer) {
      return xstream.fromXML(StringSerializer.get().fromByteBuffer(byteBuffer));
   }

   @Override
   public final ByteBuffer toByteBuffer(Object obj) {
      String str = xstream.toXML(obj);
      return StringSerializer.get().toByteBuffer(str);
   }

   /**
    * Renvoie un singleton
    * @return singleton
    */
   public static JettisonSerializer get() {
      return INSTANCE;
   }
}