package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationAffichableFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsConsultationAffichableFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.service.SaeServiceTestService;


/**
 * 2000-ConsultationAffichable-TestLibre
 */
@Controller
@RequestMapping(value = "test2099")
public class Test2099Controller extends AbstractTestWsController<TestWsConsultationAffichableFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2099";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsConsultationAffichableFormulaire getFormulairePourGet() {
      
      TestWsConsultationAffichableFormulaire formulaire = new TestWsConsultationAffichableFormulaire();
      
   // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      
      ConsultationAffichableFormulaire formConsult = formulaire.getConsultationAffichable();
      formConsult.setIdArchivage(SaeServiceTestService.getIdArchivageExemple());
      formConsult.getResultats().setStatus(TestStatusEnum.SansStatus);

      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsConsultationAffichableFormulaire formulaire) {
      consultationAffichable(
            formulaire.getUrlServiceWeb(),
            formulaire.getConsultationAffichable(), formulaire.getViFormulaire());
   }
   
   
   private void consultationAffichable(
         String urlWebService,
         ConsultationAffichableFormulaire formulaire, ViFormulaire viForm) {
      
      // Appel de la méthode de test
      getConsultationAffichableTestService().appelWsOpConsultationAffichableTestLibre(
            urlWebService, 
            formulaire, viForm);
      
   }
   
 
}
