package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test404Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeConsultationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

/**
 * 404-Consultation-OK-TypeMime-MTOM-pdf
 */
@Controller
@RequestMapping(value = "test404")
public class Test404Controller extends
      AbstractTestWsController<Test404Formulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "404";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test404Formulaire getFormulairePourGet() {

      Test404Formulaire formulaire = new Test404Formulaire();

      // -----------------------------------------------------------------------------
      // Etape 1 : Capture unitaire
      // -----------------------------------------------------------------------------

      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();

      // L'URL ECDE du fichier de test
      formCapture
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Consultation-404-Consultation-OK-TypeMime-MTOM-pdf/documents/doc1.PDF"));

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "AC750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-01");
      metadonnees.add("Denomination",
            "Test 404-Consultation-OK-TypeMime-MTOM-pdf");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");

      // -----------------------------------------------------------------------------
      // Etape 2 : Consultation
      // -----------------------------------------------------------------------------

      ConsultationFormulaire formConsult = formulaire.getConsultation();

      // Utiliser le service consultationMTOM
      formConsult.setModeConsult(ModeConsultationEnum.NouveauServiceAvecMtom);
      
      // Les codes des métadonnées souhaitées
      CodeMetadonneeList codesMetas = formConsult.getCodeMetadonnees();
      codesMetas.add("Denomination");

      // -----------------------------------------------------------------------------
      // Renvoie le formulaire
      // -----------------------------------------------------------------------------
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test404Formulaire formulaire) {

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

   private void etape1captureUnitaire(Test404Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire
            .getCaptureUnitaire();

      // Vide le résultat du test précédent de l'étape 2
      ConsultationFormulaire formConsultEtp2 = formulaire.getConsultation();
      formConsultEtp2.getResultats().clear();
      formConsultEtp2.setIdArchivage(null);

      // Vide le dernier id d'archivage et le dernier sha1
      formulaire.setDernierIdArchivage(null);
      formulaire.setDernierSha1(null);

      // Lance le test
      CaptureUnitaireResultat consultResult = getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1);

      // Si le test est en succès ...
      if (formCaptureEtp1.getResultats().getStatus().equals(
            TestStatusEnum.Succes)) {

         // On mémorise l'identifiant d'archivage et le sha-1
         formulaire.setDernierIdArchivage(consultResult.getIdArchivage());
         formulaire.setDernierSha1(consultResult.getSha1());

         // On affecte l'identifiant d'archivage à l'étape 2 (consultation)
         formConsultEtp2.setIdArchivage(consultResult.getIdArchivage());

      }

   }

   private void etape2consultation(Test404Formulaire formulaire) {

      // Initialise
      ConsultationFormulaire formConsult = formulaire.getConsultation();

      // Le SHA-1 attendu
      String sha1attendu = null;
      String idArchivageDemande = formConsult.getIdArchivage(); // NOPMD
      String dernierIdArchivageCapture = formulaire.getDernierIdArchivage(); // NOPMD
      String dernierSha1capture = formulaire.getDernierSha1(); // NOPMD

      if ((idArchivageDemande.equals(dernierIdArchivageCapture))
            && (StringUtils.isNotBlank(dernierSha1capture))) {
         sha1attendu = formulaire.getDernierSha1();
      }

      // La liste des codes des métadonnées attendues
      CodeMetadonneeList codesMetasAttendues = new CodeMetadonneeList();
      codesMetasAttendues.add("Denomination");

      // Les valeurs des métadonnées attendues
      List<MetadonneeValeur> metaAttendues = new ArrayList<MetadonneeValeur>();
      metaAttendues.add(new MetadonneeValeur("Denomination",
            "Test 404-Consultation-OK-TypeMime-MTOM-pdf"));

      // Lance le test
      getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(), formConsult, sha1attendu,
                  codesMetasAttendues, metaAttendues);

      // Au mieux, le résultat du test est "à contrôler", car le type MIME
      // doit être contrôlé manuellement pour l'instant
      if (!TestStatusEnum.Echec.equals(formConsult.getResultats().getStatus())) {
         formConsult.getResultats().setStatus(TestStatusEnum.AControler);
      }

   }

}
