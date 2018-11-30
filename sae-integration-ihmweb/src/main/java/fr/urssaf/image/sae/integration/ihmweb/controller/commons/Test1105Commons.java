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
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test1105Formulaire;
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
 * Méthodes communes pour les tests 1105a, 1105b
 */
@Component
public class Test1105Commons {

   @Autowired
   private TestsControllerCommons testCommons;

   private String getDenomination(String numeroTest) {
      if ("1105a".equals(numeroTest)) {
         return "1105-Droits-Conformite-Consultation-ATT-VIGI";
      } else if ("1105b".equals(numeroTest)) {
         return "T1105-Droits-Conformite-Consultation-MTOM-ATT-VIGI";
      }  else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   private ModeArchivageUnitaireEnum getModeArchivage(String numeroTest) {
      if ("1105a".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitaire;
      } else if ("1105b".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJContenuAvecMtom;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }
   

   public final Test1105Formulaire getFormulairePourGet(String numeroTest) {


      Test1105Formulaire formulaire = new Test1105Formulaire();

      // Valeurs initiales du formulaire pour la capture unitaire
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);
      // Un exemple d'URL ECDE de fichier à capturer
      // (qui correspond à un document réellement existant sur l'ECDE
      // d'intégration)
      formCapture
            .setUrlEcde(testCommons.getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Droit-1105-Droits-Consultation-ATT-VIGI/documents/doc1.PDF"));
      // Des métadonnées exemples
      MetadonneeValeurList metasExemples = ReferentielMetadonneesService
            .getMetadonneesExemplePourCapture();
      metasExemples.modifieValeurMeta(SaeIntegrationConstantes.META_HASH,
            "a2f93f1f121ebba0faef2c0596f2f126eacae77b ");
      metasExemples.modifieValeurMeta(SaeIntegrationConstantes.META_CODE_ORG_PROPRIETAIRE,
      "UR750");
      formCapture.getMetadonnees().addAll(metasExemples);
      formCapture.getMetadonnees().add("Denomination",
            getDenomination(numeroTest));
      formCapture.setModeCapture(getModeArchivage(numeroTest));
      formCapture.setNomFichier("doc1.PDF");
      
      //formulaire de consultation
      
      ConsultationFormulaire formConsult = formulaire.getConsultation();

      formConsult.setModeConsult(ModeConsultationEnum.NouveauServiceAvecMtom);

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
      
      
      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("INT_CS_ATT_VIGI");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIdCertif("2");
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add("INT_PAGM_ATT_VIGI_ARCH_UNIT");
      
      return formulaire;

   }

   public final void doPost(Test1105Formulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureUnitaire(formulaire);
         formulaire.getViFormulaire();
         PagmList pagmList = new PagmList();
         ViFormulaire viForm = formulaire.getViFormulaire();
         viForm.setPagms(pagmList);
         pagmList.add("INT_PAGM_ATT_VIGI_CONSULT");

      } else if ("2".equals(etape)) {

         etape2consultation(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }
   }

   private void etape1captureUnitaire(Test1105Formulaire formulaire) {

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
                  formulaire.getUrlServiceWeb(), formCaptureEtp1, formulaire.getViFormulaire());

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

   private void etape2consultation(Test1105Formulaire formulaire) {

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
      metaAttendues.add(new MetadonneeValeur("CodeOrganismeGestionnaire","CER69"));
      metaAttendues.add(new MetadonneeValeur("CodeOrganismeProprietaire","UR750"));
      metaAttendues.add(new MetadonneeValeur("CodeRND", "2.3.1.1.12"));
      metaAttendues.add(new MetadonneeValeur("ContratDeService","INT_CS_ATT_VIGI"));
      metaAttendues.add(new MetadonneeValeur("DateCreation", "2011-09-01"));
      metaAttendues.add(new MetadonneeValeur("DateReception",StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("FormatFichier", "fmt/354"));
      metaAttendues.add(new MetadonneeValeur("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b"));
      metaAttendues.add(new MetadonneeValeur("NomFichier", "doc1.PDF"));
      metaAttendues.add(new MetadonneeValeur("TailleFichier", "56587"));
      metaAttendues.add(new MetadonneeValeur("Titre", "Attestation de vigilance"));

      // Lance le test
      testCommons.getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(), formConsult, sha1attendu,
                  formulaire.getConsultation().getCodeMetadonnees(), metaAttendues);

      // Si le test n'est pas en échec, alors il est OK (tout peut être vérifié)
      if (!TestStatusEnum.Echec.equals(resultatTestConsult.getStatus())) {
         resultatTestConsult.setStatus(TestStatusEnum.AControler);
      }

   }


}
