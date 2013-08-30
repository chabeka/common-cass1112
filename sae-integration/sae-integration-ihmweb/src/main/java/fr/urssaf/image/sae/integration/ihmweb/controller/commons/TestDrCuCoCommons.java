package fr.urssaf.image.sae.integration.ihmweb.controller.commons;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireDrCuCo;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeArchivageUnitaireEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeConsultationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;

/**
 * Méthodes communes pour les tests 1111a, 1111b, 1116a et 1116b
 */
@Component
public class TestDrCuCoCommons {

   @Autowired
   private TestsControllerCommons testCommons;

   private final String CONSULTATION = "consultation";
   private final String CAPTURE = "capture";

   private String getDenomination(String numeroTest) {
      if ("1111a".equals(numeroTest)) {
         return "1111-Droits-Conformite-Consultation-ATT-AEPL";
      } else if ("1111b".equals(numeroTest)) {
         return "1111-Droits-Conformite-Consultation-MTOM-ATT-AEPL";
      } else if ("1117a".equals(numeroTest)) {
         return "1117-Droits-Conformite-Consultation-UNE-META";
      } else if ("1117b".equals(numeroTest)) {
         return "1117-Droits-Conformite-Consultation-UNE-META";
      } else if ("1123a".equals(numeroTest)) {
         return "1123-Droits-Conformite-Consultation-PLUSIEURS-META";
      } else if ("1123b".equals(numeroTest)) {
         return "1123-Droits-Conformite-Consultation-MTOM-PLUSIEURS-META";
      } else if ("1131a".equals(numeroTest)) {
         return "1131-Droits-Conformite-Consultation-PRMD-DYNA";
      } else if ("1131b".equals(numeroTest)) {
         return "1131-Droits-Conformite-Consultation-MTOM-PRMD-DYNA";
      } else if ("1156a".equals(numeroTest)) {
         return "1156-Droits-Consultation-KO-PRMD-DYNA-INNACCESSIBLE";
      } else if ("1156b".equals(numeroTest)) {
         return "1156-Droits-Consultation-MTOM-KO-PRMD-DYNA-INNACCESSIBLE";
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   private ModeArchivageUnitaireEnum getModeArchivage(String numeroTest) {
      if (ArrayUtils.contains(new String[] { "1111a", "1117a", "1123a",
            "1131a", "1156a" }, numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitaire;
      } else if (ArrayUtils.contains(new String[] { "1111b", "1117b", "1123b",
            "1131b", "1156b" }, numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJContenuAvecMtom;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   private void setViForm(String numeroTest, TestFormulaireDrCuCo formulaire,
         String action) {
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      if ("1111".equals(numeroTest.substring(0, 4))) {
         viForm.setIssuer("INT_CS_ATT_AEPL");
         if (CAPTURE.equals(action)) {
            pagmList.add("INT_PAGM_ATT_AEPL_ALL");
         }
         if (CONSULTATION.equals(action)) {
            pagmList.add("INT_PAGM_ATT_AEPL_CONSULT");
         }
      } else if ("1117".equals(numeroTest.substring(0, 4))) {
         viForm.setIssuer("INT_CS_UNE_META");
         if (CAPTURE.equals(action)) {
            pagmList.add("INT_PAGM_UNE_META_ARCH_UNIT");
         }
         if (CONSULTATION.equals(action)) {
            pagmList.add("INT_PAGM_UNE_META_CONSULT");
         }

      } else if ("1123".equals(numeroTest.substring(0, 4))) {
         viForm.setIssuer("INT_CS_PLUSIEURS_META");
         if (CAPTURE.equals(action)) {
            pagmList.add("INT_PAGM_PLUSIEURS_META_ARCH_UNIT");
         }
         if (CONSULTATION.equals(action)) {
            pagmList.add("INT_PAGM_PLUSIEURS_META_CONSULT");
         }
      } else if ("1131".equals(numeroTest.substring(0, 4))) {
         viForm.setIssuer("INT_CS_PRMD_DYNA_CODERND");
         if (CAPTURE.equals(action)) {
            pagmList.add("INT_PAGM_PRMD_DYNA_CODERND_ALL");
         }
         if (CONSULTATION.equals(action)) {
            pagmList.add("INT_PAGM_PRMD_DYNA_CODERND_CONSULT");
         }
      } else if ("1156".equals(numeroTest.substring(0, 4))) {
         viForm.setIssuer("INT_CS_PRMD_DYNA_MULTI");
         if (CAPTURE.equals(action)) {
            pagmList.add("INT_PAGM_PRMD_DYNA_MULTI_ARCH");
         }
         if (CONSULTATION.equals(action)) {
            pagmList.add("INT_PAGM_PRMD_DYNA_MULTI_CONSULT");
         }
      }

      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
   }

   public final TestFormulaireDrCuCo getFormulairePourGet(String numeroTest) {

      TestFormulaireDrCuCo formulaire = new TestFormulaireDrCuCo();

      setViForm(numeroTest, formulaire, CAPTURE);

      // Valeurs initiales du formulaire pour la capture unitaire
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);

      MetadonneeValeurList metasExemples = ReferentielMetadonneesService
            .getMetadonneesExemplePourCapture();
      formCapture.getMetadonnees().modifieValeurMeta("CodeRND", "2.3.1.1.13");
      formCapture.setModeCapture(getModeArchivage(numeroTest));
      formCapture.setNomFichier("ADELPF_710_PSNV211157BPCA1L0000.pdf");

      if ("1111".equals(numeroTest.substring(0, 4))) {
         formCapture
               .setUrlEcde(testCommons
                     .getEcdeService()
                     .construitUrlEcde(
                           "SAE_INTEGRATION/20110822/Droit-1111-Droits-Conformite-Consultation-ATT-AEPL/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));

      } else if ("1117".equals(numeroTest.substring(0, 4))) {
         formCapture
               .setUrlEcde(testCommons
                     .getEcdeService()
                     .construitUrlEcde(
                           "SAE_INTEGRATION/20110822/Droit-1117-Droits-Conformite-Consultation-UNE-META/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));

         formCapture.getMetadonnees().add("Siren", "3090000001");
      } else if ("1123".equals(numeroTest.substring(0, 4))) {
         formCapture
               .setUrlEcde(testCommons
                     .getEcdeService()
                     .construitUrlEcde(
                           "SAE_INTEGRATION/20110822/Droit-1123-Droits-Conformite-Consultation-PLUSIEURS-META/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));

         formCapture.getMetadonnees().add("Siren", "3090000001");
         metasExemples.modifieValeurMeta("CodeRND", "2.3.1.1.12");
      } else if ("1131".equals(numeroTest.substring(0, 4))) {
         formCapture
               .setUrlEcde(testCommons
                     .getEcdeService()
                     .construitUrlEcde(
                           "SAE_INTEGRATION/20110822/Droit-1131-Droits-Conformite-Consultation-PRMD-DYNA/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));

      } else if ("1156".equals(numeroTest.substring(0, 4))) {
         formCapture
               .setUrlEcde(testCommons
                     .getEcdeService()
                     .construitUrlEcde(
                           "SAE_INTEGRATION/20110822/Droit-1156-Droits-Consultation-KO-PRMD-DYNA-INNACCESSIBLE/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));
      }

      // Des métadonnées exemples
      metasExemples.modifieValeurMeta(SaeIntegrationConstantes.META_HASH,
            "d145ea8e0ca28b8c97deb0c2a550f0a969a322a3");

      metasExemples.modifieValeurMeta(
            SaeIntegrationConstantes.META_CODE_ORG_PROPRIETAIRE, "UR750");
      formCapture.getMetadonnees().addAll(metasExemples);
      formCapture.getMetadonnees().add("Denomination",
            getDenomination(numeroTest));

      // formulaire de consultation

      ConsultationFormulaire formConsult = formulaire.getConsultation();
      if ("a".equals(numeroTest.substring(4, 5))) {
         formConsult.setModeConsult(ModeConsultationEnum.AncienServiceSansMtom);
      } else {
         formConsult
               .setModeConsult(ModeConsultationEnum.NouveauServiceAvecMtom);
      }

      CodeMetadonneeList codesMetas = formConsult.getCodeMetadonnees();
      // Code des métadonnées attendues après l'appel à la consult
      codesMetas.add("CodeOrganismeGestionnaire");
      codesMetas.add("CodeOrganismeProprietaire");
      codesMetas.add("CodeRND");
      codesMetas.add("ContratDeService");
      codesMetas.add("DateArchivage");
      codesMetas.add("DateCreation");
      codesMetas.add("DateReception");
      codesMetas.add("FormatFichier");
      codesMetas.add("Hash");
      codesMetas.add("NomFichier");
      codesMetas.add("TailleFichier");
      codesMetas.add("Titre");
      return formulaire;

   }

   public final void doPost(TestFormulaireDrCuCo formulaire, String numeroTest) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureUnitaire(formulaire);

         setViForm(numeroTest, formulaire, CONSULTATION);

      } else if ("2".equals(etape)) {

         etape2consultation(formulaire, numeroTest);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }
   }

   private void etape1captureUnitaire(TestFormulaireDrCuCo formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire
            .getCaptureUnitaire();

      // Vide le résultat du test précédent de l'étape 2
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      formConsult.getResultats().clear();
      formConsult.setIdArchivage(null);

      // Vide le dernier id d'archivage et le dernier sha1
      formulaire.setDernierIdArchivage(null);
      formulaire.setDernierSha1(null);

      // Lance le test
      CaptureUnitaireResultat consultResult = testCommons
            .getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1,
                  formulaire.getViFormulaire());

      // Si le test est en succès ...
      if (formCaptureEtp1.getResultats().getStatus().equals(
            TestStatusEnum.Succes)) {

         // On mémorise l'identifiant d'archivage et le sha-1
         formulaire.setDernierIdArchivage(consultResult.getIdArchivage());
         formulaire.setDernierSha1(consultResult.getSha1());

         // On affecte l'identifiant d'archivage à l'étape 2 (consultation)
         formConsult.setIdArchivage(consultResult.getIdArchivage());

      }

   }

   private void etape2consultation(TestFormulaireDrCuCo formulaire,
         String numeroTest) {

      // Initialise
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      ResultatTest resultatTestConsult = formConsult.getResultats();

      // Le SHA-1 attendu
      String sha1attendu = null;
      String idArchivageDemande = formConsult.getIdArchivage(); // NOPMD
      String dernierIdArchivageCapture = formulaire.getDernierIdArchivage(); // NOPMD
      String dernierSha1capture = formulaire.getDernierSha1(); // NOPMD

      if ((idArchivageDemande.equals(dernierIdArchivageCapture))
            && (StringUtils.isNotBlank(dernierSha1capture))) {
         sha1attendu = formulaire.getDernierSha1();
      }

      // Valeurs des métadonnées attendus après l'appel à la consult
      List<MetadonneeValeur> metaAttendues = new ArrayList<MetadonneeValeur>();
      metaAttendues.add(new MetadonneeValeur("CodeOrganismeGestionnaire",
            "CER69"));
      metaAttendues.add(new MetadonneeValeur("CodeOrganismeProprietaire",
            "UR750"));

      metaAttendues.add(new MetadonneeValeur("DateCreation", "2011-09-01"));
      metaAttendues
            .add(new MetadonneeValeur("DateReception", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("FormatFichier", "fmt/354"));
      metaAttendues.add(new MetadonneeValeur("NomFichier",
            "ADELPF_710_PSNV211157BPCA1L0000.pdf"));

      if ("1111".equals(numeroTest.substring(0, 4))) {
         metaAttendues.add(new MetadonneeValeur("ContratDeService",
               "INT_CS_ATT_AEPL"));
         metaAttendues.add(new MetadonneeValeur("CodeRND", "2.3.1.1.13"));
      } else if ("1117".equals(numeroTest.substring(0, 4))) {
         metaAttendues.add(new MetadonneeValeur("ContratDeService",
               "INT_CS_UNE_META"));
         metaAttendues.add(new MetadonneeValeur("CodeRND", "2.3.1.1.13"));
      } else if ("1123".equals(numeroTest.substring(0, 4))) {
         metaAttendues.add(new MetadonneeValeur("ContratDeService",
               "INT_CS_PLUSIEURS_META"));
         metaAttendues.add(new MetadonneeValeur("CodeRND", "2.3.1.1.12"));

      } else if ("1131".equals(numeroTest.substring(0, 4))) {
         metaAttendues.add(new MetadonneeValeur("ContratDeService",
               "INT_CS_PRMD_DYNA_CODERND"));
         metaAttendues.add(new MetadonneeValeur("CodeRND", "2.3.1.1.12"));

      } else if ("1156".equals(numeroTest.substring(0, 4))) {
         metaAttendues.add(new MetadonneeValeur("ContratDeService",
               "INT_CS_PRMD_DYNA_MULTI"));
         metaAttendues.add(new MetadonneeValeur("CodeRND", "2.3.1.1.3"));

      }
      metaAttendues.add(new MetadonneeValeur("Titre",
            "Attestation de vigilance"));

      // Lance le test
      if("1156".equals(numeroTest.substring(0, 4))){
         testCommons.getConsultationTestService()
         .appelWsOpConsultationSoapFault(formulaire.getUrlServiceWeb(), formConsult, formulaire.getViFormulaire());
      }else{
      testCommons.getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(), formConsult, sha1attendu,
                  formulaire.getConsultation().getCodeMetadonnees(),
                  metaAttendues);
      }

      // Si le test n'est pas en échec, alors il est OK (tout peut être vérifié)
      if (!TestStatusEnum.Echec.equals(resultatTestConsult.getStatus())) {
         resultatTestConsult.setStatus(TestStatusEnum.Succes);
      }

   }

}
