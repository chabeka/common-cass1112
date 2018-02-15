package fr.urssaf.image.commons.cassandra.serializer;

import java.nio.ByteBuffer;

import me.prettyprint.cassandra.serializers.AbstractSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Classe de sérialisation d'un objet en XML, qui utilise
 * xstream
 *
 */
public class XMLSerializer extends AbstractSerializer<Object> {

   private final XStream xstream;
   private static final XMLSerializer INSTANCE = new XMLSerializer();

   /**
    * Constructeur
    */
   public XMLSerializer() {
      super();
      HierarchicalStreamDriver driver = new StaxDriver();
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
   public static XMLSerializer get() {
      return INSTANCE;
   }
}