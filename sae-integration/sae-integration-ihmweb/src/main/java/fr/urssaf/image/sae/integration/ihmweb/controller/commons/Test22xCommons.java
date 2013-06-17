package fr.urssaf.image.sae.integration.ihmweb.controller.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test22xFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * Méthodes communes pour les tests 225, 226, 227, 228
 */
@Component
public class Test22xCommons {

   @Autowired
   private TestsControllerCommons testCommons;

   private String getDenomination(String numeroTest) {
      if ("225".equals(numeroTest)) {
         return "Test 225-CAPTUREMASSE-OK-VIRTUEL-SANS-HASH";
      } else if ("226".equals(numeroTest)) {
         return "Test 226-CAPTUREMASSE-OK-VIRTUEL-AVEC-HASH";
      } else if ("227".equals(numeroTest)) {
         return "Test 227-CAPTUREMASSE-OK-VIRTUEL-GROS-VOLUME";
      } else if ("228".equals(numeroTest)) {
         return "Test 228-CAPTUREMASSE-OK-VIRTUEL-UN-DOCUMENT";
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   private Integer getCount(String numeroTest) {
      if ("225".equals(numeroTest)) {
         return 5;
      } else if ("226".equals(numeroTest)) {
         return 5;
      } else if ("227".equals(numeroTest)) {
         return 1;
      } else if ("228".equals(numeroTest)) {
         return 1;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   private CodeMetadonneeList getMetaListRecherche(String numeroTest) {
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      if (Arrays.asList("225", "226", "227", "228").contains(numeroTest)) {
         codesMeta.add("Denomination");
      }
      if (Arrays.asList("225", "226").contains(numeroTest)) {
         codesMeta.add("NbPages");
      }
      if (Arrays.asList("225", "227", "226").contains(numeroTest))
         codesMeta.add("NumeroRecours");

      return codesMeta;
   }

   private CodeMetadonneeList getMetaListConsultation(String numeroTest) {
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("Denomination");
      if (Arrays.asList("225", "226", "227", "228").contains(numeroTest)) {
         codesMeta.add("NumeroRecours");
      }
      if (Arrays.asList("225").contains(numeroTest)) {
         codesMeta.add("NbPages");
      }

      return codesMeta;
   }

   private String getURLEcde(String numeroTest) {
      if ("225".equals(numeroTest)) {
         return testCommons
               .getEcdeService()
               .construitUrlEcde(
                     "SAE_INTEGRATION/20110822/CaptureMasse-225-CaptureMasse-OK-Virtuel-Sans-Hash/");
      } else if ("226".equals(numeroTest)) {
         return testCommons
               .getEcdeService()
               .construitUrlEcde(
                     "SAE_INTEGRATION/20110822/CaptureMasse-226-CaptureMasse-OK-Virtuel-Avec-Hash/");
      } else if ("227".equals(numeroTest)) {
         return testCommons
               .getEcdeService()
               .construitUrlEcde(
                     "SAE_INTEGRATION/20110822/CaptureMasse-227-CaptureMasse-OK-Virtuel-Gros-Volume/");
      } else if ("228".equals(numeroTest)) {
         return testCommons
               .getEcdeService()
               .construitUrlEcde(
                     "SAE_INTEGRATION/20110822/CaptureMasse-228-CaptureMasse-OK-Virtuel-Un-Document/");
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   public final Test22xFormulaire getFormulairePourGet(String numeroTest) {

      Test22xFormulaire formulaire = new Test22xFormulaire();

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape de capture unitaire
      // -----------------------------------------------------------------------------

      // Initialise le formulaire de capture de masse

      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getURLEcde(numeroTest).concat("sommaire.xml"));
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);
      formCapture.setAvecHash(false);

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat
            .setUrlSommaire(getURLEcde(numeroTest).concat("resultat.xml"));
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Initialise le formulaire de recherche

      RechercheFormulaire rechFormulaire = formulaire.getRechercheFormulaire();
      rechFormulaire.setRequeteLucene(testCommons.getCasTest(numeroTest)
            .getLuceneExemple());

      // Métadonnées souhaitées
      // => La liste des métadonnées consultables
      CodeMetadonneeList codesMeta = getMetaListRecherche(numeroTest);
      rechFormulaire.setCodeMetadonnees(codesMeta);

      // Initialise le formulaire de consultation

      ConsultationFormulaire formConsult = formulaire
            .getConsultationFormulaire();

      formConsult.setCodeMetadonnees(getMetaListConsultation(numeroTest));

      return formulaire;

   }

   public final void doPost(Test22xFormulaire formulaire, String numeroTest) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("2".equals(etape)) {

         etape2captureMasseResultats(formulaire.getCaptureMasseResultat());

      } else if ("3".equals(etape)) {

         etape3Recherche(formulaire, numeroTest);

      } else if ("4".equals(etape)) {

         etape4Consultation(formulaire, numeroTest);

      } else if ("5".equals(etape)) {

         etape5Comptages(formulaire, numeroTest);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private Integer getCountForTest(String numeroTest) {
      Integer count = 0;
      if (Arrays.asList("225", "226").contains(numeroTest)) {
         count = 5;
      }
      if (Arrays.asList("227", "228").contains(numeroTest)) {
         count = 1;
      }
      return count;
   }

   private void etape1captureMasseAppelWs(String urlWebService,
         Test22xFormulaire formulaire) {

      // Vide le résultat du test précédent de l'étape 2
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());

      // Appel de la méthode de test
      testCommons.getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(
            urlWebService, formulaire.getCaptureMasseDeclenchement());

   }

   private void etape2captureMasseResultats(
         CaptureMasseResultatFormulaire formulaire) {

      testCommons.getCaptureMasseTestService()
            .testResultatsTdmReponseOKAttendue(formulaire);

   }

   private void etape3Recherche(Test22xFormulaire formulaire, String numeroTest) {

      // Appel le service de test de la recherche
      RechercheResponse response = testCommons.getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getRechercheFormulaire(), getCount(numeroTest),
                  false, TypeComparaison.NumeroRecours);

      ResultatTest resultatTest = formulaire.getRechercheFormulaire()
            .getResultats();

      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {

         ResultatRechercheType results[] = response.getRechercheResponse()
               .getResultats().getResultat();

         // Tri les résultats par ordre croissant de NumeroRecours
         List<ResultatRechercheType> resultatsTries = Arrays.asList(response
               .getRechercheResponse().getResultats().getResultat());
         Collections.sort(resultatsTries, new ResultatRechercheComparator(
               TypeComparaison.NumeroRecours));

         // Vérifie chaque résultat
         verifieResultat(resultatsTries, resultatTest, numeroTest);

         // Au mieux, si le test est OK, on le passe "A contrôler", car
         // certaines métadonnées doivent être vérifiées à la main
         if (!TestStatusEnum.Echec.equals(formulaire.getRechercheFormulaire()
               .getResultats().getStatus())) {

            formulaire.getConsultationFormulaire().setIdArchivage(
                  results[0].getIdArchive().getUuidType());

            formulaire.getRechercheFormulaire().getResultats().setStatus(
                  TestStatusEnum.AControler);
         }

      }

   }

   private void verifieResultat(List<ResultatRechercheType> resultatRecherche,
         ResultatTest resultatTest, String numeroTest) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      if("225".equals(numeroTest)){
         valeursAttendues.add("Denomination",
               "Test 225-CaptureMasse-OK-Virtuel-Sans-Hash");
      }
      if("226".equals(numeroTest)){
         valeursAttendues.add("Denomination",
               "Test 226-CaptureMasse-OK-Virtuel-Avec-Hash");
      }
      if("227".equals(numeroTest)){
         valeursAttendues.add("Denomination",
               "Test 227-CaptureMasse-OK-Virtuel-Gros-Volume");
      }
      if("228".equals(numeroTest)){
         valeursAttendues.add("Denomination",
               "Test 228-CaptureMasse-OK-Virtuel-Un-Document");
      }

      
      
      Integer count = 0;
      for (ResultatRechercheType r : resultatRecherche) {
         testCommons.getRechercheTestService().verifieResultatRecherche(r,
               count.toString(), resultatTest, valeursAttendues);
         count++;
      }
   }
   
   private List<MetadonneeValeur> getValeurAttendu(String numeroTest){
      List<MetadonneeValeur> valeursMetaAttendus = new ArrayList<MetadonneeValeur>();
      
      if("225".equals(numeroTest)){
            valeursMetaAttendus.add(new MetadonneeValeur("Denomination",
            "Test 225-CaptureMasse-OK-Virtuel-Sans-Hash"));
      }
      if("226".equals(numeroTest)){
         valeursMetaAttendus.add(new MetadonneeValeur("Denomination",
         "Test 226-CaptureMasse-OK-Virtuel-Avec-Hash"));
      }
      if("227".equals(numeroTest)){
         valeursMetaAttendus.add(new MetadonneeValeur("Denomination",
         "Test 227-CaptureMasse-OK-Virtuel-Gros-Volume"));
         valeursMetaAttendus.add(new MetadonneeValeur("NumeroRecours",
         "1"));
      }
      if("228".equals(numeroTest)){
         valeursMetaAttendus.add(new MetadonneeValeur("Denomination",
         "Test 228-CaptureMasse-OK-Virtuel-Un-Document"));
         valeursMetaAttendus.add(new MetadonneeValeur("NumeroRecours",
         "1"));
      }
      return valeursMetaAttendus;
   }

   private void etape4Consultation(Test22xFormulaire formulaire, String numeroTest) {

      // Valeurs des métadonnées attendues
      

      // Appel du service de vérification
      testCommons.getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getConsultationFormulaire(),
                  null,
                  getMetaListConsultation(numeroTest), getValeurAttendu(numeroTest));

      // Si le test n'est pas en échec, alors on peut le passer en succès,
      // car tout a pu être vérifié
      ResultatTest resultatTest = formulaire.getConsultationFormulaire()
            .getResultats();
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.AControler);
      }

   }

   private void etape5Comptages(Test22xFormulaire formulaire, String numeroTest) {

      // Récupération de l'objet ResultatTest
      ResultatTest resultatTest = formulaire.getComptagesFormulaire()
            .getResultats();
      resultatTest.clear();

      // Lecture de l'identifiant du traitement de masse
      String idTdm = formulaire.getComptagesFormulaire().getIdTdm();

      // Appel du service de comptages
      testCommons.getCaptureMasseTestService().comptages(idTdm, resultatTest,
            new Long(getCount(numeroTest)));

      // Passe le test en OK si pas KO
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

}
