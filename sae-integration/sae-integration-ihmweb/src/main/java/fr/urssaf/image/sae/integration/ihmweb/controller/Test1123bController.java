package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.TestDrCuCoCommons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireDrCuCo;

/**
 * 1123-Droits-Conformite-Consultation-MTOM-PLUSIEURS-META
 */
@Controller
@RequestMapping(value = "test1123b")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1123bController extends
      AbstractTestWsController<TestFormulaireDrCuCo> {
   
   @Autowired
   private TestDrCuCoCommons testCommons;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1123b";
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
