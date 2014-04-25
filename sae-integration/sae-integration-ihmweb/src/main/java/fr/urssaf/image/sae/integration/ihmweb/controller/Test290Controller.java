package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ComptagesTdmFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestStockageMasseAllFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 201-CaptureMasse-OK-Tor-10
 */
@Controller
@RequestMapping(value = "test290")
public class Test290Controller extends
      AbstractTestWsController<TestStockageMasseAllFormulaire> {

   /**
    * Nombre d'occurence attendu
    */
   private static final int COUNT_WAITED = 0;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "290";
   }
   
   
   private String getDebutUrlEcde() {
      return getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-290-CaptureMasse-Avec-Hash-Recalcule-KO-Tor-10/");
   }
   

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestStockageMasseAllFormulaire getFormulairePourGet() {

      TestStockageMasseAllFormulaire formulaire = new TestStockageMasseAllFormulaire();

      // Initialise le formulaire de capture de masse
      
      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getDebutUrlEcde() + "sommaire.xml");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);
      formCapture.setAvecHash(true);
      formCapture.setTypeHash("SHA-1");
      formCapture.setHash("82e9d7e3fee8c99b238d952a1cf01351882c3c40");
      
      RechercheFormulaire formulaireRecherche =formulaire.getRechFormulaire();
      formulaireRecherche.setRequeteLucene(getCasTest().getLuceneExemple());
      
      return formulaire;
      
      
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestStockageMasseAllFormulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("2".equals(etape)) {

         etape2captureMasseResultats(formulaire.getCaptureMasseDeclenchement()
               .getUrlSommaire(), formulaire.getCaptureMasseResultat());

      } else if ("3".equals(etape)) {

         etape3Recherche(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureMasseAppelWs(String urlWebService,
         TestStockageMasseAllFormulaire formulaire) {

      // Vide le résultat du test précédent de l'étape 2
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());

      // Vide le résultat du test précédent de l'étape 3
      ComptagesTdmFormulaire formComptages = formulaire.getComptagesFormulaire();
      formComptages.getResultats().clear();
      formComptages.setIdTdm(null);
      
      String[] result = new String[]{ formulaire.getCaptureMasseDeclenchement().getHash(), 
         "011f49b71a35bf283d9a9df28b8f9b95308dc932"};
      
      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseSoapFaultAttendue(urlWebService,
            formulaire.getCaptureMasseDeclenchement(), "sae_HashSommaireIncorrect", result);
   }

   private void etape2captureMasseResultats(String urlEcde,
         CaptureMasseResultatFormulaire formulaire) {

      getCaptureMasseTestService().testResultatsTdmReponseAucunFichierAttendu(formulaire, urlEcde);
   }

   private void etape3Recherche(TestStockageMasseAllFormulaire formulaire) {
      
      
      // Appel le service de test de la recherche
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getRechFormulaire(), COUNT_WAITED, false,
                  TypeComparaison.NumeroRecours);

      ResultatTest resultatTest = formulaire.getRechFormulaire().getResultats();
      
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {

         ResultatRechercheType results[] = response.getRechercheResponse()
               .getResultats().getResultat();

         // Au mieux, si le test est OK, on le passe "A contrôler", car
         // certaines métadonnées doivent être vérifiées à la main
         if (!TestStatusEnum.Echec.equals(formulaire.getRechFormulaire()
               .getResultats().getStatus()) && ArrayUtils.isEmpty(results)) {

            formulaire.getRechFormulaire().getResultats().setStatus(
                  TestStatusEnum.Succes);
         }

      }

   }

}
