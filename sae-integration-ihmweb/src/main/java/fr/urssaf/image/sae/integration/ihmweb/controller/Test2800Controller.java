package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.GetDocFormatOrigineFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsGetDocFormatOrigineFormulaire;
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
            formulaire.getGetDocFormatOrigine());
   }
   
   
   private void getDocFormatOrigine(
         String urlWebService,
         GetDocFormatOrigineFormulaire formulaire) {
      
      // Appel de la méthode de test
      getGetDocFormatOrigineTestService().appelWsOpGetDocFormatOrigineTestLibre(
            urlWebService, 
            formulaire);
      
   }
   
 
}
