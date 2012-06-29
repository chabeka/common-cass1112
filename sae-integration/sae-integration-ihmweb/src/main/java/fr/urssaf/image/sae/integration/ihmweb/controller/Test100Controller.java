package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsCaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;

/**
 * 100-CaptureUnitaire-TestLibre
 */
@Controller
@RequestMapping(value = "test100")
public class Test100Controller extends
      AbstractTestWsController<TestWsCaptureUnitaireFormulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "100";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsCaptureUnitaireFormulaire getFormulairePourGet() {

      TestWsCaptureUnitaireFormulaire formulaire = new TestWsCaptureUnitaireFormulaire();
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();

      // Un exemple d'URL ECDE de fichier à capturer
      // (qui correspond à un document réellement existant sur l'ECDE
      // d'intégration)
      formCapture
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/CaptureUnitaire-100-CaptureUnitaire-TestLibre/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));

      // Des métadonnées exemples
      MetadonneeValeurList metasExemples = ReferentielMetadonneesService
            .getMetadonneesExemplePourCapture();
      metasExemples.modifieValeurMeta(SaeIntegrationConstantes.META_HASH,
            "d145ea8e0ca28b8c97deb0c2a550f0a969a322a3");
      formCapture.getMetadonnees().addAll(metasExemples);
      formCapture.getMetadonnees().add("Denomination",
            "Test 100-CaptureUnitaire-TestLibre");

      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsCaptureUnitaireFormulaire formulaire) {
      captureUnitaire(formulaire.getUrlServiceWeb(), formulaire
            .getCaptureUnitaire());
   }

   private void captureUnitaire(String urlWebService,
         CaptureUnitaireFormulaire formulaire) {

      // Appel de la méthode de test
      getCaptureUnitaireTestService().appelWsOpCaptureUnitaireUrlEcdeTestLibre(
            urlWebService, formulaire);

   }

}
