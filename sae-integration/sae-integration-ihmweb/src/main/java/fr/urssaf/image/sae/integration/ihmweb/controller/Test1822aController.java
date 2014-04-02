package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireFcpCmReCo;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 1822-1-FCP-CM-OK-Identification-NonValidation-Monitor-FMT354-OK
 */
@Controller
@RequestMapping(value = "test1822a")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1822aController extends
      AbstractTestWsController<TestFormulaireFcpCmReCo> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1822a";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testFcpCmReCo";
   }

   private String getDebutUrlEcde() {
      return getEcdeService()
            .construitUrlEcde(
                  "SAE_INTEGRATION/20110822/FCP-1822-1-FCP-CM-OK-Identification-NonValidation-Monitor-FMT354-OK/");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestFormulaireFcpCmReCo getFormulairePourGet() {

      TestFormulaireFcpCmReCo formulaire = new TestFormulaireFcpCmReCo();

      // capture masse
      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();

      // L'URL ECDE DU SOMMAIRE.XML
      formCapture.setUrlSommaire(getDebutUrlEcde() + "sommaire.xml");

      // formCapture.setHash("23ec83cefdd26f30b68ecbbae1ce6cf6560bca44");
      // formCapture.setTypeHash("SHA-1");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);

      // resultat de la capture de masse
      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde() + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      // formulaire de recherche

      RechercheFormulaire formRecherche = formulaire.getRechercheFormulaire();
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Pas de métadonnées spécifiques à récupérer
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("CodeRND");
      codesMeta.add("NumeroRecours");

      formRecherche.setCodeMetadonnees(codesMeta);

      // Valeurs initiales du formulaire pour la consultation
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      formConsult.getResultats().setStatus(TestStatusEnum.SansStatus);
      CodeMetadonneeList codesMetas = formConsult.getCodeMetadonnees();
      codesMetas.add("CodeRND");
      codesMetas.add("Hash");

      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("INT_CS_FORMAT_FMT_354_TFM");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add("INT_PAGM_FORMAT_FMT_354_TFM_ARCH_MASSE");

      return formulaire;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestFormulaireFcpCmReCo formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {
         etape1captureMasseAppelWs(formulaire);
         // PagmList pagmList = new PagmList();
         // pagmList.add("INT_PAGM_ATT_VIGI_RECH");
         // formulaire.getViFormulaire().setPagms(pagmList);
      } else if ("2".equals(etape)) {

         etape2captureMasseResultats(formulaire.getCaptureMasseResultat());

      } else if ("3".equals(etape)) {
         recherche(formulaire.getUrlServiceWeb(), formulaire
               .getRechercheFormulaire(), formulaire.getViFormulaire());
      } else if ("4".equals(etape)) {

         etape4consultation(formulaire);
      } else if ("5".equals(etape)) {
         etape5Comptages(formulaire);
      }
   }

   private void etape1captureMasseAppelWs(TestFormulaireFcpCmReCo formulaire) {

      // Initialise
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());

      // Lance le test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(
            formulaire.getUrlServiceWeb(),
            formulaire.getCaptureMasseDeclenchement(),
            formulaire.getViFormulaire());
   }

   private void etape2captureMasseResultats(
         CaptureMasseResultatFormulaire formulaire) {

      getCaptureMasseTestService()
            .testResultatsTdmReponseOKAttendue(formulaire);
      
      // Si le test n'est pas en échec, alors on peut le passer a "A controler",
      // car il faut vérifier que l'on trouve dans les logs de debug des traces d'identification
      ResultatTest resultatTest = formulaire.getResultats();
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.AControler);
      }
   }

   private void recherche(String urlServiceWeb, RechercheFormulaire formulaire,
         ViFormulaire viParams) {

      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

      // Résultats attendus
      int nbResultatsAttendus = 10;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(urlServiceWeb,
                  formulaire, nbResultatsAttendus,
                  flagResultatsTronquesAttendu, null);

      // Vérifications en profondeur
      if ((response != null)
            && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {

         // Tri les résultats par ordre croissant de DateCreation
         List<ResultatRechercheType> resultatsTries = Arrays.asList(response
               .getRechercheResponse().getResultats().getResultat());
         Collections.sort(resultatsTries, new ResultatRechercheComparator(
               TypeComparaison.NumeroRecours));

         // Vérifie chaque résultat
         verifieResultatN(1, resultatsTries.get(0), resultatTest, "1");
         verifieResultatN(2, resultatsTries.get(1), resultatTest, "2");
         verifieResultatN(3, resultatsTries.get(2), resultatTest, "3");
         verifieResultatN(4, resultatsTries.get(3), resultatTest, "4");
         verifieResultatN(5, resultatsTries.get(4), resultatTest, "5");
         verifieResultatN(6, resultatsTries.get(5), resultatTest, "6");
         verifieResultatN(7, resultatsTries.get(6), resultatTest, "7");
         verifieResultatN(8, resultatsTries.get(7), resultatTest, "8");
         verifieResultatN(9, resultatsTries.get(8), resultatTest, "9");
         verifieResultatN(10, resultatsTries.get(9), resultatTest, "10");
      }

      // On passe le test à OK si tous les contrôles sont passées
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }
   }

   private void verifieResultatN(int numeroResultatRecherche,
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest,
         String numeroRecours) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("NumeroRecours", numeroRecours);

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);

   }
   
   private void etape4consultation(TestFormulaireFcpCmReCo formulaire) {

      // Initialise
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      ResultatTest resultatTestConsult = formConsult.getResultats();
      
      // Le SHA-1 attendu
      String sha1attendu = null;
      String idArchivageDemande = formConsult.getIdArchivage();
      String dernierIdArchivageCapture = formulaire.getDernierIdArchivage();
      String dernierSha1capture = formulaire.getDernierSha1();
      if ((idArchivageDemande.equals(dernierIdArchivageCapture))
            && (StringUtils.isNotBlank(dernierSha1capture))) {
         sha1attendu = formulaire.getDernierSha1();
      }

      // Valeurs des métadonnées attendues après l'appel à la consult
      List<MetadonneeValeur> metaAttendues = new ArrayList<MetadonneeValeur>();
      metaAttendues.add(new MetadonneeValeur("CodeRND", "2.3.1.1.12"));
      metaAttendues.add(new MetadonneeValeur("Hash", "c3a2c0e577b1e7b734c6e1ad03a70edf7a2262cd"));
      
      // Lance le test
      getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(), formConsult, sha1attendu,
                  formulaire.getConsultation().getCodeMetadonnees(),
                  metaAttendues);

      // Si le test n'est pas en échec, alors il est OK (tout peut être vérifié)
      if (!TestStatusEnum.Echec.equals(resultatTestConsult.getStatus())) {
         resultatTestConsult.setStatus(TestStatusEnum.Succes);
      }

   }
   
   private void etape5Comptages(TestFormulaireFcpCmReCo formulaire) {

      // Récupération de l'objet ResultatTest
      ResultatTest resultatTest = formulaire.getComptagesFormulaire()
            .getResultats();
      resultatTest.clear();

      // Lecture de l'identifiant du traitement de masse
      String idTdm = formulaire.getComptagesFormulaire().getIdTdm();

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest,
            new Long(10));

      // Passe le test en OK si pas KO
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }
}
