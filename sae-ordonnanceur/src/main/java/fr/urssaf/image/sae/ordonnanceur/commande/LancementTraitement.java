package fr.urssaf.image.sae.ordonnanceur.commande;

import java.util.UUID;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.exception.JobRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.service.CoordinationService;
import fr.urssaf.image.sae.ordonnanceur.service.JobFailureService;

/**
 * Opération du lancement des traitements de masse.<br>
 * 
 * @see CoordinationService#lancerTraitement()
 * 
 * 
 */
public class LancementTraitement implements Callable<UUID> {

   private static final Logger LOG = LoggerFactory
         .getLogger(LancementTraitement.class);

   private static final String PREFIX_LOG = "LancementTraitement";

   private final ApplicationContext context;

   private final JobFailureService jobFailureService;

   /**
    * 
    * @param context
    *           contexte de l'application
    */
   public LancementTraitement(ApplicationContext context) {
      Assert.notNull(context, "'context' is required");
      this.context = context;
      this.jobFailureService = context.getBean(JobFailureService.class);
   }

   /**
    * L'implémentation appelle le service de lancement d'un traitement de masse
    * lors de l'exécution du Thread.
    * 
    * @return identifiant du traitement de masse, null si aucun traitement n'a
    *         été exécuté.
    */
   @Override
   public UUID call() {
      LOG.debug(
            "{} - début de l'opération du lancement d'un traitement de masse ",
            PREFIX_LOG);
      CoordinationService service = context.getBean(CoordinationService.class);

      UUID idJob = null;
      try {

         idJob = service.lancerTraitement();

      } catch (AucunJobALancerException e) {

         LOG.debug("{} - il n'y a aucun traitement à lancer", PREFIX_LOG);

      } catch (JobRuntimeException e) {

         LOG.debug("{} - il n'y a aucun traitement à lancer", PREFIX_LOG);

         // on mémorise l'échec du traitement
         jobFailureService.ajouterEchec(e.getJob().getIdJob(), e);

      }

      LOG.debug(
            "{} - fin de l'opération du lancement d'un traitement de masse ",
            PREFIX_LOG);
      return idJob;
   }

}
