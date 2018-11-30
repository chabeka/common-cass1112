package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.Test104Commons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test104Formulaire;

/**
 * 104-CaptureUnitaire-OK-Sans-Code-Activite
 */
@Controller
@RequestMapping(value = "test104a")
public class Test104aController extends
      AbstractTestWsController<Test104Formulaire> {

   
   @Autowired
   private Test104Commons test104Commons;
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "104a";
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
   protected final Test104Formulaire getFormulairePourGet() {

      return test104Commons.getFormulairePourGet(getNumeroTest());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test104Formulaire formulaire) {

      test104Commons.doPost(formulaire);

   }

}
