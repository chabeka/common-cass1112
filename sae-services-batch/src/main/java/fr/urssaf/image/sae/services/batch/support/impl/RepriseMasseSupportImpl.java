package fr.urssaf.image.sae.services.batch.support.impl;

import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.reprise.SAERepriseMasseService;
import fr.urssaf.image.sae.services.batch.support.TraitementExecutionSupport;

/**
 * Implémentation du service {@link TraitementExecutionSupport} pour la reprise
 * de traitements en masse
 * 
 */
@Component
@Qualifier("repriseMasseTraitement")
public class RepriseMasseSupportImpl implements TraitementExecutionSupport {

   @Autowired
   private SAERepriseMasseService repriseMasseService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final ExitTraitement execute(JobRequest job) {

      Assert.notNull(job, "le job est requis ");

      UUID idJobReprise = job.getIdJob();

      ExitTraitement exitTraitement = null;

      if (job.getType().equals(TYPES_JOB.reprise_masse.name())) {
         Assert.notNull(
               this.repriseMasseService,
               "Il n'existe aucune configuration pour instancier le composant 'SAERepriseMasseService'");

         if (MapUtils.isNotEmpty(job.getJobParameters())) {
            if (StringUtils.isNotEmpty(job.getJobParameters().get(Constantes.UUID_JOB_A_Reprendre))) {
               exitTraitement = repriseMasseService.repriseMasse(idJobReprise);
            }

         }
      }
      return exitTraitement;

   }

}
