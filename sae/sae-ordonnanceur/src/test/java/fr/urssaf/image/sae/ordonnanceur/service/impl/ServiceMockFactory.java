package fr.urssaf.image.sae.ordonnanceur.service.impl;

import org.easymock.EasyMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.ordonnanceur.service.CoordinationService;
import fr.urssaf.image.sae.ordonnanceur.service.DecisionService;
import fr.urssaf.image.sae.ordonnanceur.service.JobFailureService;
import fr.urssaf.image.sae.ordonnanceur.service.JobService;
import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;
import fr.urssaf.image.sae.ordonnanceur.support.TraitementLauncherSupport;
import fr.urssaf.image.sae.ordonnanceur.support.TraitementMasseSupport;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;

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
    * @return instance de {@link TraitementMasseSupport}
    */
   public final TraitementMasseSupport createTraitementMasseSupport() {

      TraitementMasseSupport support = EasyMock
            .createMock(TraitementMasseSupport.class);

      return support;
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

   /**
    * 
    * @return instance de {@link JobFailureService}
    */
   public final JobFailureService createJobFailureService() {

      JobFailureService service = EasyMock.createMock(JobFailureService.class);

      return service;
   }

   /**
    * Création d'un "faux" dispatcheur de trace
    * 
    * @return le "faux" dispatcheur de trace
    */
   public final DispatcheurService createDispatcheurService() {

      return new DispatcheurService() {

         private final Logger logger = LoggerFactory.getLogger(this.getClass());
         
         @Override
         public void ajouterTrace(TraceToCreate trace) {

            logger.debug("Appel du faux dispatcheur pour tracer l'événement {}",
                  trace.getCodeEvt());

         }
      };

   }

}
