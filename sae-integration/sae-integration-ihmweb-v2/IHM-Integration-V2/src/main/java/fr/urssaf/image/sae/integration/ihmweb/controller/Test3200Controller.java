package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.EtatTraitementMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsEtatTraitementMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.UUIDList;

/**
 * 3200-EtatTraitementMasse-TestLibre
 */
@Controller
@RequestMapping(value = "test3200")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test3200Controller extends
      AbstractTestWsController<TestWsEtatTraitementMasseFormulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "3200";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsEtatTraitementMasseFormulaire getFormulairePourGet() {

      TestWsEtatTraitementMasseFormulaire formulaire = new TestWsEtatTraitementMasseFormulaire();
      
   // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      
      EtatTraitementMasseFormulaire formEtatTraitementMasse = formulaire.getEtatTraitementMasse();
      formEtatTraitementMasse.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration      
      UUIDList uuidList = new UUIDList();
      formEtatTraitementMasse.setRequeteListeUUID(uuidList);
      uuidList.add("acf4e750-2898-11e6-942b-f8b156a864b3");
      uuidList.add("90c7404c-2bb7-11e6-b67b-9e71128cae77");
     
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsEtatTraitementMasseFormulaire formulaire) {
      etatTraitementMasse(formulaire.getUrlServiceWeb(), formulaire.getEtatTraitementMasse(), formulaire.getViFormulaire());
   }

   private void etatTraitementMasse(String urlServiceWeb, EtatTraitementMasseFormulaire formulaire, ViFormulaire viForm) {

      // Appel de la méthode de test
      getEtatTraitementMasseTestService().appelWsOpEtatTraitementMasseTestLibre(urlServiceWeb, formulaire, viForm);
   }
}
   