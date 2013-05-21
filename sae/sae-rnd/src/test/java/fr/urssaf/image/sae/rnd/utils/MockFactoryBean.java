/**
 * 
 */
package fr.urssaf.image.sae.rnd.utils;

import net.docubase.toolkit.model.reference.LifeCycleRule;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

import org.easymock.EasyMock;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.rnd.ws.adrn.service.RndRecuperationService;

/**
 * Classe de factory pour créer les mocks
 * 
 */
public class MockFactoryBean {

   /**
    * Création d'un mock de ServiceProvider
    * 
    * @return un mock ServiceProvider
    */
   public final ServiceProvider createServiceProvider() {
      return EasyMock.createMock(ServiceProvider.class);
   }

   /**
    * Création d'un mock de DFCEConnectionService
    * 
    * @return un mock DFCEConnectionService
    */
   public final DFCEConnectionService createDFCEConnectionService() {
      return EasyMock.createMock(DFCEConnectionService.class);
   }

   /**
    * Création d'un mock de StorageAdministrationService
    * 
    * @return un mock StorageAdministrationService
    */
   public final StorageAdministrationService createStorageAdministrationService() {
      return EasyMock.createMock(StorageAdministrationService.class);
   }

   /**
    * Création d'un mock de LifeCycleRule
    * 
    * @return un mock LifeCycleRule
    */
   public final LifeCycleRule createLifeCycleRule() {
      return EasyMock.createMock(LifeCycleRule.class);
   }

   /**
    * Création d'un mock {@link RndRecuperationService}
    * 
    * @return un mock {@link RndRecuperationService}s
    */
   public final RndRecuperationService createRndRecuperationService() {
      return EasyMock.createMock(RndRecuperationService.class);
   }
}
