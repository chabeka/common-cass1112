package fr.urssaf.image.commons.cassandra.serializer;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

public class JettisonSerializerTest {

   @SuppressWarnings("unchecked")
   @Test
   @Ignore     // Ne fonctionne pas avec les dernières version de xstream et jettison
   public void testJettisonSerializer() throws UnsupportedEncodingException {
      JettisonSerializer os = new JettisonSerializer();
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("element1", 1);
      map.put("element2", "deux");
      
      byte[] bytes = os.toBytes(map);
      String json = new String(bytes, "UTF8");
      
      System.out.println(json);
      
      HashMap<String, Object> map2 = (HashMap<String, Object>) os.fromBytes(bytes);
      Assert.assertEquals(1, map2.get("element1"));
      Assert.assertEquals("deux", map2.get("element2"));
   }

}
