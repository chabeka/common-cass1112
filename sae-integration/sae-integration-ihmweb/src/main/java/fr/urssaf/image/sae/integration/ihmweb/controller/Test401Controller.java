package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test401Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.ConsultationResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectExtractor;


/**
 * 401-Consultation-OK-RetourContenu
 */
@Controller
@RequestMapping(value = "test401")
public class Test401Controller extends AbstractTestWsController<Test401Formulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "401";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test401Formulaire getFormulairePourGet() {
      
      Test401Formulaire formulaire = new Test401Formulaire();
      
      // Initialisation du formulaire de l'étape de capture unitaire
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      
      // L'URL ECDE du fichier de test
      formCapture.setUrlEcde(
            getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/Consultation-401-Consultation-OK-RetourContenu/documents/doc1.PDF"));
      
      // Les métadonnées      
      MetadonneeValeurList metadonnees = new MetadonneeValeurList(); 
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice","ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire","CER69");
      metadonnees.add("CodeOrganismeProprietaire","AC750");
      metadonnees.add("CodeRND","2.3.1.1.12");
      metadonnees.add("DateCreation","2011-09-01");
      metadonnees.add("Denomination","Test 401-Consultation-OK-RetourContenu");
      metadonnees.add("FormatFichier","fmt/354");
      metadonnees.add("Hash","a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages","2");
      metadonnees.add("Titre","Attestation de vigilance");
      metadonnees.add("TypeHash","SHA-1");
      
      // Renvoie le formulaire
      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test401Formulaire formulaire) {
      
      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {
         
         etape1captureUnitaire(formulaire);
         
      } else if ("2".equals(etape)) {
         
         etape2consultation(formulaire);
         
      } else{
         
         throw new IntegrationRuntimeException("L'étape " + etape + " est inconnue !");
         
      }
      
   }
   
   
   private void etape1captureUnitaire(
         Test401Formulaire formulaire) {
      
      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptureUnitaire();

      // Vide le résultat du test précédent de l'étape 2 
      ConsultationFormulaire formConsultEtp2 = formulaire.getConsultation();
      formConsultEtp2.getResultats().clear();
      formConsultEtp2.setIdArchivage(null);
      
      // Vide le dernier id d'archivage et le dernier sha1
      formulaire.setDernierIdArchivage(null);
      formulaire.setDernierSha1(null);
      
      // Lance le test
      CaptureUnitaireResultat consultResult = 
         getCaptureUnitaireTestService().appelWsOpCaptureUnitaireReponseAttendue(
               formulaire.getUrlServiceWeb(),
               formCaptureEtp1);
      
      // Si le test est en succès ...
      if (formCaptureEtp1.getResultats().getStatus().equals(TestStatusEnum.Succes)) {
         
         // On mémorise l'identifiant d'archivage et le sha-1
         formulaire.setDernierIdArchivage(consultResult.getIdArchivage());
         formulaire.setDernierSha1(consultResult.getSha1());
         
         // On affecte l'identifiant d'archivage à l'étape 2 (consultation)
         formConsultEtp2.setIdArchivage(consultResult.getIdArchivage());
         
      }
      

   }
   
   
   private void etape2consultation(
         Test401Formulaire formulaire) {
      
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
      
      // Lance le test
      ConsultationResultat response = getConsultationTestService().appelWsOpConsultationReponseCorrecteAttendue(
            formulaire.getUrlServiceWeb(),
            formConsult, 
            sha1attendu);
      
      // Vérifie les métadonnées attendues
      if (response!=null) { // <= response est null si on a obtenu une SoapFault
      
         MetadonneeValeurList metas = SaeServiceObjectExtractor.extraitMetadonnees(response.getMetadonnees());
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), 
               metas, 
               "CodeOrganismeGestionnaire", 
               "CER69");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), 
               metas, 
               "CodeOrganismeProprietaire", 
               "AC750");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), 
               metas, 
               "CodeRND", 
               "2.3.1.1.12");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), 
               metas, 
               "ContratDeService", 
               "ATT_PROD_001");
         // DateArchivage => à vérifier manuellement
//       getTestsMetasService().verifiePresenceEtValeurAvecLog(
//             formConsult.getResultats(), 
//             metas, 
//             "DateArchivage", 
//             );
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), 
               metas, 
               "DateCreation", 
               "2011-09-01");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), 
               metas, 
               "DateReception", 
               StringUtils.EMPTY);
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), 
               metas, 
               "FormatFichier", 
               "fmt/354");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), 
               metas, 
               "Hash", 
               "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), 
               metas, 
               "NomFichier", 
               "doc1.PDF");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), 
               metas, 
               "TailleFichier", 
               "56587");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), 
               metas, 
               "Titre", 
               "Attestation de vigilance");
         
      }
      
   }
 
}
