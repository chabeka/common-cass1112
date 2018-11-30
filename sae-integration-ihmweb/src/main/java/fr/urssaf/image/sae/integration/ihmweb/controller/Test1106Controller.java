package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ComptagesTdmFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test1106Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
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
 * 1106-Droits-Conformite-All-ATT-VIGI
 */
@Controller
@RequestMapping(value = "test1106")
public class Test1106Controller extends
      AbstractTestWsController<Test1106Formulaire> {

   private static final String VI_ISSUER = "INT_CS_ATT_VIGI";
   private static final String VI_PAGM = "INT_PAGM_ATT_VIGI_ALL";
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1106";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testDrCuCmReCo";
   }
   
   private static final int WAITED_COUNT =11;
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test1106Formulaire getFormulairePourGet() {

      // Création du formulaire
      Test1106Formulaire formulaire = new Test1106Formulaire();

      // VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setIssuer(VI_ISSUER);
      definitViPagm(viForm, VI_PAGM);

      // Valeurs initiales du formulaire pour la capture unitaire
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);
      // L'URL ECDE
      formCapture
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Droit-1106-Droits-Conformite-All-ATT-VIGI/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));
      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "UR750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2007-04-01");
      metadonnees.add("Denomination", "Test 1106-Droits-Conformite-All-ATT-VIGI");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "d145ea8e0ca28b8c97deb0c2a550f0a969a322a3");
      metadonnees.add("NbPages", "2");
      metadonnees.add("NumeroRecours", "11");
      metadonnees.add("Siren", "3090000001");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");
      
      // Valeurs initiales des formulaires pour la capture de masse
      // Formulaire pour l'appel au WS de capture de masse
      CaptureMasseFormulaire formCaptMasseDecl = formulaire.getCaptureMasseDeclenchement();
      formCaptMasseDecl.setUrlSommaire(getEcdeService().construitUrlEcde(
            "SAE_INTEGRATION/20110822/Droit-1106-Droits-Conformite-All-ATT-VIGI/sommaire.xml"));
      formCaptMasseDecl.getResultats().setStatus(TestStatusEnum.SansStatus);
      // Formulaire de lecture des fichiers flag et du resultats.xml
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire.getCaptureMasseResultat();
      formCaptMassRes.setUrlSommaire(formCaptMasseDecl.getUrlSommaire());
      formCaptMassRes.getResultats().setStatus(TestStatusEnum.SansStatus);
      // Formulaire de comptage dans DFCE
      ComptagesTdmFormulaire comptageFormulaire = formulaire.getComptagesFormulaire();
      comptageFormulaire.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Valeurs initiales du formulaire pour la recherche
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("ApplicationProductrice");
      codesMeta.add("CodeRND");
      codesMeta.add("DateCreation");
      codesMeta.add("Denomination");
      codesMeta.add("NumeroRecours");
      codesMeta.add("Siren");
      formRecherche.setCodeMetadonnees(codesMeta);
      
      // Valeurs initiales du formulaire pour la consultation
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      formConsult.getResultats().setStatus(TestStatusEnum.SansStatus);
      CodeMetadonneeList codesMetas = formConsult.getCodeMetadonnees();
      codesMetas.add("CodeRND");
      codesMetas.add("ContratDeService");
      codesMetas.add("Denomination");

      // Fin
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test1106Formulaire formulaire) {

      String etape = formulaire.getEtape();

      if ("1".equals(etape)) {

         etape1captureUnitaire(formulaire);

      } else if ("2".equals(etape)) {

         etape2captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("3".equals(etape)) {

         etape3captureMasseLectureResultats(formulaire
               .getCaptureMasseResultat());

      } else if ("4".equals(etape)) {

         etape4recherche(formulaire.getUrlServiceWeb(), formulaire.getRecherche(),
               formulaire.getViFormulaire());

      } else if ("5".equals(etape)) {

         etape5consultation(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureUnitaire(Test1106Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptureUnitaire();
      
      // Vide le dernier id d'archivage et le dernier sha1
      formulaire.setDernierIdArchivage(null);
      formulaire.setDernierSha1(null);
      
      // Appel de la méthode de test
      CaptureUnitaireResultat consultResult = 
         getCaptureUnitaireTestService().appelWsOpCaptureUnitaireReponseAttendue(
               formulaire.getUrlServiceWeb(), formCaptureEtp1, formulaire.getViFormulaire());
      
      // Si le test est en succès ...
      if (formCaptureEtp1.getResultats().getStatus().equals(
            TestStatusEnum.Succes)) {

         // On mémorise l'identifiant d'archivage et le sha-1
         formulaire.setDernierIdArchivage(consultResult.getIdArchivage());
         formulaire.setDernierSha1(consultResult.getSha1());

         // On affecte l'identifiant d'archivage à l'étape 2 (consultation)
         ConsultationFormulaire formConsult = formulaire.getConsultation();
         formConsult.setIdArchivage(consultResult.getIdArchivage());

      }

   }

   private void etape2captureMasseAppelWs(String urlWebService,
         Test1106Formulaire formulaire) {

      // Vide le résultat du test précédent de l'étape 3
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(null);

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(urlWebService,
            formulaire.getCaptureMasseDeclenchement(), formulaire.getViFormulaire());
      
      // Renseigne le formulaire de l'étape 3
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());
      

   }

   private void etape3captureMasseLectureResultats(
         CaptureMasseResultatFormulaire formulaire) {

      getCaptureMasseTestService().testResultatsTdmReponseOKAttendue(formulaire);

   }

   private void etape4recherche(String urlWebService, RechercheFormulaire rechForm,
         ViFormulaire viParams) {

      // Initialise
      ResultatTest resultatTest = rechForm.getResultats();
      
      RechercheResponse response =  getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(urlWebService,
            rechForm, WAITED_COUNT, false, TypeComparaison.NumeroRecours,viParams );
      
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
         verifieResultatN(11, resultatsTries.get(10), resultatTest, "11");

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
      valeursAttendues.add("Denomination", "Test 1106-Droits-Conformite-All-ATT-VIGI");
      valeursAttendues.add("NumeroRecours", numeroRecours);
      valeursAttendues.add("Siren", "3090000001");

      if(ArrayUtils.contains(new String[]{"1","5","9","11"},numeroRecours)){
         valeursAttendues.add("CodeRND", "2.3.1.1.12");
      }
      if(ArrayUtils.contains(new String[]{"6","2","10"},numeroRecours)){
         valeursAttendues.add("CodeRND", "2.3.1.1.8");
      }
      if(ArrayUtils.contains(new String[]{"3","7","4","8"},numeroRecours)){
         valeursAttendues.add("CodeRND", "2.3.1.1.3");
      }

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);

   }

   private void etape5consultation(Test1106Formulaire formulaire) {

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
      metaAttendues.add(new MetadonneeValeur("ContratDeService", "INT_CS_ATT_VIGI"));
      metaAttendues.add(new MetadonneeValeur("Denomination", "Test 1106-Droits-Conformite-All-ATT-VIGI"));

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
   
   private void definitViPagm(ViFormulaire viForm, String pagm) {
      PagmList pagmList = new PagmList();
      pagmList.add(pagm);
      viForm.setPagms(pagmList);
   }

}
