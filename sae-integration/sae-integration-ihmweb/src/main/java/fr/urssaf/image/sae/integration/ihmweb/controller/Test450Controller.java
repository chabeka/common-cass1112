package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.service.SaeServiceTestService;


/**
 * 450-Consultation-KO-SansAuthentification
 */
@Controller
@RequestMapping(value = "test450")
public class Test450Controller extends AbstractTestWsController<TestWsConsultationFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "450";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsConsultationFormulaire getFormulairePourGet() {
      
      TestWsConsultationFormulaire formulaire = new TestWsConsultationFormulaire();
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      formConsult.setIdArchivage(SaeServiceTestService.getIdArchivageExemple());
      
      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsConsultationFormulaire formulaire) {
      consultation(
            formulaire.getUrlServiceWeb(),
            formulaire.getConsultation());
   }
   
   
   
   private void consultation(
         String urlServiceWeb,
         ConsultationFormulaire formulaire) {
      
      // Appel de la méthode de test
      getConsultationTestService().appelWsOpConsultationSoapFault(
            urlServiceWeb, 
            formulaire,
            ViStyle.VI_SF_wsse_SecurityTokenUnavailable,
            "wsse_SecurityTokenUnavailable",
            null);
      
   }
   
 
}
