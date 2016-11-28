package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.RestoreMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsRestoreMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.service.SaeServiceTestService;


/**
 * 3100-RestoreMasse-OK-SansAuthentification
 */
@Controller
@RequestMapping(value = "test3150")
public class Test3150Controller extends AbstractTestWsController<TestWsRestoreMasseFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "3150";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsRestoreMasseFormulaire getFormulairePourGet() {
      
      TestWsRestoreMasseFormulaire formulaire = new TestWsRestoreMasseFormulaire();
      RestoreMasseFormulaire formRestoreMasse = formulaire.getRestoreMasse();
      formRestoreMasse.setIdArchivage(SaeServiceTestService.getIdArchivageExemple());
      
      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsRestoreMasseFormulaire formulaire) {
      restoreMasse(
            formulaire.getUrlServiceWeb(),
            formulaire.getRestoreMasse());
   }
   
   
   
   private void restoreMasse(
         String urlServiceWeb,
         RestoreMasseFormulaire formulaire) {
      
      // Appel de la méthode de test
      getRestoreMasseTestService().appelWsOpRestoreMasseSoapFault(
            urlServiceWeb, 
            formulaire,
            ViStyle.VI_SF_wsse_SecurityTokenUnavailable,
            "wsse_SecurityTokenUnavailable",
            null);
      //getRestoreMasseTestService().appelWsOpRestoreMasseTestLibre(urlServiceWeb, formulaire);
   }
   
 
}
