package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.GetDocFormatOrigineFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsGetDocFormatOrigineFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;


/**
 * 2800-getDocFormatOrigine-TestLibre
 */
@Controller
@RequestMapping(value = "test2800")
public class Test2800Controller extends AbstractTestWsController<TestWsGetDocFormatOrigineFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2800";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsGetDocFormatOrigineFormulaire getFormulairePourGet() {
      
      
      
      TestWsGetDocFormatOrigineFormulaire formulaire = new TestWsGetDocFormatOrigineFormulaire();
      GetDocFormatOrigineFormulaire formConsult = formulaire.getGetDocFormatOrigine();
   // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      
      formConsult.getResultats().setStatus(TestStatusEnum.SansStatus);

      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsGetDocFormatOrigineFormulaire formulaire) {
      getDocFormatOrigine(
            formulaire.getUrlServiceWeb(),
            formulaire.getGetDocFormatOrigine(), formulaire.getViFormulaire());
   }
   
   
   private void getDocFormatOrigine(
         String urlWebService,
         GetDocFormatOrigineFormulaire formulaire, ViFormulaire viForm) {
      
      // Appel de la méthode de test
      getGetDocFormatOrigineTestService().appelWsOpGetDocFormatOrigineTestLibre(
            urlWebService, 
            formulaire, viForm);
      
   }
   
 
}
