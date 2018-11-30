package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;


/**
 * 452-Consultation-KO-MetadonneeConsultInexistante
 */
@Controller
@RequestMapping(value = "test452")
public class Test452Controller extends AbstractTestWsController<TestWsConsultationFormulaire> {

   
   private static final String ID_ARCHIVE_TEST = 
      "00000000-0000-0000-0000-000000000000";
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "452";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsConsultationFormulaire getFormulairePourGet() {
      
      TestWsConsultationFormulaire formulaire = new TestWsConsultationFormulaire();
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      
      formConsult.setIdArchivage(ID_ARCHIVE_TEST);
      
      formConsult.getCodeMetadonnees().add("Gloubi");
      formConsult.getCodeMetadonnees().add("CodeRND");
      formConsult.getCodeMetadonnees().add("Boulga");
      formConsult.getCodeMetadonnees().add("Siret");
      
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
      this.getConsultationTestService().appelWsOpConsultationSoapFault(
            urlServiceWeb, 
            formulaire,
            ViStyle.VI_OK,
            "sae_ConsultationMetadonneesInexistante",
            new Object[] {"Boulga, Gloubi"});
      
   }

   
}
