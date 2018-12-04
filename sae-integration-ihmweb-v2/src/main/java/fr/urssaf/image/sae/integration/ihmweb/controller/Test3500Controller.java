package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RecuperationMetadonneeFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsRecuperationMetadonneeFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

@Controller
@RequestMapping(value = "test3500")
public class Test3500Controller extends AbstractTestWsController<TestWsRecuperationMetadonneeFormulaire>{
   
   @Override
   protected String getNumeroTest() {
      return "3500";
   }

   @Override
   protected TestWsRecuperationMetadonneeFormulaire getFormulairePourGet() {
      TestWsRecuperationMetadonneeFormulaire formulaire = new TestWsRecuperationMetadonneeFormulaire();
      
   // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      
      RecuperationMetadonneeFormulaire formConsult = formulaire.getRecuperationMetadonnee();
      formConsult.getResultats().setStatus(TestStatusEnum.SansStatus);

      return formulaire;
   }

   @Override
   protected void doPost(TestWsRecuperationMetadonneeFormulaire formulaire) {
      recuperationMetadonnee(
            formulaire.getUrlServiceWeb(),
            formulaire.getRecuperationMetadonnee(), formulaire.getViFormulaire());
   }
   
   protected void doPost(String url, TestWsRecuperationMetadonneeFormulaire formulaire) {
      recuperationMetadonnee(
            url,
            formulaire.getRecuperationMetadonnee(), formulaire.getViFormulaire());
   }
   
   private void recuperationMetadonnee(
         String urlWebService,
         RecuperationMetadonneeFormulaire formulaire, ViFormulaire viForm) {
      
      // Appel de la méthode de test
      getRecuperationMetadonneeTestServ().appelWsOpRecuperationMetadonneeTestLibre(
            urlWebService, 
            formulaire, viForm);
      
   }

}
