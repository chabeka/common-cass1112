package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestStockageMasseAllFormulaire;

/**
 * 216-CaptureMasse-OK-Tor-300000
 */
@Controller
@RequestMapping(value = "test216")
public class Test216Controller extends
      AbstractTestWsController<TestStockageMasseAllFormulaire> {

   
   @Autowired
   private Test21XCommons test21XCommons;
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "216";
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testCmReCo";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestStockageMasseAllFormulaire getFormulairePourGet() {

      return test21XCommons.getFormulairePourGet(getNumeroTest());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestStockageMasseAllFormulaire formulaire) {

      test21XCommons.doPost(formulaire);

   }


}
