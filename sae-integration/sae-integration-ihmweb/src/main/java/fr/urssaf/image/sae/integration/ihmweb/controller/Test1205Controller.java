package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireDrCmRe;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 1205-PKI-CS-PLUSIEURS-CERTIFICATS-APPLICATIFS
 */
@Controller
@RequestMapping(value = "test1205")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1205Controller extends
      AbstractTestWsController<TestFormulaireDrCmRe> {

   private static int WAITED_COUNT = 10;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1205";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testDrCmRe";
   }

   private String getDebutUrlEcde() {
      return getEcdeService()
            .construitUrlEcde(
                  "SAE_INTEGRATION/20110822/PKI-1205-PKI-CS-PLUSIEURS-CERTIFICATS-APPLICATIFS/");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestFormulaireDrCmRe getFormulairePourGet() {

      TestFormulaireDrCmRe formulaire = new TestFormulaireDrCmRe();
      RechercheFormulaire formRecherche = formulaire.getRechercheFormulaire();
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getDebutUrlEcde() + "sommaire.xml");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde() + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Pas de métadonnées spécifiques à récupérer
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("ApplicationProductrice");
      codesMeta.add("CodeRND");
      codesMeta.add("DateCreation");
      codesMeta.add("Denomination");
      codesMeta.add("NumeroRecours");
      codesMeta.add("Siren");
      formRecherche.setCodeMetadonnees(codesMeta);

      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("INT_CS_2_CERTIFS");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add("INT_PAGM_2_CERTIFS_ARCH_MASSE");
      viForm
            .setIdCertif(SaeIntegrationConstantes.PKI_IGC_AED_PNR_APPLI_TEST);

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestFormulaireDrCmRe formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         PagmList pagmList = new PagmList();
         formulaire.getViFormulaire().setPagms(pagmList);
         pagmList.add("INT_PAGM_2_CERTIFS_ARCH_MASSE");
         
         etape1captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("2".equals(etape)) {

         etape2captureMasseResultats(formulaire.getCaptureMasseResultat());
         PagmList pagmList = new PagmList();
         formulaire.getViFormulaire().setPagms(pagmList);
         pagmList.add("INT_PAGM_2_CERTIFS_ALL");

      } else if ("3".equals(etape)) {

         recherche(formulaire.getUrlServiceWeb(), formulaire
               .getRechercheFormulaire(), formulaire.getViFormulaire());
         
         // apres, le passage du premier test, on prevoit le passage du second
         // en changeant le certificat, et le nom d'element recherche
         formulaire.getViFormulaire()
            .setIdCertif(SaeIntegrationConstantes.PKI_IGC_AED_APPLI_TEST_SAE);
         WAITED_COUNT = 20;
         
      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureMasseAppelWs(String urlWebService,
         TestFormulaireDrCmRe formulaire) {

      // Vide le résultat du test précédent de l'étape 2
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(
            urlWebService, formulaire.getCaptureMasseDeclenchement(),
            formulaire.getViFormulaire());

   }

   private void etape2captureMasseResultats(
         CaptureMasseResultatFormulaire captureMasseResultat) {

      getCaptureMasseTestService().testResultatsTdmReponseOKAttendue(
            captureMasseResultat);
   }

   private void recherche(String urlServiceWeb, RechercheFormulaire formulaire,
         ViFormulaire viParams) {

      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

      // Résultats attendus
      int nbResultatsAttendus = WAITED_COUNT;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(urlServiceWeb,
                  formulaire, nbResultatsAttendus,
                  flagResultatsTronquesAttendu, TypeComparaison.NumeroRecours,
                  viParams);

      // Vérifications en profondeur
      if ((response != null)
            && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {

         // Tri les résultats par ordre croissant de DateCreation
         List<ResultatRechercheType> resultatsTries = Arrays.asList(response
               .getRechercheResponse().getResultats().getResultat());
         Collections.sort(resultatsTries, new ResultatRechercheComparator(
               TypeComparaison.NumeroRecours));

         // Vérifie chaque résultat
         if (nbResultatsAttendus == 10) {
            // cas ou on n'a 10 enregistrements attendus car premier passage des tests
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
         } else {
            // cas ou on n'a 20 enregistrements attendus car deuxieme passage des tests
            // avec le second certificat
            verifieResultatN(1, resultatsTries.get(0), resultatTest, "1");
            verifieResultatN(2, resultatsTries.get(1), resultatTest, "1");
            verifieResultatN(3, resultatsTries.get(2), resultatTest, "2");
            verifieResultatN(4, resultatsTries.get(3), resultatTest, "2");
            verifieResultatN(5, resultatsTries.get(4), resultatTest, "3");
            verifieResultatN(6, resultatsTries.get(5), resultatTest, "3");
            verifieResultatN(7, resultatsTries.get(6), resultatTest, "4");
            verifieResultatN(8, resultatsTries.get(7), resultatTest, "4");
            verifieResultatN(9, resultatsTries.get(8), resultatTest, "5");
            verifieResultatN(10, resultatsTries.get(9), resultatTest, "5");
            verifieResultatN(11, resultatsTries.get(10), resultatTest, "6");
            verifieResultatN(12, resultatsTries.get(11), resultatTest, "6");
            verifieResultatN(13, resultatsTries.get(12), resultatTest, "7");
            verifieResultatN(14, resultatsTries.get(13), resultatTest, "7");
            verifieResultatN(15, resultatsTries.get(14), resultatTest, "8");
            verifieResultatN(16, resultatsTries.get(15), resultatTest, "8");
            verifieResultatN(17, resultatsTries.get(16), resultatTest, "9");
            verifieResultatN(18, resultatsTries.get(17), resultatTest, "9");
            verifieResultatN(19, resultatsTries.get(18), resultatTest, "10");
            verifieResultatN(20, resultatsTries.get(19), resultatTest, "10");
         }
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

      valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
      valeursAttendues.add("DateCreation", "2007-04-01");
      valeursAttendues.add("Denomination",
         "Test 1205-PKI-CS-PLUSIEURS-CERTIFICATS-APPLICATIFS");
      
      
      if(ArrayUtils.contains(new String[]{"1","5","9"},numeroRecours)){
         valeursAttendues.add("CodeRND", "2.3.1.1.12");
      }
      if(ArrayUtils.contains(new String[]{"2","6","10"},numeroRecours)){
         valeursAttendues.add("CodeRND", "2.3.1.1.8");
      }
      if(ArrayUtils.contains(new String[]{"3","4","7", "8"},numeroRecours)){
         valeursAttendues.add("CodeRND", "2.3.1.1.3");
      }
      
      if(ArrayUtils.contains(new String[]{"7"},numeroRecours)){
         valeursAttendues.add("Siren", "3090000002");
      } else {
         valeursAttendues.add("Siren", "3090000001");
      }

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);

   }

}
