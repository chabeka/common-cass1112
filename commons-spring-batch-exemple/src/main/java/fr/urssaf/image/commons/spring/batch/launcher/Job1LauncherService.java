package fr.urssaf.image.commons.spring.batch.launcher;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class Job1LauncherService {

   @Autowired
   private JobLauncher jobLauncher;

   @Autowired
   @Qualifier("job1")
   private Job job;

   public String launch(File xmlPath, File filePath) {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("xml.input.location", new JobParameter(xmlPath
            .getAbsolutePath()));
      parameters.put("file.output.location", new JobParameter(filePath
            .getAbsolutePath()));
      parameters.put("id", new JobParameter(UUID.randomUUID().toString()));

      JobParameters jobParameters = new JobParameters(parameters);

      JobExecution jobExecution;

      try {
         jobExecution = jobLauncher.run(job, jobParameters);
      } catch (JobExecutionAlreadyRunningException e) {

         throw new NestableRuntimeException(e);

      } catch (JobRestartException e) {

         throw new NestableRuntimeException(e);

      } catch (JobInstanceAlreadyCompleteException e) {

         throw new NestableRuntimeException(e);

      } catch (JobParametersInvalidException e) {

         throw new IllegalArgumentException(e.getMessage(), e);
      }

      return jobExecution.getExitStatus().getExitCode();

   }
}
