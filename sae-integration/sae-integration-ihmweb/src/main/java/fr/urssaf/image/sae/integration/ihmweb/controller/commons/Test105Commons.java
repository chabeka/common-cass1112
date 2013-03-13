package fr.urssaf.image.sae.integration.ihmweb.controller.commons;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test105Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeArchivageUnitaireEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeConsultationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

/**
 * Méthodes communes pour les tests 105a, 105b, 105c, 105d
 */
@Component
public class Test105Commons {

   @Autowired
   private TestsControllerCommons testCommons;

   private String getDenomination(String numeroTest) {
      if ("105a".equals(numeroTest)) {
         return "Test 105-CaptureUnitaire-OK-HashMajMin";
      } else if ("105b".equals(numeroTest)) {
         return "Test 105-CaptureUnitaire-OK-HashMajMin-PJ-URL";
      } else if ("105c".equals(numeroTest)) {
         return "Test 105-CaptureUnitaire-OK-HashMajMin-PJ-sans-MTOM";
      } else if ("105d".equals(numeroTest)) {
         return "Test 105-CaptureUnitaire-OK-HashMajMin-PJ-avec-MTOM";
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   private ModeArchivageUnitaireEnum getModeArchivage(String numeroTest) {
      if ("105a".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitaire;
      } else if ("105b".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJUrlEcde;
      } else if ("105c".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJContenuSansMtom;
      } else if ("105d".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJContenuAvecMtom;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }
   
   private String nomFichier ="doc1.PDF";
   private String getnomFichier(String numeroTest) {
      if ("105a".equals(numeroTest)) {
         return StringUtils.EMPTY;
      } else if ("105b".equals(numeroTest)) {
         return StringUtils.EMPTY;
      } else if ("105c".equals(numeroTest)) {
         return nomFichier;
      } else if ("105d".equals(numeroTest)) {
         return nomFichier;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }
   public final Test105Formulaire getFormulairePourGet(String numeroTest) {

      Test105Formulaire formulaire = new Test105Formulaire();

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape de capture unitaire
      // -----------------------------------------------------------------------------

      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();

      // L'URL ECDE
      formCapture
            .setUrlEcde(testCommons.getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/CaptureUnitaire-105-CaptureUnitaire-OK-HashMajMin/documents/doc1.pdf"));
      
      // Le nom du fichier
      formCapture.setNomFichier(getnomFichier(numeroTest));

      // Le mode d'utilisation de la capture
      formCapture.setModeCapture(getModeArchivage(numeroTest));

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "AC750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-05");
      metadonnees.add("Denomination", getDenomination(numeroTest));
      metadonnees.add("FormatFichier", "fmt/354");
      // Hash avec un A majuscule au début
      metadonnees.add("Hash", "A2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape de consultation
      // -----------------------------------------------------------------------------

      ConsultationFormulaire formConsult = formulaire.getConsultation();

      formConsult.setModeConsult(ModeConsultationEnum.NouveauServiceAvecMtom);

      CodeMetadonneeList codesMetas = formConsult.getCodeMetadonnees();
      codesMetas.add("Hash");

      // Renvoie le formulaire
      return formulaire;

   }

   public final void doPost(Test105Formulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureUnitaire(formulaire);

      } else if ("2".equals(etape)) {

         etape2consultation(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureUnitaire(Test105Formulaire formulaire) {

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
      CaptureUnitaireResultat consultResult = testCommons.getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1);

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

   private void etape2consultation(Test105Formulaire formulaire) {

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

      // Code des métadonnées attendues après l'appel à la consult
      CodeMetadonneeList codesMetasAttendus = new CodeMetadonneeList();
      codesMetasAttendus.add("Hash");

      // Valeurs des métadonnées attendus après l'appel à la consult
      List<MetadonneeValeur> metaAttendues = new ArrayList<MetadonneeValeur>();
      metaAttendues.add(new MetadonneeValeur("Hash",
            "a2f93f1f121ebba0faef2c0596f2f126eacae77b"));

      // Lance le test
      testCommons.getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(), formConsult, sha1attendu,
                  codesMetasAttendus, metaAttendues);

      // Si le test n'est pas en échec, alors il est OK (tout peut être vérifié)
      if (!TestStatusEnum.Echec.equals(resultatTestConsult.getStatus())) {
         resultatTestConsult.setStatus(TestStatusEnum.Succes);
      }

   }

}
