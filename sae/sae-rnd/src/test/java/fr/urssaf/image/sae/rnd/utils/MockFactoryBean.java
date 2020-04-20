/**
 *
 */
package fr.urssaf.image.sae.rnd.utils;

import org.easymock.EasyMock;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.rnd.ws.adrn.service.RndRecuperationService;
import net.docubase.toolkit.model.reference.LifeCycleRule;
import net.docubase.toolkit.model.reference.LifeCycleStep;

/**
 * Classe de factory pour créer les mocks
 *
 */
public class MockFactoryBean {

   /**
    * Création d'un mock de LifeCycleRule
    *
    * @return un mock LifeCycleRule
    */
   public final LifeCycleRule createLifeCycleRule() {
      return EasyMock.createMock(LifeCycleRule.class);
   }

   /**
    * Création d'un mock de LifeCycleStep
    *
    * @return un mock LifeCycleStep
    */
   public final LifeCycleStep createLifeCycleStep() {
      return EasyMock.createMock(LifeCycleStep.class);
   }

   /**
    * Création d'un mock {@link RndRecuperationService}
    *
    * @return un mock {@link RndRecuperationService}s
    */
   public final RndRecuperationService createRndRecuperationService() {
      return EasyMock.createMock(RndRecuperationService.class);
   }

   /**
    * Création d'un mock {@link DFCEServices}
    *
    * @return un mock {@link DFCEServicess}
    */
   public final DFCEServices createDFCESercices() {
      return EasyMock.createMock(DFCEServices.class);
   }
}
