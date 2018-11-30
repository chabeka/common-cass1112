package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RepriseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test3900Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

@Controller
@RequestMapping(value = "test3900")
public class Test3900Controller extends
      AbstractTestWsController<Test3900Formulaire> {

   public static final String NUMERO_TEST = "3900";

   @Override
   protected String getNumeroTest() {
      return NUMERO_TEST;
   }

   @Override
   protected Test3900Formulaire getFormulairePourGet() {

      Test3900Formulaire formulaire = new Test3900Formulaire();

      // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);

      RepriseFormulaire formReprise = formulaire.getReprise();
      formReprise.getResultats().setStatus(TestStatusEnum.SansStatus);

      return formulaire;
   }

   @Override
   protected void doPost(Test3900Formulaire formulaire) {
      reprise(formulaire.getUrlServiceWeb(), formulaire.getReprise(),
            formulaire.getViFormulaire());
   }

   private void reprise(String urlServiceWeb, RepriseFormulaire formulaire,
         ViFormulaire viForm) {
      getRepriseTestService().appelWsOpRepriseTestLibre(urlServiceWeb,
            formulaire, viForm);
   }

}
