package fr.urssaf.image.commons.cassandra.serializer;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;

public class XMLSerializerTest {

   @SuppressWarnings("unchecked")
   @Test
   public void testXMLSerializer() throws UnsupportedEncodingException {
      XMLSerializer os = XMLSerializer.get();

      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("élement1", 1);
      map.put("élement2", "deux");
      map.put("date", new Date(1330813969524L));
      
      byte[] bytes = os.toBytes(map);
      String json = new String(bytes, "UTF8");
      
      System.out.println(json);

      HashMap<String, Object> map2 = (HashMap<String, Object>) os.fromBytes(bytes);
      Assert.assertEquals(1, map2.get("élement1"));
      Assert.assertEquals("deux", map2.get("élement2"));
      Assert.assertEquals(new Date(1330813969524L), map2.get("date"));
   }


}
