package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.TestDrCuCoCommons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireDrCuCo;

/**
 * 1117-Droits-Conformite-Consultation-UNE-META
 */
@Controller
@RequestMapping(value = "test1117a")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1117aController extends
      AbstractTestWsController<TestFormulaireDrCuCo> {
   
   @Autowired
   private TestDrCuCoCommons testCommons;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1117a";
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
