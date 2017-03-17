package fr.urssaf.image.sae.services.batch.support.impl;

import java.net.URI;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.exception.JobParameterTypeException;
import fr.urssaf.image.sae.services.batch.modification.SAEModificationMasseService;
import fr.urssaf.image.sae.services.batch.support.TraitementExecutionSupport;

/**
 * Implémentation du service {@link TraitementExecutionSupport} pour la
 * modification de masse
 */
@Component
@Qualifier("modificationMasseTraitement")
public class ModificationMasseSupportImpl implements TraitementExecutionSupport {

   @Autowired
   private SAEModificationMasseService modificationMasseService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final ExitTraitement execute(JobRequest job) {

      Assert
            .notNull(
                  this.modificationMasseService,
                  "Il n'existe aucune configuration pour instancier le composant 'SAEModificationMasseService'");

      Assert.notNull(job, "'job' is required");

      UUID idTraitement = job.getIdJob();

      Assert.notNull(idTraitement, "'identifiant job' is required");

      // le paramètre stocké dans la pile des travaux correspond pour les
      // traitements de capture en masse à l'URL ECDE
      String urlECDE = StringUtils.EMPTY;
      if (StringUtils.isNotBlank(job.getParameters())) {
         urlECDE = job.getParameters();
      } else {
         urlECDE = job.getJobParameters().get(Constantes.ECDE_URL);
      }

      URI sommaireURL;

      try {
         sommaireURL = URI.create(urlECDE);

      } catch (IllegalArgumentException e) {

         // cas où ecdeParameter ne respecte pas RFC 2396
         // (Cf. http://www.ietf.org/rfc/rfc2396.txt)

         throw new JobParameterTypeException(job, e);

      }

      return modificationMasseService.modificationMasse(sommaireURL,
            idTraitement, job.getJobParameters().get(Constantes.HASH), job
                  .getJobParameters().get(Constantes.TYPE_HASH));
   }
}
