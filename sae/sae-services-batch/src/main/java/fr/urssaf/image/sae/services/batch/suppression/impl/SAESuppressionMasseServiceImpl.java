package fr.urssaf.image.sae.services.batch.suppression.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.suppression.SAESuppressionMasseService;

/**
 * Implémentation du service {@link SAESuppressionMasseService}
 */
@Service
public class SAESuppressionMasseServiceImpl implements SAESuppressionMasseService{
   
   /**
    * Executable du traitement de suppression de masse
    */
   @Autowired
   private JobLauncher jobLauncher;
   
   /**
    * Job de la suppression de masse
    */
   @Autowired
   @Qualifier("suppression_masse")
   private Job jobSuppression;
   
   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAESuppressionMasseServiceImpl.class);
   
   private final String TRC_SUPPRESSION = "suppressionMasse()";

   /**
    * {@inheritDoc}
    */
   @Override
   public ExitTraitement suppressionMasse(UUID idTraitement, String reqLucene) {
      Map<String, JobParameter> mapParam = new HashMap<String, JobParameter>();
      mapParam.put(Constantes.REQ_LUCENE_SUPPRESSION,
            new JobParameter(reqLucene));
      mapParam.put(Constantes.ID_TRAITEMENT, new JobParameter(idTraitement
            .toString()));

      JobParameters parameters = new JobParameters(mapParam);
      ExitTraitement exitTraitement = new ExitTraitement();
      JobExecution jobExecution = null;
      
      try {
         jobExecution = jobLauncher.run(jobSuppression, parameters);

         // TODO : gerer le retour d'execution du job
         exitTraitement.setExitMessage("Traitement " + TRC_SUPPRESSION + "réalisé avec succès");
         exitTraitement.setSucces(true);

         /* erreurs Spring non gérées */
      } catch (Throwable e) {

         LOGGER.warn(
               "{} - erreur lors de la suppression de masse. Exception levée",
               TRC_SUPPRESSION, e);

         List<Throwable> listThrowables = new ArrayList<Throwable>();
         listThrowables.add(e);
         // TODO : voir si cette gestion des exception suffit ou s'il faut faire une verif final

         exitTraitement.setExitMessage(e.getMessage());
         exitTraitement.setSucces(false);
      }
      
      return exitTraitement;
   }

}
