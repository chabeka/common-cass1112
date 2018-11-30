package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.Test22xCommons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test22xFormulaire;

/**
 * Test 225, 226, 227 et 228
 */
@Controller
@RequestMapping("test228")
public class Test228Controller extends
      AbstractTestWsController<Test22xFormulaire> {

   
   @Autowired
   private Test22xCommons testCommons;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "228";
   }
   

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test22xFormulaire getFormulairePourGet() {

      Test22xFormulaire formulaire = testCommons.getFormulairePourGet(getNumeroTest()) ;
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test22xFormulaire formulaire) {

      testCommons.doPost(formulaire, getNumeroTest());

   }
}
