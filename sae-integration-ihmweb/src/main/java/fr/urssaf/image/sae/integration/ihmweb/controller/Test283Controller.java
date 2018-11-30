package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ComptagesTdmFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestStockageMasseAllFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.FichierType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.NonIntegratedDocumentType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 283-CaptureMasse-Pile-KO-ArretDFCE-Stockage
 */
@Controller
@RequestMapping(value = "test283")
public class Test283Controller extends
      AbstractTestWsController<TestStockageMasseAllFormulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "283";
   }

   private String getDebutUrlEcde() {
      return getEcdeService()
            .construitUrlEcde(
                  "SAE_INTEGRATION/20110822/CaptureMasse-283-CaptureMasse-Pile-KO-ArretDFCE-Stockage/");
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

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde() + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Initialise le formulaire de recherche

      RechercheFormulaire rechFormulaire = formulaire.getRechFormulaire();
      rechFormulaire.setRequeteLucene(getCasTest().getLuceneExemple());

      CodeMetadonneeList codeMetadonneeList = new CodeMetadonneeList();
      rechFormulaire.setCodeMetadonnees(codeMetadonneeList);
      codeMetadonneeList.add("CodeRND");
      codeMetadonneeList.add("DateArchivage");
      codeMetadonneeList.add("NumeroRecours");
      
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

         // initialise l'identifiant de traitement de masse en lisant le fichier
         // debut_traitement.flag
         String idTdm = getCaptureMasseTestService().readIdTdmInDebutTrait(
               formulaire.getCaptureMasseDeclenchement().getUrlSommaire());
         ComptagesTdmFormulaire formComptage = formulaire
               .getComptagesFormulaire();
         formComptage.setIdTdm(idTdm);

      } else if ("3".equals(etape)) {

         etape3Recherche(formulaire);

      } else if ("4".equals(etape)) {

         etape4Comptages(formulaire);

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
      erreurType.setCode("SAE-CA-BUL003");
      erreurType
            .setLibelle("La capture de masse en mode \"Tout ou rien\" a été interrompue. Une procédure d'exploitation a été initialisée pour supprimer les données qui auraient pu être stockées.");
      
      ListeErreurType listeErreurType = new ListeErreurType();
      listeErreurType.getErreur().add(erreurType);
      
      FichierType fichierType = new FichierType();
      fichierType.setCheminEtNomDuFichier("doc1.PDF");

      NonIntegratedDocumentType documentType = new NonIntegratedDocumentType();
      documentType.setErreurs(listeErreurType);
      documentType.setObjetNumerique(fichierType);
      
      getCaptureMasseTestService()
            .testResultatsTdmReponseKOAttendue(formulaire, 3000, documentType);

   }

   private void etape3Recherche(TestStockageMasseAllFormulaire formulaire) {

      // Appel le service de test de la recherche
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getRechFormulaire(), null, null,
                  TypeComparaison.NumeroRecours);

      ResultatTest resultatTest = formulaire.getRechFormulaire().getResultats();

      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         
         // ne verifie les resultats que si on a des resultats
         if (response
               .getRechercheResponse().getResultats().getResultat() != null) {
         
            // Tri les résultats par ordre croissant de NumeroRecours
            List<ResultatRechercheType> resultatsTries = Arrays.asList(response
                  .getRechercheResponse().getResultats().getResultat());
            Collections.sort(resultatsTries, new ResultatRechercheComparator(
                  TypeComparaison.NumeroRecours));
         
            // Vérifie chaque résultat
            for (int i = 0; i < resultatsTries.size(); i++) {
               verifieResultat(resultatsTries.get(i), resultatTest, i + 1, response
                     .getRechercheResponse().getResultatTronque());
            }
         }
         
         if (!TestStatusEnum.Echec.equals(formulaire.getRechFormulaire()
               .getResultats().getStatus())) {
            if (!response
                     .getRechercheResponse().getResultatTronque()) {
               resultatTest.setStatus(TestStatusEnum.Succes);
            } else {
               resultatTest.setStatus(TestStatusEnum.AControler);
            }
         }
      }

   }
   
   private void verifieResultat(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest, int numeroRecours, boolean resultatTronque) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      String numeroResultatRecherche = Integer.toString(numeroRecours);
      
      Date now = new Date();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("DateArchivage", formatter.format(now));
      if (!resultatTronque) {
         valeursAttendues.add("NumeroRecours", Integer.toString(numeroRecours));
      }
      
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);
   }

   private void etape4Comptages(TestStockageMasseAllFormulaire formulaire) {

      // Récupération de l'objet ResultatTest
      ResultatTest resultatTest = formulaire.getComptagesFormulaire()
            .getResultats();
      resultatTest.clear();

      // Lecture de l'identifiant du traitement de masse
      String idTdm = formulaire.getComptagesFormulaire().getIdTdm();

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest,
            null);

      // Passe le test en OK si pas KO
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

}
