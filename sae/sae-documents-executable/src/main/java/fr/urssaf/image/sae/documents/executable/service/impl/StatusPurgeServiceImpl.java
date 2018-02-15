package fr.urssaf.image.sae.documents.executable.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.documents.executable.service.StatusPurgeService;

/**
 * Classe d'implémentation de l'interface {@link StatusPurgeService}. Cette classe
 * est un singleton et peut être accessible via le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class StatusPurgeServiceImpl implements StatusPurgeService {

   // Service de gestion des paramètres
   @Autowired
   private ParametersService paramService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean isPurgeRunning() {

      try {

         return paramService.isPurgeCorbeilleIsRunning();

      } catch (ParameterNotFoundException e) {
         return false;
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void updatePurgeStatus(Boolean value) {

      paramService.setPurgeCorbeilleIsRunning(value);

   }

}
