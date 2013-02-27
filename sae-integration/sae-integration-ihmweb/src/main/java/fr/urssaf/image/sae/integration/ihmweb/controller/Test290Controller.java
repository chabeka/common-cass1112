package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestStockageMasseAllFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator;
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
      formCapture.setHash("e2b2267a400f77463d42b7b6664fffa362669469");
      
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

         etape2captureMasseResultats(formulaire.getCaptureMasseResultat());

      } else if ("3".equals(etape)) {

         etape3Recherche(formulaire);

      } else if ("4".equals(etape)) {

         etape5Comptages(formulaire);

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

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(urlWebService,
            formulaire.getCaptureMasseDeclenchement());

   }

   private void etape2captureMasseResultats(
         CaptureMasseResultatFormulaire formulaire) {
      ErreurType erreurType = new ErreurType();
      erreurType.setCode("SAE-EC-SOM001");
      erreurType
            .setLibelle("Le fichier sommaire n'est pas valide. Détails : Le hash du fichier sommaire.xml attendu : e2b2267a400f77463d42b7b6664fffa362669469 est différent de celui obtenu : 1c9a87ef661865846a0167e7d535e90d8f0cd809 (type de hash : SHA-1)");
      getCaptureMasseTestService()
            .testResultatsTdmReponseKOAttendue(formulaire, erreurType) ;

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
      
  

   private void etape5Comptages(TestStockageMasseAllFormulaire formulaire) {

      // Récupération de l'objet ResultatTest
      ResultatTest resultatTest = formulaire.getComptagesFormulaire()
            .getResultats();
      resultatTest.clear();

      // Lecture de l'identifiant du traitement de masse
      String idTdm = formulaire.getComptagesFormulaire().getIdTdm();

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest,
            new Long(COUNT_WAITED));

      // Passe le test en OK si pas KO
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

}
