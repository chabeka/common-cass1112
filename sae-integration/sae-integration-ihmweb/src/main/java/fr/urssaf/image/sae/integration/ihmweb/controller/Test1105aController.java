package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.controller.commons.Test1105Commons;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test1105Formulaire;

/**
 * 1105-Droits-Conformite-Consultation-ATT-VIG
 */
@Controller
@RequestMapping(value = "test1105a")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1105aController extends
      AbstractTestWsController<Test1105Formulaire> {
   
   @Autowired
   private Test1105Commons test1105Commons;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1105a";
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
   protected final Test1105Formulaire getFormulairePourGet() {

      return test1105Commons.getFormulairePourGet(getNumeroTest());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test1105Formulaire formulaire) {
      test1105Commons.doPost(formulaire);
   }


}
