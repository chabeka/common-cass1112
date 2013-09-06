package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.SuppressionFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsSuppressionFormulaire;

/**
 * 1699-SAI-Suppression-TestLibre
 */
@Controller
@RequestMapping(value = "test1699")
public class Test1699Controller extends
      AbstractTestWsController<TestWsSuppressionFormulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1699";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsSuppressionFormulaire getFormulairePourGet() {

      
      TestWsSuppressionFormulaire formulaire = new TestWsSuppressionFormulaire();
      
      SuppressionFormulaire formSuppression = formulaire.getSuppression();
      
      formSuppression.setIdDocument(UUID.randomUUID());
      
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsSuppressionFormulaire formulaire) {
      suppression(formulaire.getUrlServiceWeb(), formulaire
            .getSuppression());
   }

   private void suppression(String urlWebService,
         SuppressionFormulaire formulaire) {

      // Appel de la méthode de test
      getSuppressionTestService().appelWsOpSuppressionTestLibre(
            urlWebService, formulaire);

   }

}
