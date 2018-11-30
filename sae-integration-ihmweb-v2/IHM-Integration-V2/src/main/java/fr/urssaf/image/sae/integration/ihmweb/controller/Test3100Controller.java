package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RestoreMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsRestoreMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.service.SaeServiceTestService;


/**
 * 3100-RestoreMasse-OK-SansAuthentification
 */
@Controller
@RequestMapping(value = "test3100")
public class Test3100Controller extends AbstractTestWsController<TestWsRestoreMasseFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "3100";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsRestoreMasseFormulaire getFormulairePourGet() {
      
      TestWsRestoreMasseFormulaire formulaire = new TestWsRestoreMasseFormulaire();
      
   // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      
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
            formulaire.getRestoreMasse(), formulaire.getViFormulaire());
   }
   
   
   
   private void restoreMasse(
         String urlServiceWeb,
         RestoreMasseFormulaire formulaire, ViFormulaire viForm) {
      
      // Appel de la méthode de test
//      getRestoreMasseTestService().appelWsOpRestoreMasseSoapFault(
//            urlServiceWeb, 
//            formulaire,
//            ViStyle.VI_SF_wsse_SecurityTokenUnavailable,
//            "wsse_SecurityTokenUnavailable",
//            null);
      getRestoreMasseTestService().appelWsOpRestoreMasseTestLibre(urlServiceWeb, formulaire, viForm);
   }
   
 
}
