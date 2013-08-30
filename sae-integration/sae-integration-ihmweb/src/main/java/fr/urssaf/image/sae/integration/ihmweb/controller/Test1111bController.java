package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.TestDrCuCoCommons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireDrCuCo;

/**
 * 1111-Droits-Conformite-Consultation-MTOM-ATT-AEPL
 */
@Controller
@RequestMapping(value = "test1111b")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1111bController extends
      AbstractTestWsController<TestFormulaireDrCuCo> {
   
   @Autowired
   private TestDrCuCoCommons test1111Commons;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1111b";
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

      return test1111Commons.getFormulairePourGet(getNumeroTest());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestFormulaireDrCuCo formulaire) {
      test1111Commons.doPost(formulaire, getNumeroTest());
   }


}
