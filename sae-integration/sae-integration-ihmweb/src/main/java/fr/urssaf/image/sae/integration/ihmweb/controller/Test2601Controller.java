package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import fr.urssaf.image.sae.integration.ihmweb.controller.commons.Test2601Commons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test2601Formulaire;

/**
 * 2601-IdGed-Recherche-OK-IdGed
 */
@Controller
@RequestMapping(value = "test2601")
public class Test2601Controller extends
      AbstractTestWsController<Test2601Formulaire> {

   
   @Autowired
   private Test2601Commons test2601Commons;
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2601";
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
   protected final Test2601Formulaire getFormulairePourGet() {

      return test2601Commons.getFormulairePourGet(getNumeroTest());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test2601Formulaire formulaire) {

      test2601Commons.doPost(formulaire, getNumeroTest());

   }

}
