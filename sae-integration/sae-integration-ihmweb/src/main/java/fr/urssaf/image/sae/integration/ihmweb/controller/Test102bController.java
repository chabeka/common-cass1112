package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.Test102Commons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test102Formulaire;

/**
 * 102-CaptureUnitaire-OK-EnrichissementEcrasement-PJ-URL
 */
@Controller
@RequestMapping(value = "test102b")
public class Test102bController extends
      AbstractTestWsController<Test102Formulaire> {

   
   @Autowired
   private Test102Commons test102Commons;
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "102b";
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testCuRe";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test102Formulaire getFormulairePourGet() {

      return test102Commons.getFormulairePourGet(getNumeroTest());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test102Formulaire formulaire) {

      test102Commons.doPost(formulaire, getNumeroTest());

   }

}
