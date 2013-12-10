package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.Test1156Commons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireDrCuCo;

/**
 * 1156-Droits-Consultation-MTOM-KO-PRMD-DYNA-INNACCESSIBLE
 */
@Controller
@RequestMapping(value = "test1156b")
public class Test1156bController extends
      AbstractTestWsController<TestFormulaireDrCuCo> {
   
   @Autowired
   private Test1156Commons testCommons;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1156b";
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
