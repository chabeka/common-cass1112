package fr.urssaf.image.sae.services.batch.support.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.restore.SAERestoreMasseService;
import fr.urssaf.image.sae.services.batch.support.TraitementExecutionSupport;

/**
 * Implémentation du service {@link TraitementExecutionSupport} 
 * pour la restore de masse
 */
@Component
@Qualifier("restoreMasseTraitement")
public class RestoreMasseSupportImpl implements TraitementExecutionSupport {

   @Autowired
   private SAERestoreMasseService restoreMasseService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final ExitTraitement execute(JobRequest job) {

      Assert
            .notNull(
                  this.restoreMasseService,
                  "Il n'existe aucune configuration pour instancier le composant 'SAERestoreMasseService'");

      Assert.notNull(job, "'job' is required");

      String idTraitement = job.getJobParameters().get(Constantes.ID_TRAITEMENT_A_RESTORER);
      ExitTraitement exitTraitement = null;
      exitTraitement = restoreMasseService.restoreMasse(job.getIdJob(), UUID.fromString(idTraitement));

      return exitTraitement;
   }
}
