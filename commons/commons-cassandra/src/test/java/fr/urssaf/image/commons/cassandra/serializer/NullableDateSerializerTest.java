package fr.urssaf.image.commons.cassandra.serializer;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;


public class NullableDateSerializerTest {

   @Test
   public void test() {
      NullableDateSerializer ds = new NullableDateSerializer();
      byte[] bytes = ds.toBytes(null);
      Assert.assertEquals(0, bytes.length);
      Date d = ds.fromBytes(new byte[0]);
      Assert.assertEquals(null, d);

      Date d1 = new Date();
      byte[] bytes1 = ds.toBytes(d1);
      Date d2 = ds.fromBytes(bytes1);
      Assert.assertEquals(d2, d1);

   }

}
