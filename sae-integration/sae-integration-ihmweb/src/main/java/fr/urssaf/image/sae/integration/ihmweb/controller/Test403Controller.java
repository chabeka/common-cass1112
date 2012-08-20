package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test403Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;


/**
 * 403-Consultation-OK-TouteMetadonneeConsultable
 */
@Controller
@RequestMapping(value = "test403")
public class Test403Controller extends AbstractTestWsController<Test403Formulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "403";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test403Formulaire getFormulairePourGet() {
      
      Test403Formulaire formulaire = new Test403Formulaire();
      
      // -----------------------------------------------------------------------------
      // Etape 1 : Capture unitaire 
      // -----------------------------------------------------------------------------
      
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      
      // L'URL ECDE du fichier de test
      formCapture.setUrlEcde(
            getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/Consultation-403-Consultation-OK-TouteMetadonneeConsultable/documents/doc1.PDF"));
      
      // Les métadonnées      
      MetadonneeValeurList metadonnees = new MetadonneeValeurList(); 
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice","ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire","CER69");
      metadonnees.add("CodeOrganismeProprietaire","AC750");
      metadonnees.add("CodeRND","2.3.1.1.12");
      metadonnees.add("DateCreation","2011-09-01");
      metadonnees.add("Denomination","Test 403-Consultation-OK-TouteMetadonneeConsultable");
      metadonnees.add("FormatFichier","fmt/354");
      metadonnees.add("Hash","a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages","2");
      metadonnees.add("Titre","Attestation de vigilance");
      metadonnees.add("TypeHash","SHA-1");
      
      
      // -----------------------------------------------------------------------------
      // Etape 2 : Consultation 
      // -----------------------------------------------------------------------------
      
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      
      // Les codes des métadonnées souhaitées
      // Toutes les métadonnées consultables
      CodeMetadonneeList codesMetas = formConsult.getCodeMetadonnees();
      codesMetas.addAll(getRefMetas().listeMetadonneesConsultables());
      
           
      
      
      // -----------------------------------------------------------------------------
      // Renvoie le formulaire
      // -----------------------------------------------------------------------------
      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test403Formulaire formulaire) {
      
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
         Test403Formulaire formulaire) {
      
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
         Test403Formulaire formulaire) {
      
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
      // => Toutes les métadonnées consultables
      CodeMetadonneeList codesMetasAttendues = formConsult.getCodeMetadonnees();
            
      // Les valeurs des métadonnées attendues (uniquement pour les valeurs prédictibles)
      List<MetadonneeValeur> metaAttendues = new ArrayList<MetadonneeValeur>();
      metaAttendues.add(new MetadonneeValeur("ApplicationProductrice", "ADELAIDE"));
      metaAttendues.add(new MetadonneeValeur("ApplicationTraitement", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("CodeActivite", "3"));
      metaAttendues.add(new MetadonneeValeur("CodeCategorieV2", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("CodeFonction", "2"));
      metaAttendues.add(new MetadonneeValeur("CodeOrganismeGestionnaire", "CER69"));
      metaAttendues.add(new MetadonneeValeur("CodeOrganismeProprietaire", "AC750"));
      metaAttendues.add(new MetadonneeValeur("CodeRND", "2.3.1.1.12"));
      metaAttendues.add(new MetadonneeValeur("CodeSousCategorieV2", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("ContratDeService", "CS_ANCIEN_SYSTEME"));
      // metaAttendues.add(new MetadonneeValeur("DateArchivage", "")); // => non prédictible
      metaAttendues.add(new MetadonneeValeur("DateCreation", "2011-09-01"));
      // metaAttendues.add(new MetadonneeValeur("DateDebutConservation", "")); // => non prédictible
      // metaAttendues.add(new MetadonneeValeur("DateFinConservation", "")); // => non prédictible
      metaAttendues.add(new MetadonneeValeur("DateReception", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("Denomination", "Test 403-Consultation-OK-TouteMetadonneeConsultable"));
      metaAttendues.add(new MetadonneeValeur("DureeConservation", "1825"));
      metaAttendues.add(new MetadonneeValeur("FormatFichier", "fmt/354"));
      metaAttendues.add(new MetadonneeValeur("Gel", "false"));
      metaAttendues.add(new MetadonneeValeur("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b"));
      metaAttendues.add(new MetadonneeValeur("IdTraitementMasse", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("NbPages", "2"));
      metaAttendues.add(new MetadonneeValeur("NniEmployeur", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("NomFichier", "doc1.PDF"));
      metaAttendues.add(new MetadonneeValeur("NumeroCompteExterne", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("NumeroCompteInterne", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("NumeroIntControle", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("NumeroPersonne", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("NumeroRecours", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("NumeroStructure", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("Periode", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("PseudoSiret", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("Siren", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("Siret", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("SiteAcquisition", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("TailleFichier", "56587"));
      metaAttendues.add(new MetadonneeValeur("Titre", "Attestation de vigilance"));
      metaAttendues.add(new MetadonneeValeur("TracabilitePostArchivage", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("TracabilitrePreArchivage", StringUtils.EMPTY));
      metaAttendues.add(new MetadonneeValeur("TypeHash", "SHA-1"));
      metaAttendues.add(new MetadonneeValeur("VersionRND", getTestConfig().getVersionRND()));
      
      // Lance le test
      getConsultationTestService().appelWsOpConsultationReponseCorrecteAttendue(
            formulaire.getUrlServiceWeb(),
            formConsult, 
            sha1attendu,
            codesMetasAttendues,
            metaAttendues);
      
       // Au mieux, le résultat du test est "à contrôler", car il y a des
       // métadonnées à vérifier à la main
       if (!TestStatusEnum.Echec.equals(formConsult.getResultats().getStatus())) {
          formConsult.getResultats().setStatus(TestStatusEnum.AControler);
       }
      
   }
 
}
