package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ComptagesTdmFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test212Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 212-CaptureMasse-Pile-OK-ECDE-local
 */
@Controller
@RequestMapping(value = "test212")
public class Test212Controller extends
      AbstractTestWsController<Test212Formulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "212";
   }

   private String getDebutUrlEcde(int index) {
      if (index == 0) {
         return getEcdeService()
               .construitUrlEcde(
                     "SAE_INTEGRATION/20110822/CaptureMasse-212-CaptureMasse-Pile-OK-ECDE-local-1/",
                     index);
      } else if (index == 1) {
         return getEcdeService()
               .construitUrlEcde(
                     "SAE_INTEGRATION/20110822/CaptureMasse-212-CaptureMasse-Pile-OK-ECDE-local-2/",
                     index);
      } else {
         return getEcdeService()
               .construitUrlEcde(
                     "SAE_INTEGRATION/20110822/CaptureMasse-212-CaptureMasse-Pile-OK-ECDE-local/",
                     index);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test212Formulaire getFormulairePourGet() {

      Test212Formulaire formulaire = new Test212Formulaire();
      // Initialise le formulaire de capture de masse

      // comme on appel deux service de capture de mass on renseigne l'url avec
      // les valeurs possibles du ficher ecdesources.xml
      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getDebutUrlEcde(0) + "sommaire.xml");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);

      // initialisation de l'url non local pour l'appel de la capture de masse
      CaptureMasseFormulaire formCaptureNonLocal = formulaire
            .getCaptureMasseDeclenchementNonLocal();
      formCaptureNonLocal.setUrlSommaire(getDebutUrlEcde(1) + "sommaire.xml");
      formCaptureNonLocal.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde(0) + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      // initialisation de l'url non local pour l'appel de la recherche
      CaptureMasseResultatFormulaire formResultatNonLocal = formulaire
            .getCaptureMasseResultatNonLocal();
      formResultatNonLocal.setUrlSommaire(getDebutUrlEcde(1) + "resultat.xml");
      formResultatNonLocal.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Initialise le formulaire de recherche de l'étape 5
      RechercheFormulaire rechFormulaire = formulaire.getRechFormulaire();
      // La requête LUCENE
      rechFormulaire.setRequeteLucene(getCasTest().getLuceneExemple());
      // Les métadonnées que l'on souhaite en retour
      CodeMetadonneeList codesMeta1 = new CodeMetadonneeList();
      rechFormulaire.setCodeMetadonnees(codesMeta1);
      codesMeta1.add("CodeRND");
      codesMeta1.add("DateArchivage");
      codesMeta1.add("NumeroRecours");

      // Initialise le formulaire de recherche de l'étape 6
      RechercheFormulaire rechFormulaireNonLocal = formulaire
            .getRechFormulaireNonLocal();
      // le fichier listeCasTest.xml n'acceptant qu'une chaîne Lucene on
      // spécifie le chaîne de recherche non local manuellement
      rechFormulaireNonLocal
            .setRequeteLucene("Denomination:\"Test 212-CaptureMasse-Pile-OK-ECDE-local-2\"");
      // Les métadonnées que l'on souhaite en retour
      CodeMetadonneeList codesMeta2 = new CodeMetadonneeList();
      rechFormulaireNonLocal.setCodeMetadonnees(codesMeta2);
      codesMeta2.add("CodeRND");
      codesMeta2.add("DateArchivage");
      codesMeta2.add("NumeroRecours");

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test212Formulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureMasseLocalAppelWs(formulaire);

      } else if ("2".equals(etape)) {

         etape2captureMasseNonLocalAppelWs(formulaire);

      } else if ("3".equals(etape)) {

         etape3captureMasseLocalResultats(formulaire);

      } else if ("4".equals(etape)) {

         etape4captureMasseNonLocalResultats(formulaire);

      } else if ("5".equals(etape)) {

         etape5RechercheLocal(formulaire);

      } else if ("6".equals(etape)) {

         etape6RechercheNonLocal(formulaire);

      } else if ("7".equals(etape)) {

         etape7ComptagesLocal(formulaire);

      } else if ("8".equals(etape)) {

         etape8ComptagesNonLocal(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureMasseLocalAppelWs(Test212Formulaire formulaire) {

      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();

      CaptureMasseFormulaire captMassForm = formulaire
            .getCaptureMasseDeclenchement();

      String urlSommaire = formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire();

      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(urlSommaire);

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(
            formulaire.getUrlServiceWeb(), captMassForm);

   }

   private void etape2captureMasseNonLocalAppelWs(Test212Formulaire formulaire) {

      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultatNonLocal();

      CaptureMasseFormulaire captMassForm = formulaire
            .getCaptureMasseDeclenchementNonLocal();

      String urlSommaire = formulaire.getCaptureMasseDeclenchementNonLocal()
            .getUrlSommaire();

      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(urlSommaire);

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(
            formulaire.getUrlServiceWeb(), captMassForm);

   }

   private void etape3captureMasseLocalResultats(Test212Formulaire formulaire) {

      CaptureMasseResultatFormulaire formCaptMasse = formulaire
            .getCaptureMasseResultat();
      
      formCaptMasse.getResultats().clear();

      getCaptureMasseTestService().testResultatsTdmReponseOKAttendue(
            formCaptMasse);
      
   }

   private void etape4captureMasseNonLocalResultats(Test212Formulaire formulaire) {

      CaptureMasseResultatFormulaire formCaptMasse = formulaire
            .getCaptureMasseResultatNonLocal();
      
      ResultatTest resultatTest = formCaptMasse.getResultats(); 
      resultatTest.clear();

      getCaptureMasseTestService().testResultatsTdmReponseAucunFichierAttendu(
            formCaptMasse, formCaptMasse.getUrlSommaire());
      
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.AControler);
      }

   }

   private void etape5RechercheLocal(Test212Formulaire formulaire) {

      // Nombre de résultats attendus
      int nbResultatsAttendus = 10;

      // Récupération de l'objet de formulaire
      RechercheFormulaire rechForm = formulaire.getRechFormulaire();
      ;

      // Initialise
      ResultatTest resultatTest = rechForm.getResultats();

      // Appel le service de test de la recherche
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(), rechForm, nbResultatsAttendus,
                  false, TypeComparaison.NumeroRecours);

      // Vérifications en profondeur
      if ((response != null)
            && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {

         // Tri les résultats par ordre croissant de NumeroRecours
         List<ResultatRechercheType> resultatsTries = Arrays.asList(response
               .getRechercheResponse().getResultats().getResultat());
         Collections.sort(resultatsTries, new ResultatRechercheComparator(
               TypeComparaison.NumeroRecours));

         // Vérifie chaque résultat
         for (int i = 0; i < nbResultatsAttendus; i++) {
            etape5RechercheLocalVerifieResultat(resultatsTries.get(i), String
                  .valueOf(i + 1), resultatTest);
         }

         // Si pas d'échec du test jusque là
         if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {

            // Passe le test en "à contrôler" si aucune erreur détectée
            resultatTest.setStatus(TestStatusEnum.AControler);

            // Initialise le formulaire de consultation
            formulaire.getConsultFormulaire().setIdArchivage(
                  resultatsTries.get(0).getIdArchive().toString());

         }

      }

   }

   private void etape5RechercheLocalVerifieResultat(
         ResultatRechercheType resultatRecherche,
         String numeroResultatRecherche, ResultatTest resultatTest) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("NumeroRecours", numeroResultatRecherche);

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

   private void etape6RechercheNonLocal(Test212Formulaire formulaire) {

      // Nombre de résultats attendus
      int nbResultatsAttendus = 0;

      // Récupération de l'objet de formulaire
      RechercheFormulaire rechForm = formulaire.getRechFormulaireNonLocal();

      // Appel le service de test de la recherche
      getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            formulaire.getUrlServiceWeb(), rechForm, nbResultatsAttendus,
            false, TypeComparaison.NumeroRecours);

   }

   private void etape7ComptagesLocal(Test212Formulaire formulaire) {

      // Nombre de résultats attendus
      int nbResultatsAttendus = 10;
      
      // Récupération de l'objet formulaire
      ComptagesTdmFormulaire formComptage = formulaire.getComptagesFormulaire();

      // Récupération de l'objet ResultatTest
      ResultatTest resultatTest = formComptage.getResultats();
      resultatTest.clear();

      // Lecture de l'identifiant du traitement de masse
      String idTdm = formComptage.getIdTdm();

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest,
            new Long(nbResultatsAttendus));

      // Passe le test en OK si pas KO
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

   private void etape8ComptagesNonLocal(Test212Formulaire formulaire) {

      // Nombre de résultats attendus
      int nbResultatsAttendus = 0;
      
      // Récupération de l'objet formulaire
      ComptagesTdmFormulaire formComptage = formulaire.getComptagesFormulaireNonLocal(); 

      // Récupération de l'objet ResultatTest
      ResultatTest resultatTest = formComptage.getResultats();
      resultatTest.clear();

      // Lecture de l'identifiant du traitement de masse
      String idTdm = formComptage.getIdTdm();

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest,
            new Long(nbResultatsAttendus));

      // Passe le test en OK si pas KO
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

}
