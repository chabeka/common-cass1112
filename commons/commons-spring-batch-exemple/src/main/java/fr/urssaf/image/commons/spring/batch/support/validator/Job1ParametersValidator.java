package fr.urssaf.image.commons.spring.batch.support.validator;

import java.io.File;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

@Component
public class Job1ParametersValidator implements JobParametersValidator {

   @Override
   public void validate(JobParameters parameters)
         throws JobParametersInvalidException {

      String xmlPath = parameters.getString("xml.input.location");

      File xmlFile = new File(xmlPath);
      if (!xmlFile.isFile()) {

         throw new JobParametersInvalidException(xmlPath + " n'existe pas!");
      }

   }

}
