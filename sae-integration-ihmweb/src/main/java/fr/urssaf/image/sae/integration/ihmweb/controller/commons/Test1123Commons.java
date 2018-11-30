package fr.urssaf.image.sae.integration.ihmweb.controller.commons;

import java.util.ArrayList;
import java.util.List;

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
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeConsultationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

/**
 * Méthodes communes pour les tests 1123a et 1123b
 */
@Component
public class Test1123Commons {

   
   private static final String NUMERO_TEST_A = "1123a";
   private static final String NUMERO_TEST_B = "1123b";
   
   private static final String NOM_TEST_A = "Test 1123-Droits-Conformite-Consultation-PLUSIEURS-META";
   private static final String NOM_TEST_B = "Test 1123-Droits-Conformite-Consultation-MTOM-PLUSIEURS-META";
   
   private static final String VI_ISSUER = "INT_CS_PLUSIEURS_META";
   private static final String VI_PAGM_CAPTURE = "INT_PAGM_PLUSIEURS_META_ARCH_UNIT";
   private static final String VI_PAGM_CONSULTATION = "INT_PAGM_PLUSIEURS_META_CONSULT";
   
   private static final String FICHIER_CAPTURE_UNITAIRE = "SAE_INTEGRATION/20110822/Droit-1123-Droits-Conformite-Consultation-PLUSIEURS-META/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf";
   
   private static final String CODE_RND = "2.3.1.1.12";
   
   @Autowired
   private TestsControllerCommons testCommons;
   
   
   private String getDenomination(String numeroTest) {
      if (NUMERO_TEST_A.equals(numeroTest)) {
         return NOM_TEST_A;
      } else if (NUMERO_TEST_B.equals(numeroTest)) {
         return NOM_TEST_B;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }
   
   private ModeConsultationEnum getModeConsultation(String numeroTest) {
      if (NUMERO_TEST_A.equals(numeroTest)) {
         return ModeConsultationEnum.AncienServiceSansMtom;
      } else if (NUMERO_TEST_B.equals(numeroTest)) {
         return ModeConsultationEnum.NouveauServiceAvecMtom;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }
   
   public final TestFormulaireDrCuCo getFormulairePourGet(String numeroTest) {

      // Création de l'objet formulaire
      TestFormulaireDrCuCo formulaire = new TestFormulaireDrCuCo();
      
      // VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setIssuer(VI_ISSUER);
      definitViPagm(viForm, VI_PAGM_CAPTURE);
      
      // Valeurs initiales du formulaire pour la capture unitaire
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);
      // URL ECDE
      formCapture.setUrlEcde(testCommons.getEcdeService().construitUrlEcde(FICHIER_CAPTURE_UNITAIRE));
      // Métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "UR750");
      metadonnees.add("CodeRND", CODE_RND);
      metadonnees.add("DateCreation", "2007-04-01");
      metadonnees.add("Denomination", getDenomination(numeroTest));
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "d145ea8e0ca28b8c97deb0c2a550f0a969a322a3");
      metadonnees.add("NbPages", "2");
      metadonnees.add("NumeroRecours", "11");
      metadonnees.add("Siren", "3090000001");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");
      
      // Formulaire de consultation
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      formConsult.setModeConsult(getModeConsultation(numeroTest));
      CodeMetadonneeList codesMetas = formConsult.getCodeMetadonnees();
      codesMetas.add("CodeRND");
      codesMetas.add("ContratDeService");
      codesMetas.add("Denomination");
      return formulaire;

   }
   
   
   public final void doPost(TestFormulaireDrCuCo formulaire, String numeroTest) {
      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {
         etape1captureUnitaire(formulaire);
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
      
      // Modifie le VI pour passer au PAGM de la consultation
      definitViPagm(formulaire.getViFormulaire(), VI_PAGM_CONSULTATION);

   }

   private void etape2consultation(TestFormulaireDrCuCo formulaire,
         String numeroTest) {

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
      metaAttendues.add(new MetadonneeValeur("CodeRND", CODE_RND));
      metaAttendues.add(new MetadonneeValeur("ContratDeService", VI_ISSUER));
      metaAttendues.add(new MetadonneeValeur("Denomination", getDenomination(numeroTest)));

      // Lance le test
      testCommons.getConsultationTestService()
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
