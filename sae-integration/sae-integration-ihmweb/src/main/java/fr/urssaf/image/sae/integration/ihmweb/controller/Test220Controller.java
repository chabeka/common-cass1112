package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestStockageMasseAllFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureMasseResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeConsultationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 220-CaptureMasse-Avec-Hash-OK-Tor-10
 */
@Controller
@RequestMapping(value = "test220")
public class Test220Controller extends
      AbstractTestWsController<TestStockageMasseAllFormulaire> {

   /**
    * Nombre d'occurence attendu
    */
   private static final int COUNT_WAITED = 10;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "220";
   }
   
   
   private String getDebutUrlEcde() {
      return getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-220-CaptureMasse-Avec-Hash-OK-Tor-10/");
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
      formCapture.setHash("bf46fc3884dc98adcaeba36475a9ad33e56311be");
      formCapture.setTypeHash("SHA-1");
      formCapture.setAvecHash(true);

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde() + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      
      // Initialise le formulaire de recherche
      
      RechercheFormulaire rechFormulaire = formulaire.getRechFormulaire();
      rechFormulaire
            .setRequeteLucene(getCasTest().getLuceneExemple());
      
      CodeMetadonneeList codeMetadonneeList = new CodeMetadonneeList();
      rechFormulaire.setCodeMetadonnees(codeMetadonneeList);
      codeMetadonneeList.add("NumeroRecours");
      codeMetadonneeList.add("Denomination");
      
      
      // Initialise le formulaire de consultation
      
      ConsultationFormulaire formConsult = formulaire.getConsultFormulaire();
      
      formConsult.setModeConsult(ModeConsultationEnum.NouveauServiceAvecMtom);
      
      CodeMetadonneeList codeMetaConsult = new CodeMetadonneeList();
      formConsult.setCodeMetadonnees(codeMetaConsult);
      codeMetaConsult.add("Denomination");
      codeMetaConsult.add("IdTraitementMasseInterne");
      

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

         etape4Consultation(formulaire);

      } else if ("5".equals(etape)) {

         etape5Comptages(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureMasseAppelWs(String urlWebService,
         TestStockageMasseAllFormulaire formulaire) {

      // Vide le résultat du test précédent de l'étape 2 (lecture ECDE)
      formulaire.getCaptureMasseResultat().getResultats().clear();
      formulaire.getCaptureMasseResultat().setUrlSommaire(
            formulaire.getCaptureMasseDeclenchement().getUrlSommaire());
      
      // Vide le résultat du test précédent de l'étape 3 (recherche)
      formulaire.getRechFormulaire().getResultats().clear();
      
      // Vide le résultat du test précédent de l'étape 4 (consultation)
      formulaire.getConsultFormulaire().getResultats().clear();
      
      // Vide le résultat du test précédent de l'étape 5 (comptages dfce)
      formulaire.getComptagesFormulaire().getResultats().clear();
      formulaire.getComptagesFormulaire().setIdTdm(StringUtils.EMPTY);

      // Appel de la méthode de test
      CaptureMasseResultat cmResult = getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(
            urlWebService, formulaire.getCaptureMasseDeclenchement());
      
      // Stocke l'id du tdm renvoyé par la méthode
      formulaire.getComptagesFormulaire().setIdTdm(cmResult.getIdTraitement());

   }

   private void etape2captureMasseResultats(
         CaptureMasseResultatFormulaire formulaire) {

      getCaptureMasseTestService()
            .testResultatsTdmReponseOKAttendue(formulaire);

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

         // Tri les résultats par ordre croissant de NumeroRecours
         List<ResultatRechercheType> resultatsTries = Arrays.asList(response
               .getRechercheResponse().getResultats().getResultat());
         Collections.sort(resultatsTries, new ResultatRechercheComparator(
               TypeComparaison.NumeroRecours));
         
         // Vérifie chaque résultat
         for (int i=0;i<COUNT_WAITED;i++) {
            verifieResultat(resultatsTries.get(i), resultatTest, i+1);
         }
         
         
         // Si les vérifs sont OK jusque là, le test passe en OK car
         // tout a été vérifié
         if (!TestStatusEnum.Echec.equals(formulaire.getRechFormulaire()
               .getResultats().getStatus())) {

            formulaire.getConsultFormulaire().setIdArchivage(
                  results[0].getIdArchive().getUuidType());

            formulaire.getRechFormulaire().getResultats().setStatus(
                  TestStatusEnum.Succes);
         }

      }

   }

   private void verifieResultat(
         ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest,
         int numeroRecours) {
      
      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      String numeroResultatRecherche = Integer.toString(numeroRecours);
      valeursAttendues.add("Denomination", "Test 220-CaptureMasse-Avec-Hash-OK-Tor-10");
      valeursAttendues.add("NumeroRecours", numeroResultatRecherche);

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);
      
   }
   
   

   private void etape4Consultation(TestStockageMasseAllFormulaire formulaire) {
      
      // Les codes des métadonnées attendues
      CodeMetadonneeList codeMetaAttendues = new CodeMetadonneeList();
      codeMetaAttendues.add("Denomination");
      codeMetaAttendues.add("IdTraitementMasseInterne");
      
      // Valeurs des métadonnées attendues
      List<MetadonneeValeur> valeursMetaAttendus = new ArrayList<MetadonneeValeur>();
      valeursMetaAttendus.add(new MetadonneeValeur("Denomination","Test 220-CaptureMasse-Avec-Hash-OK-Tor-10"));
      valeursMetaAttendus.add(new MetadonneeValeur("IdTraitementMasseInterne",
            formulaire.getComptagesFormulaire().getIdTdm()));
      
      // Appel du service de vérification
      getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getConsultFormulaire(),
                  "a2f93f1f121ebba0faef2c0596f2f126eacae77b",
                  codeMetaAttendues,
                  valeursMetaAttendus);
      
      // Si le test n'est pas en échec, alors on peut le passer en succès,
      // car tout a pu être vérifié
      ResultatTest resultatTest = formulaire.getConsultFormulaire().getResultats();
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
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