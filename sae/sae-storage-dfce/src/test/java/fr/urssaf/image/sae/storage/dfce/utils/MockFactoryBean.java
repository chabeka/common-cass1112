/**
 *
 */
package fr.urssaf.image.sae.storage.dfce.utils;

import org.easymock.EasyMock;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import net.docubase.toolkit.model.base.Base;


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
    * Création d'un mock de dfceServices
    *
    * @return un mock dfceServices
    */
   public final DFCEServices createDFCESercices() {
      return EasyMock.createMock(DFCEServices.class);
   }

}
