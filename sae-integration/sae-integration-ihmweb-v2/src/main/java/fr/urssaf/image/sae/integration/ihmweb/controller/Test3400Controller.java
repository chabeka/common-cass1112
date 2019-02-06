package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationGNTGNSFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsConsultationGNTGNSFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

@Controller
@RequestMapping(value = "test3400")
public class Test3400Controller extends AbstractTestWsController<TestWsConsultationGNTGNSFormulaire>{

   @Override
   protected String getNumeroTest() {
      return "3400";
   }

   @Override
   protected TestWsConsultationGNTGNSFormulaire getFormulairePourGet() {
      TestWsConsultationGNTGNSFormulaire formulaire = new TestWsConsultationGNTGNSFormulaire();
      
   // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      
      ConsultationGNTGNSFormulaire formConsult = formulaire.getConsultation();
      formConsult.getResultats().setStatus(TestStatusEnum.SansStatus);

      return formulaire;
   }

   @Override
   protected void doPost(TestWsConsultationGNTGNSFormulaire formulaire) {
      consultationGNTGNS(
            formulaire.getUrlServiceWeb(),
            formulaire.getConsultation(), formulaire.getViFormulaire());
   }
   
   protected void doPost(String url, TestWsConsultationGNTGNSFormulaire formulaire) {
      consultationGNTGNS(
            url,
            formulaire.getConsultation(), formulaire.getViFormulaire());
   }
   
   private void consultationGNTGNS(
         String urlWebService,
         ConsultationGNTGNSFormulaire formulaire, ViFormulaire viForm) {
      
      // Appel de la méthode de test
      getConsultationGNTGNSTestService().appelWsOpConsultationTestLibre(
            urlWebService, 
            formulaire, viForm);
      
   }

}
