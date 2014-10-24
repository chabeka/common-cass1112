package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationAffichableFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsConsultationAffichableFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.service.SaeServiceTestService;


/**
 * 400-ConsultationAffichable-TestLibre
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
            formulaire.getConsultationAffichable());
   }
   
   
   private void consultationAffichable(
         String urlWebService,
         ConsultationAffichableFormulaire formulaire) {
      
      // Appel de la méthode de test
      getConsultationAffichableTestService().appelWsOpConsultationAffichableTestLibre(
            urlWebService, 
            formulaire);
      
   }
   
 
}
