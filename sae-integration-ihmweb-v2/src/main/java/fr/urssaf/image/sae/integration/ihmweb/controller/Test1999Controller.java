package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsTransfertFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TransfertFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;

/**
 * 1699-SAI-Suppression-TestLibre
 */
@Controller
@RequestMapping(value = "test1999")
public class Test1999Controller extends
      AbstractTestWsController<TestWsTransfertFormulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1999";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsTransfertFormulaire getFormulairePourGet() {
      TestWsTransfertFormulaire formulaire = new TestWsTransfertFormulaire();
      
   // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      
      TransfertFormulaire formTransfert = formulaire.getTransfert();
      formTransfert.setIdDocument(UUID.randomUUID());
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsTransfertFormulaire formulaire) {
      transfert(formulaire.getUrlServiceWeb(), formulaire.getTransfert(), formulaire.getViFormulaire());
   }

   private void transfert(String urlWebService, TransfertFormulaire formulaire, ViFormulaire viForm) {
      // Appel de la méthode de test
      getTransfertTestService().appelWsOpTransfertTestLibre(urlWebService,
            formulaire, viForm);
   }

}
