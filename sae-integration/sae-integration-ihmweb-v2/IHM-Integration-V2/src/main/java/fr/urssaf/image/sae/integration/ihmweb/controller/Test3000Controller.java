package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.SuppressionMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsSuppressionMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.SuppressionMasseRequestType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.SuppressionMasseResponse;

/**
 * 3000-SuppressionMasse-TestLibre
 */
@Controller
@RequestMapping(value = "test3000")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test3000Controller extends
      AbstractTestWsController<TestWsSuppressionMasseFormulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "3000";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsSuppressionMasseFormulaire getFormulairePourGet() {

      TestWsSuppressionMasseFormulaire formulaire = new TestWsSuppressionMasseFormulaire();
      
   // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      
      SuppressionMasseFormulaire formSuppressionMasse = formulaire.getSuppressionMasse();
      formSuppressionMasse.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      String requeteLucene;
      
      requeteLucene =getCasTest().getLuceneExemple();
      formSuppressionMasse.setRequeteLucene(requeteLucene);
      
       return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsSuppressionMasseFormulaire formulaire) {
      suppressionMasse(formulaire.getUrlServiceWeb(), formulaire.getSuppressionMasse(), formulaire.getViFormulaire());
   }

   private void suppressionMasse(String urlServiceWeb, SuppressionMasseFormulaire formulaire, ViFormulaire viForm) {

      // Appel de la méthode de test
      getSuppressionMasseTestService().appelWsOpSuppressionMasseTestLibre(urlServiceWeb, formulaire, viForm);
   }
}
   
