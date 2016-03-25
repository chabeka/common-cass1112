package fr.urssaf.image.sae.services.batch.restore.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.restore.SAERestoreMasseService;

/**
 * Implémentation du service {@link SAERestoreMasseService}
 */
@Service
public class SAERestoreMasseServiceImpl implements SAERestoreMasseService {

   /**
    * Executable du traitement de suppression de masse
    */
   @Autowired
   private JobLauncher jobLauncher;
   
   /**
    * Service de gestion de la pile des travaux.
    */
   @Autowired
   private JobQueueService jobQueueService;
   
   /**
    * Job de la restauration de documents 
    */
   @Autowired
   @Qualifier("restore_masse")
   private Job jobRestore;
   
   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAERestoreMasseServiceImpl.class);

   private final String TRC_RESTORE = "restoreMasse()";
   
   /**
    * {@inheritDoc}
    */
   @Override
   public ExitTraitement restoreMasse(UUID idTraitementRestore, UUID idTraitementSuppression) {
      Map<String, JobParameter> mapParam = new HashMap<String, JobParameter>();
      mapParam.put(Constantes.ID_TRAITEMENT_A_RESTORER,
            new JobParameter(idTraitementSuppression.toString()));
      mapParam.put(Constantes.ID_TRAITEMENT_RESTORE, new JobParameter(idTraitementRestore
            .toString()));

      JobParameters parameters = new JobParameters(mapParam);
      ExitTraitement exitTraitement = new ExitTraitement();
      JobExecution jobExecution = null;
      
      try {
         jobExecution = jobLauncher.run(jobRestore, parameters);

         if (ExitStatus.COMPLETED.equals(jobExecution.getExitStatus())) {
            exitTraitement.setExitMessage("Traitement réalisé avec succès");
            exitTraitement.setSucces(true);
         } else {

            exitTraitement.setExitMessage("Traitement en erreur");
            exitTraitement.setSucces(false);
         }
         
         // met a jour le job pour renseigner le nombre de docs restorés
         int nbDocsRestores = 0;
         if (jobExecution.getExecutionContext().containsKey(
               Constantes.NB_DOCS_RESTORES)) {
            nbDocsRestores = jobExecution.getExecutionContext().getInt(
                  Constantes.NB_DOCS_RESTORES);
         }
         jobQueueService.renseignerDocCountJob(idTraitementRestore, nbDocsRestores);

         /* erreurs Spring non gérées */
      } catch (Throwable e) {

         LOGGER.warn(
               "{} - erreur lors de la restore de masse. Exception levée",
               TRC_RESTORE, e);

         List<Throwable> listThrowables = new ArrayList<Throwable>();
         listThrowables.add(e);

         exitTraitement.setExitMessage(e.getMessage());
         exitTraitement.setSucces(false);
      }
      
      return exitTraitement;
   }

}
