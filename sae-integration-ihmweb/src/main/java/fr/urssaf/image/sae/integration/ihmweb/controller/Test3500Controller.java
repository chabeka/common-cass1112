package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.RecuperationMetadonneeFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsRecuperationMetadonneeFormulaire;
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
      RecuperationMetadonneeFormulaire formConsult = formulaire.getRecuperationMetadonnee();
      formConsult.getResultats().setStatus(TestStatusEnum.SansStatus);

      return formulaire;
   }

   @Override
   protected void doPost(TestWsRecuperationMetadonneeFormulaire formulaire) {
      recuperationMetadonnee(
            formulaire.getUrlServiceWeb(),
            formulaire.getRecuperationMetadonnee());
   }
   
   protected void doPost(String url, TestWsRecuperationMetadonneeFormulaire formulaire) {
      recuperationMetadonnee(
            url,
            formulaire.getRecuperationMetadonnee());
   }
   
   private void recuperationMetadonnee(
         String urlWebService,
         RecuperationMetadonneeFormulaire formulaire) {
      
      // Appel de la méthode de test
      getRecuperationMetadonneeTestServ().appelWsOpRecuperationMetadonneeTestLibre(
            urlWebService, 
            formulaire);
      
   }

}
