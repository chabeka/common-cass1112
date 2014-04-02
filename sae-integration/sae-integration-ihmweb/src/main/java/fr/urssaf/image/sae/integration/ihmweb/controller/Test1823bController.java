package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireFcpCmReCo;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.FichierType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.NonIntegratedDocumentType;

/**
 * 1823-2-FCP-CM-OK-Identification-NonValidation-Strict-FMT354-KO
 */
@Controller
@RequestMapping(value = "test1823b")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1823bController extends
      AbstractTestWsController<TestFormulaireFcpCmReCo> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1823b";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testFcpCmReTrace";
   }

   private String getDebutUrlEcde() {
      return getEcdeService()
            .construitUrlEcde(
                  "SAE_INTEGRATION/20110822/FCP-1823-2-FCP-CM-OK-Identification-NonValidation-Strict-FMT354-KO/");
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
      codesMeta.add("Hash");

      formRecherche.setCodeMetadonnees(codesMeta);

      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("INT_CS_FORMAT_FMT_354_TFS");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add("INT_PAGM_FORMAT_FMT_354_TFS_ARCH_MASSE");

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
         etape4Comptages(formulaire);
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

      ErreurType error = new ErreurType();
      error.setCode("SAE-CA-BUL002");
      error.setLibelle("Le document doc1.PDF n'a pas été archivé. "
            + "Détails : Le fichier à archiver ne correspond pas au format spécifié.");

      ListeErreurType errorList = new ListeErreurType();
      errorList.getErreur().add(error);

      FichierType fichierType = new FichierType();
      fichierType.setCheminEtNomDuFichier("doc1.PDF");
      
      NonIntegratedDocumentType documentType = new NonIntegratedDocumentType();
      documentType.setErreurs(errorList);
      documentType.setObjetNumerique(fichierType);
      
      getCaptureMasseTestService()
            .testResultatsTdmReponseKOAttendue(formulaire, 10, documentType, 0);
   }

   private void recherche(String urlServiceWeb, RechercheFormulaire formulaire,
         ViFormulaire viParams) {

      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

      // Résultats attendus
      int nbResultatsAttendus = 0;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            urlServiceWeb, formulaire, nbResultatsAttendus,
            flagResultatsTronquesAttendu, null);

      // On passe le test à OK si tous les contrôles sont passées
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }
   }
   
   private void etape4Comptages(TestFormulaireFcpCmReCo formulaire) {

      // Récupération de l'objet ResultatTest
      ResultatTest resultatTest = formulaire.getComptagesFormulaire()
            .getResultats();
      resultatTest.clear();

      // Lecture de l'identifiant du traitement de masse
      String idTdm = formulaire.getComptagesFormulaire().getIdTdm();

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest,
            new Long(0));

      // Passe le test en OK si pas KO
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }
      
   }
}
