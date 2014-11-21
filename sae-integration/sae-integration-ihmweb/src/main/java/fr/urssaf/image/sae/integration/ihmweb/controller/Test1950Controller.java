package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsTransfertFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TransfertFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

/**
 * 1950-T-KO-RECHERCHE-KO
 */
@Controller
@RequestMapping(value = "test1950")
public class Test1950Controller extends
      AbstractTestWsController<TestWsTransfertFormulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1950";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsTransfertFormulaire getFormulairePourGet() {

      TestWsTransfertFormulaire formulaire = new TestWsTransfertFormulaire();
      TransfertFormulaire formTransfert = formulaire.getTransfert();
      
      //-- On set UUID du doc
      formTransfert.setIdDocument(UUID.fromString("00000000-0000-0000-0000-000000000000"));

      //-- formulaire de recherche
      RechercheFormulaire formRechercheGns = formulaire.getRechercheGns();
      formRechercheGns.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("INT_CS_TRANSFERT");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add("INT_PAGM_CS_TRANSFERT");

      return formulaire;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsTransfertFormulaire formulaire) {
     transfert(formulaire.getUrlServiceWeb(), formulaire.getTransfert(), formulaire.getViFormulaire());
   }
  
   private void transfert(String urlServiceWeb, TransfertFormulaire formulaire, ViFormulaire viParams) {
      //-- Appel de la méthode de test
      String refSoapFault = "sae_ArchiveNonTrouveeTransfert";
      String[] args = new String[] {formulaire.getIdDocument().toString()};
      getTransfertTestService().appelWsOpTransfertSoapFault(urlServiceWeb, formulaire, viParams, refSoapFault, args);
   }
}
