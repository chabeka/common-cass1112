package fr.urssaf.image.commons.cassandra.spring.batch.serializer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.serializers.StringSerializer;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;

public class ExecutionContextSerializerTest {

   @Test
   public void test() {
      ExecutionContextSerializer es = ExecutionContextSerializer.get();
      StringSerializer ss = StringSerializer.get();
      ExecutionContext executionContext1 = getTestExecutionContext();
      byte[] bytes1 = es.toBytes(executionContext1);
      System.out.println(executionContext1);
      String s1 = ss.fromBytes(bytes1);
      System.out.println(s1);

      ExecutionContext executionContext2 = es.fromBytes(bytes1);
      byte[] bytes2 = es.toBytes(executionContext2);
      System.out.println(executionContext2);
      String s2 = ss.fromBytes(bytes2);
      System.out.println(s2);
      Assert.assertArrayEquals(bytes2, bytes1);
   }

   private ExecutionContext getTestExecutionContext() {
      Map<String, Object> mapContext = new HashMap<String, Object>();
      mapContext.put("contexte1", "test1");
      mapContext.put("contexte2", 2);
      mapContext.put("contexte3", new Date());
      mapContext.put("contexte4", new int[] { 1, 2, 3 });
      ExecutionContext executionContext = new ExecutionContext(mapContext);
      return executionContext;
   }
}
