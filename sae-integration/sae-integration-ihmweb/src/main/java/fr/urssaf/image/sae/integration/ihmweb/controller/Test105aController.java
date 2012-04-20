package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.Test105Commons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test105Formulaire;

/**
 * 105-CaptureUnitaire-OK-HashMajMin
 */
@Controller
@RequestMapping(value = "test105a")
public class Test105aController extends
      AbstractTestWsController<Test105Formulaire> {

   
   @Autowired
   private Test105Commons test105Commons;
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "105a";
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testCuCo";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test105Formulaire getFormulairePourGet() {

      return test105Commons.getFormulairePourGet(getNumeroTest());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test105Formulaire formulaire) {

      test105Commons.doPost(formulaire);

   }

}
