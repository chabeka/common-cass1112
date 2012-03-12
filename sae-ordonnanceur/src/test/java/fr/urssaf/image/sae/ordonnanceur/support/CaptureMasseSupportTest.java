package fr.urssaf.image.sae.ordonnanceur.support;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ordonnanceur.util.HostUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-mock-test.xml"})
@SuppressWarnings("PMD.MethodNamingConventions")
public class CaptureMasseSupportTest {

   @Autowired
   private CaptureMasseSupport captureMasseSupport;

   private static final String CAPTURE_MASSE_JN = "capture_masse";

   private static final String PARAMETER_ECDE = "capture.masse.sommaire";

   private static final String CONTEXT_HOST = "serveur";

   private static final String HOST_VALUE;

   static {

      try {
         HOST_VALUE = HostUtils.getLocalHostName();
      } catch (UnknownHostException e) {
         throw new NestableRuntimeException(e);
      }
   }

   @Test
   public void filtrerJobInstanceLocal() {

      List<JobInstance> jobInstances = new ArrayList<JobInstance>();

      // traitement de capture en masse local
      jobInstances.add(createJobInstance(1, CAPTURE_MASSE_JN, PARAMETER_ECDE,
            "ecde://ecde.cer69.recouv/sommaire.xml"));
      jobInstances.add(createJobInstance(2, CAPTURE_MASSE_JN, PARAMETER_ECDE,
            "ecde://ecde.cer34.recouv/sommaire.xml"));
      // traitement de capture en masse non local
      jobInstances.add(createJobInstance(3, CAPTURE_MASSE_JN, PARAMETER_ECDE,
            "ecde://ecde.cer44.recouv/sommaire.xml"));
      // traitement de capture en masse sans configuration
      jobInstances.add(createJobInstance(4, CAPTURE_MASSE_JN, PARAMETER_ECDE,
            "ecde://ecde.cer78.recouv/sommaire.xml"));
      // autre traitement de masse
      jobInstances.add(createJobInstance(5, "OTHER_JN", "OTHER_PARAMETER1",
            "OTHER_VALUE1"));
      // traitement de capture en masse sans paramètre pour URL ECDE
      jobInstances.add(createJobInstance(6, CAPTURE_MASSE_JN,
            "OTHER_PARAMETER2", "OTHER_VALUE2"));
      // traitement de capture en masse avec paramètre pour URL ECDE erroné
      jobInstances.add(createJobInstance(7, CAPTURE_MASSE_JN, PARAMETER_ECDE,
            "ecde://azaz^^/sommaire.xml"));

      List<JobInstance> traitements = captureMasseSupport
            .filtrerJobInstanceLocal(jobInstances);

      Assert.assertEquals("le nombre de traitements filtrés est inattendu", 2,
            traitements.size());

      Assert.assertEquals("le job instance est inattendu", jobInstances.get(0),
            traitements.get(0));
      Assert.assertEquals("le job instance de capture est inattendu",
            jobInstances.get(1), traitements.get(1));
   }

   @Test
   public void filtrerJobExecutionLocal() {

      List<JobExecution> jobExecutions = new ArrayList<JobExecution>();

      // traitement de capture en masse local
      jobExecutions.add(createJobExecution(1, CAPTURE_MASSE_JN, CONTEXT_HOST,
            HOST_VALUE));
      jobExecutions.add(createJobExecution(2, CAPTURE_MASSE_JN, CONTEXT_HOST,
            HOST_VALUE));
      // traitement de capture en masse non local
      jobExecutions.add(createJobExecution(3, CAPTURE_MASSE_JN, CONTEXT_HOST,
            "ecde.cer44.recouv"));
      // traitement de capture en masse sans configuration
      jobExecutions.add(createJobExecution(4, CAPTURE_MASSE_JN, CONTEXT_HOST,
            "ecde.cer78.recouv"));
      // autre traitement de masse
      jobExecutions.add(createJobExecution(5, "OTHER_JN", CONTEXT_HOST,
            HOST_VALUE));
      // traitement de capture en masse sans host dans le context
      jobExecutions.add(createJobExecution(6, CAPTURE_MASSE_JN,
            "OTHER_PARAMETER2", HOST_VALUE));

      List<JobExecution> traitements = captureMasseSupport
            .filtrerJobExecutionLocal(jobExecutions);

      Assert.assertEquals("le nombre de traitements filtrés est inattendu", 2,
            traitements.size());

      Assert.assertEquals("le job execution est inattendu", jobExecutions
            .get(0), traitements.get(0));
      Assert.assertEquals("le job execution est inattendu", jobExecutions
            .get(1), traitements.get(1));
   }

   private JobInstance createJobInstance(long instanceId, String jobName,
         String parameterName, String parameterValue) {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put(parameterName, new JobParameter(parameterValue));

      JobParameters jobParameters = new JobParameters(parameters);

      JobInstance job = new JobInstance(instanceId, jobParameters, jobName);

      return job;
   }

   private JobExecution createJobExecution(long instanceId, String jobName,
         String contextName, String contextValue) {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      JobParameters jobParameters = new JobParameters(parameters);
      JobInstance job = new JobInstance(instanceId, jobParameters, jobName);

      JobExecution execution = new JobExecution(job);
      execution.getExecutionContext().put(contextName, contextValue);

      return execution;
   }

}
