package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.Test103Commons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test103Formulaire;

/**
 * 103-CaptureUnitaire-OK-ToutesMetasSpecifiables-PJ-URL
 */
@Controller
@RequestMapping(value = "test103b")
public class Test103bController extends
      AbstractTestWsController<Test103Formulaire> {

   
   @Autowired
   private Test103Commons test103Commons;
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "103b";
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
   protected final Test103Formulaire getFormulairePourGet() {

      return test103Commons.getFormulairePourGet(getNumeroTest());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test103Formulaire formulaire) {

      test103Commons.doPost(formulaire, getNumeroTest());

   }

}
