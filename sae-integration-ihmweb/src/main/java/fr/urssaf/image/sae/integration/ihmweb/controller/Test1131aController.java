package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.Test1131Commons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireDrCuCo;

/**
 * 1131-Droits-Conformite-Consultation-PRMD-DYNA
 */
@Controller
@RequestMapping(value = "test1131a")
public class Test1131aController extends
      AbstractTestWsController<TestFormulaireDrCuCo> {
   
   @Autowired
   private Test1131Commons testCommons;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1131a";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testDrCuCo";
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestFormulaireDrCuCo getFormulairePourGet() {

      return testCommons.getFormulairePourGet(getNumeroTest());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestFormulaireDrCuCo formulaire) {
      testCommons.doPost(formulaire, getNumeroTest());
   }


}
