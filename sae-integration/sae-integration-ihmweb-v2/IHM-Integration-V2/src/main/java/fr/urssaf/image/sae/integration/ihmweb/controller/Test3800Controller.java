package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.DeblocageFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test3800Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.UUIDList;

@Controller
@RequestMapping(value = "test3800")
public class Test3800Controller extends
      AbstractTestWsController<Test3800Formulaire> {

   @Override
   protected String getNumeroTest() {
      return "3800";
   }

   @Override
   protected Test3800Formulaire getFormulairePourGet() {

      Test3800Formulaire formulaire = new Test3800Formulaire();

      // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);

      DeblocageFormulaire formDeblocage = formulaire.getDeblocage();
      formDeblocage.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      UUIDList uuidList = new UUIDList();
      formDeblocage.setIdJob(uuidList);
      uuidList.add("acf4e750-2898-11e6-942b-f8b156a864b3");

      return formulaire;

   }

   @Override
   protected void doPost(Test3800Formulaire formulaire) {
      deblocage(formulaire.getUrlServiceWeb(), formulaire.getDeblocage(), formulaire.getViFormulaire());
   }
   
   private void deblocage(String urlServiceWeb, DeblocageFormulaire formulaire, ViFormulaire viForm) {
      
      getDeblocageTestService().appelWsOpDeblocageTestLibre(urlServiceWeb, formulaire, viForm);
   }

}
