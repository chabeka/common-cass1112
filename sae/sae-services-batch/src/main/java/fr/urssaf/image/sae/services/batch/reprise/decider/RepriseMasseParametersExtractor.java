package fr.urssaf.image.sae.services.batch.reprise.decider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.step.job.JobParametersExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.services.batch.common.Constantes;

@Component
public class RepriseMasseParametersExtractor implements JobParametersExtractor {

   @Autowired
   private JobLectureService jobLectureService;

   @Override
   public JobParameters getJobParameters(Job job, StepExecution stepExecution) {

      // Récupérer l'idJobAReprendre
      String idJobAReprendreParam = null;
      String idJobSuppression = null;
      JobParameters params = stepExecution.getJobExecution().getJobInstance()
            .getJobParameters();
      if(params != null){
         idJobAReprendreParam = params
               .getString(Constantes.ID_TRAITEMENT_A_REPRENDRE_BATCH);

         idJobSuppression = params
               .getString(Constantes.ID_TRAITEMENT_SUPPRESSION);
      }

      UUID idJobAReprendre = UUID.fromString(idJobAReprendreParam);
      JobRequest jobAReprendre = jobLectureService
            .getJobRequest(idJobAReprendre);

      if (jobAReprendre != null) {
         Map<String, String> mapParam = jobAReprendre.getJobParameters();
         Map<String, JobParameter> jobParameters = new HashMap<String, JobParameter>();

         for (Map.Entry<String, String> entry : mapParam.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(Constantes.ECDE_URL)) {
               jobParameters.put(Constantes.SOMMAIRE,
                     new JobParameter(entry.getValue()));
            } else if (entry.getKey().equalsIgnoreCase(fr.urssaf.image.sae.commons.utils.Constantes.HEURE_REPRISE)) {
               // Permet de distinguer le traitement de reprise en cours si il a été déjà lancé
               jobParameters
               .put(fr.urssaf.image.sae.commons.utils.Constantes.HEURE_REPRISE,
                     new JobParameter(Long.getLong(entry.getValue())));
            } else {
               jobParameters.put(entry.getKey(),
                     new JobParameter(entry.getValue()));
            }
         }

         if (idJobSuppression != null && !idJobSuppression.isEmpty()) {
            jobParameters.put(Constantes.ID_TRAITEMENT_SUPPRESSION,
                  new JobParameter(idJobSuppression));
         }

         jobParameters.put(Constantes.ID_TRAITEMENT,
               new JobParameter(idJobAReprendreParam));

         jobParameters.put(Constantes.TRAITEMENT_REPRISE,
               new JobParameter(Boolean.TRUE.toString()));

         JobParameters parameters = new JobParameters(jobParameters);
         return parameters;
      }
      return null;
   }

}
