package fr.urssaf.image.sae.services.batch.support.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.support.TraitementExecutionSupport;
import fr.urssaf.image.sae.services.batch.supression.SAESupressionMasseService;

/**
 * Implémentation du service {@link TraitementExecutionSupport} 
 * pour la suppression de masse
 */
@Component
@Qualifier("suppressionMasseTraitement")
public class SuppressionMasseSupportImpl implements TraitementExecutionSupport {

   @Autowired
   private SAESupressionMasseService suppressionMasseService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final ExitTraitement execute(JobRequest job) {

      Assert
            .notNull(
                  this.suppressionMasseService,
                  "Il n'existe aucune configuration pour instancier le composant 'SAESupressionMasseService'");

      Assert.notNull(job, "'job' is required");

      UUID idTraitement = job.getIdJob();
      String reqLucene = job.getJobParameters().get(Constantes.REQ_LUCENE_SUPPRESSION);
      
      ExitTraitement exitTraitement = null;
      exitTraitement = suppressionMasseService.supressionMasse(idTraitement, reqLucene);

      return exitTraitement;
   }
}
