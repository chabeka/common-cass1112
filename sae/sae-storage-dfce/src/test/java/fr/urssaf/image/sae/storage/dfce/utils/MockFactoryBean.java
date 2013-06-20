/**
 * 
 */
package fr.urssaf.image.sae.storage.dfce.utils;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;

import org.easymock.EasyMock;


/**
 * Classe de factory pour créer les mocks
 * 
 */
public class MockFactoryBean {

   /**
    * Création d'un mock de Base
    * 
    * @return un mock Base
    */
   public final Base createBase() {
      return EasyMock.createMock(Base.class);
   }

   /**
    * Création d'un mock de BaseAdministrationService
    * 
    * @return un mock BaseAdministrationService
    */
   public final BaseAdministrationService createBaseAdministrationService() {
      return EasyMock.createMock(BaseAdministrationService.class);
   }

   /**
    * Création d'un mock de ServiceProvider
    * 
    * @return un mock ServiceProvider
    */
   public final ServiceProvider createServiceProvider() {
      return EasyMock.createMock(ServiceProvider.class);
   }
   
  
}
