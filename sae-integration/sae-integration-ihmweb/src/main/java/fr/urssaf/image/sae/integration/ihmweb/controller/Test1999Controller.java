package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsTransfertFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TransfertFormulaire;

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
      TransfertFormulaire formTransfert = formulaire.getTransfert();
      formTransfert.setIdDocument(UUID.randomUUID());
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsTransfertFormulaire formulaire) {
      transfert(formulaire.getUrlServiceWeb(), formulaire.getTransfert());
   }

   private void transfert(String urlWebService, TransfertFormulaire formulaire) {
      // Appel de la méthode de test
      getTransfertTestService().appelWsOpTransfertTestLibre(urlWebService,
            formulaire);
   }

}
