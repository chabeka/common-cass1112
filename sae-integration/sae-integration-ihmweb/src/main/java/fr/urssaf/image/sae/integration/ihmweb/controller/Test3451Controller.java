package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationGNTGNSFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsConsultationGNTGNSFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;

@Controller
@RequestMapping(value = "test3451")
public class Test3451Controller extends AbstractTestWsController<TestWsConsultationGNTGNSFormulaire> {
   @Override
   protected final String getNumeroTest() {
      return "3451";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsConsultationGNTGNSFormulaire getFormulairePourGet() {
      
      TestWsConsultationGNTGNSFormulaire formulaire = new TestWsConsultationGNTGNSFormulaire();
      ConsultationGNTGNSFormulaire formConsult = formulaire.getConsultation();
      formConsult.setIdArchivage("00000000-0000-0000-0000-000000000000");
      formConsult.getResultats().setStatus(TestStatusEnum.SansStatus);

      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsConsultationGNTGNSFormulaire formulaire) {
      consultationGNTGNS(
            formulaire.getUrlServiceWeb(),
            formulaire.getConsultation());
   }
   
   
   private void consultationGNTGNS(
         String urlWebService,
         ConsultationGNTGNSFormulaire formulaire) {
      
      // Appel de la méthode de test
      getConsultationGNTGNSTestService().appelWsOpConsultationTestLibre(
            urlWebService, 
            formulaire);
      
      getConsultationGNTGNSTestService().appelWsOpConsultationSoapFault(
            urlWebService,
            formulaire,
            ViStyle.VI_OK,
            "sae_ArchiveNonTrouvee",
            new Object[] {"00000000-0000-0000-0000-000000000000"});                 
   }
 
}
