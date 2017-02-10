package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;


/**
 * 400-Consultation-TestLibre
 */
@Controller
@RequestMapping(value = "test400")
public class Test400Controller extends AbstractTestWsController<TestWsConsultationFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "400";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsConsultationFormulaire getFormulairePourGet() {
      
      TestWsConsultationFormulaire formulaire = new TestWsConsultationFormulaire();
      
   // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      formConsult.getResultats().setStatus(TestStatusEnum.SansStatus);

      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsConsultationFormulaire formulaire) {
      consultation(
            formulaire.getUrlServiceWeb(),
            formulaire.getConsultation(), formulaire.getViFormulaire());
   }
   
   
   private void consultation(
         String urlWebService,
         ConsultationFormulaire formulaire, ViFormulaire viForm) {
      
      // Appel de la méthode de test
      getConsultationTestService().appelWsOpConsultationTestLibre(
            urlWebService, 
            formulaire, viForm);
      
   }
   
 
}
