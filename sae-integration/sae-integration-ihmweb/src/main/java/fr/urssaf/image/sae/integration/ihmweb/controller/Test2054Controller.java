package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationAffichableFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsConsultationAffichableFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;


/**
 * 2054-ConsultationAffichable-KO-idarchivable-inconnu-MTOM 
 */
@Controller
@RequestMapping(value = "test2054")
public class Test2054Controller extends AbstractTestWsController<TestWsConsultationAffichableFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2054";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsConsultationAffichableFormulaire getFormulairePourGet() {
      
      TestWsConsultationAffichableFormulaire formulaire = new TestWsConsultationAffichableFormulaire();
      ConsultationAffichableFormulaire formConsult = formulaire.getConsultationAffichable();
      formConsult.setIdArchivage("00000000-0000-0000-0000-000000000000");
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
      
      getConsultationAffichableTestService().appelWsOpConsultationSoapFault(
            urlWebService,
            formulaire,
            ViStyle.VI_OK,
            "sae_ArchiveNonTrouvee",
            new Object[] {"00000000-0000-0000-0000-000000000000"});                 
   }
 
}
