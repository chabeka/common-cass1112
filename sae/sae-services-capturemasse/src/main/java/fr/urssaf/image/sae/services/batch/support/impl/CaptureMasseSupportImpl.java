package fr.urssaf.image.sae.services.batch.support.impl;

import java.net.URI;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.exception.JobParameterTypeException;
import fr.urssaf.image.sae.services.batch.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.support.TraitementExecutionSupport;

/**
 * Implémentation du service {@link TraitementExecutionSupport} pour la capture
 * en masse
 * 
 * 
 */
@Component
@Qualifier("captureMasseTraitement")
public class CaptureMasseSupportImpl implements TraitementExecutionSupport {

   @Autowired
   private SAECaptureMasseService captureMasseService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final ExitTraitement execute(JobRequest job) {

      Assert
            .notNull(
                  this.captureMasseService,
                  "Il n'existe aucune configuration pour instancier le composant 'SAECaptureMasseService'");

      Assert.notNull(job, "'job' is required");

      UUID idTraitement = job.getIdJob();
      ExitTraitement exitTraitement = null;

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

      if (MapUtils.isNotEmpty(job.getJobParameters())) {
         exitTraitement = captureMasseService.captureMasse(sommaireURL,
               idTraitement, job.getJobParameters().get(Constantes.HASH), job
                     .getJobParameters().get(Constantes.TYPE_HASH));
      } else {
         exitTraitement = captureMasseService.captureMasse(sommaireURL,
               idTraitement);
      }

      return exitTraitement;

   }

}
