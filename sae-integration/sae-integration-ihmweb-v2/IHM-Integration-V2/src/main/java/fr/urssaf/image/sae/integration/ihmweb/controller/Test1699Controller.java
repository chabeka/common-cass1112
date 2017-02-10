package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.SuppressionFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsSuppressionFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;

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
      
      // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      
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
            .getSuppression(), formulaire.getViFormulaire());
   }

   private void suppression(String urlWebService,
         SuppressionFormulaire formulaire, ViFormulaire viForm) {

      // Appel de la méthode de test
      getSuppressionTestService().appelWsOpSuppressionTestLibre(
            urlWebService, formulaire, viForm);

   }

}
