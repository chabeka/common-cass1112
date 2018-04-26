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

      String prefixeLog = "call()";

      LOG
            .debug(
                  "{} - Début d'une passe d'analyse de la pile des travaux et de lancement d'un job en attente",
                  prefixeLog);
      CoordinationService service = context.getBean(CoordinationService.class);

      UUID idJob = null;
      try {

         idJob = service.lancerTraitement();

      } catch (AucunJobALancerException e) {

         LOG.debug("{} - Il n'y a aucun traitement de masse à lancer",
               prefixeLog);

      } catch (JobRuntimeException e) {

         LOG.debug("{} - Il n'y a aucun traitement de masse à lancer",
               prefixeLog);

         // on mémorise l'échec du traitement
         jobFailureService.ajouterEchec(e.getJob().getIdJob(), e);

      } catch (RuntimeException e) {

         LOG.error(prefixeLog + " - Une erreur technique a eu lieu", e);
      }

      LOG
            .debug(
                  "{} - Fin d'une passe d'analyse de la pile des travaux et de lancement d'un job en attente",
                  prefixeLog);
      return idJob;
   }

}
