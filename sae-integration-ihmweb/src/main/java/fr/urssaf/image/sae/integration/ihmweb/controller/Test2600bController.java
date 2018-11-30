package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.Test2600Commons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test2600Formulaire;

/**
 * 2600-IdGed-ArchivageUnitaire-OK-TestLibre-PJ-URL
 */
@Controller
@RequestMapping(value = "test2600b")
public class Test2600bController extends
      AbstractTestWsController<Test2600Formulaire> {

   
   @Autowired
   private Test2600Commons test2600Commons;
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2600b";
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
   protected final Test2600Formulaire getFormulairePourGet() {

      return test2600Commons.getFormulairePourGet(getNumeroTest());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test2600Formulaire formulaire) {

      test2600Commons.doPost(formulaire, getNumeroTest());

   }

}
