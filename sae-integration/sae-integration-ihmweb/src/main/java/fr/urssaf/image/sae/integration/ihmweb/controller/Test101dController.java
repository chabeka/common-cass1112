package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.Test101Commons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test101Formulaire;

/**
 * Test 101-CaptureUnitaire-OK-Standard-PJ-avec-MTOM
 */
@Controller
@RequestMapping(value = "test101d")
public class Test101dController extends
      AbstractTestWsController<Test101Formulaire> {

   
   @Autowired
   private Test101Commons test101Commons;
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "101d";
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testCuReCo";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test101Formulaire getFormulairePourGet() {

      return test101Commons.getFormulairePourGet(getNumeroTest());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test101Formulaire formulaire) {

      test101Commons.doPost(formulaire, getNumeroTest());

   }

}
