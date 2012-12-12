/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.executable.service.TraitementService;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.PurgeService;

/**
 * Classe d'implémentation du support {@link TraitementService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class TraitementServiceImpl implements TraitementService {

   @Autowired
   private PurgeService purgeService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void purgerRegistre(PurgeType purgeType) {

      purgeService.purgerRegistre(purgeType);

   }

}
