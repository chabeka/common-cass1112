package fr.urssaf.image.commons.spring.batch.operator;

import java.io.File;
import java.util.UUID;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class Job1OperatorService {

   @Autowired
   private JobOperator jobOperator;

   @Autowired
   @Qualifier("job1")
   private Job job;

   @Autowired
   private JobRegistry registry;

   public Long start(File xmlPath, File filePath) {

      JobFactory jobFactory = new ReferenceJobFactory(job);
      try {
         registry.register(jobFactory);
      } catch (DuplicateJobException e) {
         throw new NestableRuntimeException(e);
      }

      String input = xmlPath.getAbsolutePath();
      String output = filePath.getAbsolutePath();
      String identifiant = UUID.randomUUID().toString();

      String jobName = job.getName();
      String parameters = "xml.input.location=" + input
            + ",file.output.location=" + output + ",id=" + identifiant;

      try {
         return jobOperator.start(jobName, parameters);

      } catch (NoSuchJobException e) {
         throw new NestableRuntimeException(e);
      } catch (JobInstanceAlreadyExistsException e) {
         throw new NestableRuntimeException(e);
      } catch (JobParametersInvalidException e) {
         throw new NestableRuntimeException(e);
      }

   }

   public boolean stop(long executionId) {

      try {
         return jobOperator.stop(executionId);
      } catch (NoSuchJobExecutionException e) {
         throw new NestableRuntimeException(e);
      } catch (JobExecutionNotRunningException e) {
         throw new NestableRuntimeException(e);
      }
   }

   public Long restart(long executionId) {

      JobFactory jobFactory = new ReferenceJobFactory(job);
      try {
         registry.register(jobFactory);
      } catch (DuplicateJobException e) {
         throw new NestableRuntimeException(e);
      }

      try {
         return jobOperator.restart(executionId);
      } catch (JobInstanceAlreadyCompleteException e) {
         throw new NestableRuntimeException(e);
      } catch (NoSuchJobExecutionException e) {
         throw new NestableRuntimeException(e);
      } catch (NoSuchJobException e) {
         throw new NestableRuntimeException(e);
      } catch (JobRestartException e) {
         throw new NestableRuntimeException(e);
      } catch (JobParametersInvalidException e) {
         throw new NestableRuntimeException(e);
      }
   }
}
