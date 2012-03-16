package fr.urssaf.image.sae.ordonnanceur.service.impl;

import org.easymock.EasyMock;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.ordonnanceur.service.CoordinationService;
import fr.urssaf.image.sae.ordonnanceur.service.DecisionService;
import fr.urssaf.image.sae.ordonnanceur.service.JobService;
import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;
import fr.urssaf.image.sae.ordonnanceur.support.TraitementLauncherSupport;

/**
 * Implémentation des Mocks des services
 * 
 * 
 */
@Component
public class ServiceMockFactory {

   /**
    * 
    * @return instance de {@link JobService}
    */
   public final JobService createJobService() {

      JobService service = EasyMock.createMock(JobService.class);

      return service;
   }

   /**
    * 
    * @return instance de {@link DecisionService}
    */
   public final DecisionService createDecisionService() {

      DecisionService service = EasyMock.createMock(DecisionService.class);

      return service;

   }

   /**
    * 
    * @return instance de {@link TraitementLauncherSupport}
    */
   public final TraitementLauncherSupport createTraitementLauncherSupport() {

      TraitementLauncherSupport service = EasyMock
            .createMock(TraitementLauncherSupport.class);

      return service;

   }

   /**
    * 
    * @return instance de {@link CoordinationService}
    */
   public final CoordinationService createCoordinationService() {

      CoordinationService service = EasyMock
            .createMock(CoordinationService.class);

      return service;
   }

   /**
    * 
    * @return instance de {@link DFCESupport}
    */
   public final DFCESupport createDFCESupport() {

      DFCESupport service = EasyMock.createMock(DFCESupport.class);

      return service;
   }

}
