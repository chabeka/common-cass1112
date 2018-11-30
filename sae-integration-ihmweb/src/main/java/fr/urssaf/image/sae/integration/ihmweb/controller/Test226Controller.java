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
@RequestMapping("test226")
public class Test226Controller extends
      AbstractTestWsController<Test22xFormulaire> {

   
   @Autowired
   private Test22xCommons testCommons;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "226";
   }
   

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test22xFormulaire getFormulairePourGet() {

      Test22xFormulaire formulaire = testCommons.getFormulairePourGet(getNumeroTest()) ;
      formulaire.getCaptureMasseDeclenchement().setAvecHash(Boolean.TRUE);
      formulaire.getCaptureMasseDeclenchement().setTypeHash("SHA-1");
      formulaire.getCaptureMasseDeclenchement().setHash("1992ea6c7a5761f302df88aee9339f0cd0cdeb42");
      
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
