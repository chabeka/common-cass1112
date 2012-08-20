package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test995Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.TestsTechniquesService;


/**
 * Test 995<br>
 * <br>
 * Tests techniques (opérations ping, pingSecure, et SoapFault d'authentification)
 */
@Controller
@RequestMapping(value = "test995")
public class Test995Controller extends AbstractTestWsController<Test995Formulaire> {

   
   @Autowired
   private TestsTechniquesService testsService;
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "995";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test995Formulaire getFormulairePourGet() {
      
      Test995Formulaire formulaire = new Test995Formulaire();

      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test995Formulaire formulaire) {
      lanceLesTests(formulaire);
   }
   
   
   private void lanceLesTests(Test995Formulaire formulaire) {
      
      // Efface les résultats des tests précédents
      formulaire.clearResultatsTests();
      
      // 001_Ping_ok
      testsService.casTest001(formulaire.getUrlServiceWeb(),formulaire.getTest001());
      
      // 002_PingSecure_ok
      testsService.casTest002(formulaire.getUrlServiceWeb(),formulaire.getTest002());
      
      // 003_PingSecure-SoapFault_wsse_SecurityTokenUnavailable
      testsService.casTest003(formulaire.getUrlServiceWeb(),formulaire.getTest003());
      
      // 004_PingSecure-SoapFault_wsse_InvalidSecurityToken
      testsService.casTest004(formulaire.getUrlServiceWeb(),formulaire.getTest004());
      
      // 005_PingSecure-SoapFault_wsse_FailedCheck
      testsService.casTest005(formulaire.getUrlServiceWeb(),formulaire.getTest005());
      
      // 006_PingSecure-SoapFault_vi_InvalidVI
      testsService.casTest006(formulaire.getUrlServiceWeb(),formulaire.getTest006());
      
      // 007_PingSecure-SoapFault_vi_InvalidService
      testsService.casTest007(formulaire.getUrlServiceWeb(),formulaire.getTest007());
      
      // 008_PingSecure-SoapFault_vi_InvalidAuthLevel
      testsService.casTest008(formulaire.getUrlServiceWeb(),formulaire.getTest008());
      
      // 010_PingSecure-SoapFault_sae_DroitsInsuffisants
//      testsService.casTest010(formulaire.getUrlServiceWeb(),formulaire.getTest010());
      
   }
   
   
   
   
   
}
