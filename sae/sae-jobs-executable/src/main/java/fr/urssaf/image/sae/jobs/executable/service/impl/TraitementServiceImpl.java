/**
 * 
 */
package fr.urssaf.image.sae.jobs.executable.service.impl;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.jobs.executable.model.JobsConfiguration;
import fr.urssaf.image.sae.jobs.executable.service.TraitementService;
import fr.urssaf.image.sae.pile.travaux.service.OperationPileTravauxService;

/**
 * Classe d'implémentation de TraitementService
 * Classe singleton, accessible par l'annotation @Autowired
 * 
 */
@Service
public class TraitementServiceImpl implements TraitementService {

   /**
    * Service permettant de réaliser les opérations sur les jobs
    */
   @Autowired
   private OperationPileTravauxService operationPileTravauxService;
   
   /**
    * Objet de configuration des traitements sur les objets
    */
   @Autowired
   private JobsConfiguration jobsConfiguration;
   
   /* (non-Javadoc)
    * @see fr.urssaf.image.sae.jobs.executable.service.TraitementService#purger()
    */
   public void purger() {

      Integer dureeConservation = jobsConfiguration.getJobsDureeConservation();
      
      // Calcul de la date maximale de suppression des jobs
      Date dateJour = new Date();
      Date dateMaxSuppression = DateUtils.addDays(dateJour, - dureeConservation);
      
      operationPileTravauxService.purger(dateMaxSuppression);
      
   }

}
