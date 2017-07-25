package fr.urssaf.image.sae.services.batch.reprise.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.services.batch.common.Constantes;

@Component
public class RepriseMasseDecider implements JobExecutionDecider {

   private static final String REPRISE_MODIFICATION = "REPRISE_MODIFICATION";
   private static final String REPRISE_CAPTURE = "REPRISE_CAPTURE";
   private static final String REPRISE_TRANSFERT = "REPRISE_TRANSFERT";
   private static final String REPRISE_RESTORE = "REPRISE_RESTORE";
   private static final String REPRISE_SUPPRESSION = "REPRISE_SUPPRESSION";

   @Autowired
   private JobLectureService jobLectureService;

   @Override
   public FlowExecutionStatus decide(JobExecution jobExecution,
         StepExecution stepExecution) {

      if(jobExecution.getJobInstance()!= null && jobExecution.getJobInstance().getJobParameters() != null ){
         String typeJobAReprendre = jobExecution.getJobInstance().getJobParameters().getString(Constantes.TYPE_TRAITEMENT_A_REPRENDRE);

         if (TYPES_JOB.modification_masse.name().equals(typeJobAReprendre)) {
            return new FlowExecutionStatus(REPRISE_MODIFICATION);
         } else if (TYPES_JOB.transfert_masse.name().equals(typeJobAReprendre)) {
            return new FlowExecutionStatus(REPRISE_TRANSFERT);
         } else if (TYPES_JOB.capture_masse.name().equals(typeJobAReprendre)) {
            return new FlowExecutionStatus(REPRISE_CAPTURE);
         } else if (TYPES_JOB.suppression_masse.name().equals(typeJobAReprendre)) {
            return new FlowExecutionStatus(REPRISE_SUPPRESSION);
         } else if (TYPES_JOB.restore_masse.name().equals(typeJobAReprendre)) {
            return new FlowExecutionStatus(REPRISE_RESTORE);
         } else {
            throw new RuntimeException("Reprise impossible: Type de traitement de masse inconnu");
         }
      }else {
         throw new RuntimeException("Reprise impossible pour ce traitement");
      }

   }

}
