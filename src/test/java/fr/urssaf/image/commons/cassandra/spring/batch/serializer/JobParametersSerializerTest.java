package fr.urssaf.image.commons.cassandra.spring.batch.serializer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.serializers.StringSerializer;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

public class JobParametersSerializerTest {

   @Test
   public void test() {
      JobParametersSerializer js = JobParametersSerializer.get();
      StringSerializer ss = StringSerializer.get();
      JobParameters jobParameters1 = getTestJobParameters();
      byte[] bytes1 = js.toBytes(jobParameters1);
      System.out.println(jobParameters1);
      String s1 = ss.fromBytes(bytes1);
      System.out.println(s1);

      JobParameters jobParameters2 = js.fromBytes(bytes1);
      byte[] bytes2 = js.toBytes(jobParameters1);
      System.out.println(jobParameters2);
      String s2 = ss.fromBytes(bytes2);
      System.out.println(s2);
      Assert.assertArrayEquals(bytes2, bytes1);
      Assert.assertEquals(jobParameters2, jobParameters1);
   }

   private JobParameters getTestJobParameters() {
      Map<String, JobParameter> mapJobParameters = new HashMap<String, JobParameter>();
      mapJobParameters.put("premier_parametre", new JobParameter("test1"));
      mapJobParameters.put("deuxieme_parametre", new JobParameter("test2"));
      mapJobParameters.put("troisieme_parametre", new JobParameter(122L));
      mapJobParameters.put("4ème_parametre", new JobParameter(new Date()));
      mapJobParameters.put("5ème_parametre", new JobParameter(56.5));
      return new JobParameters(mapJobParameters);
   }

}
