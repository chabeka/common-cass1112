package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsCaptureMasseFormulaire;


/**
 * 250-CaptureMasse-KO-SansAuthentification
 */
@Controller
@RequestMapping(value = "test250")
public class Test250Controller extends AbstractTestWsController<TestWsCaptureMasseFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "250";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsCaptureMasseFormulaire getFormulairePourGet() {
      
      TestWsCaptureMasseFormulaire formulaire = new TestWsCaptureMasseFormulaire();
      CaptureMasseFormulaire formCapture = formulaire.getCaptureMasse();
      formCapture.setUrlSommaire("ecde://ecdeX/numeroCS/20110527/idTraitement/sommaire.xml");
      
      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsCaptureMasseFormulaire formulaire) {
      captureMasse(
            formulaire.getUrlServiceWeb(),
            formulaire.getCaptureMasse());
   }
   
   
   private void captureMasse(
         String urlServiceWeb,
         CaptureMasseFormulaire formCaptureMasse) {
      
      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseSoapFaultAuth(
            urlServiceWeb, 
            formCaptureMasse);
      
   }
   
 
}
