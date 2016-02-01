package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationAffichableFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test2001Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;


/**
 * 2053-ConsultationAffichable-KO-no-page-negatif
 */
@Controller
@RequestMapping(value = "test2053")
public class Test2053Controller extends AbstractTestWsController<Test2001Formulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2053";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test2001Formulaire getFormulairePourGet() {
      
      Test2001Formulaire formulaire = new Test2001Formulaire();
      
      // Initialisation du formulaire de l'étape de capture unitaire
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      
      // L'URL ECDE du fichier de test
      formCapture.setUrlEcde(
            getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/ConsulationAffichable-2000-OK/documents/doc1.tif"));
      
      // Les métadonnées      
      MetadonneeValeurList metadonnees = new MetadonneeValeurList(); 
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice","ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire","CER69");
      metadonnees.add("CodeOrganismeProprietaire","AC750");
      metadonnees.add("CodeRND","2.3.1.1.12");
      metadonnees.add("DateCreation","2011-09-23");
      metadonnees.add("DateDebutConservation","2011-09-01");
      metadonnees.add("Denomination","Test 2053-ConsultationAffichable-KO-nb-premiere-page-negatif");
      metadonnees.add("FormatFichier","fmt/353");
      metadonnees.add("Hash","76734a4ba9c1dca0ced7960bcd7cc0055c16cefb");
      metadonnees.add("NbPages","43");
      metadonnees.add("Titre","Attestation de vigilance");
      metadonnees.add("TypeHash","SHA-1");
      
      // Renvoie le formulaire
      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test2001Formulaire formulaire) {
      
      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {
         
         etape1captureUnitaire(formulaire);
         
         
      } else if ("2".equals(etape)) {
         
         etape2consultationAffichable(formulaire);
         
      } else{
         
         throw new IntegrationRuntimeException("L'étape " + etape + " est inconnue !");
         
      }
      
   }
   
   
   private void etape1captureUnitaire(
         Test2001Formulaire formulaire) {
      
      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptureUnitaire();

      // Vide le résultat du test précédent de l'étape 2 
      ConsultationAffichableFormulaire formConsultEtp2 = formulaire.getConsultationAffichable();
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
         formConsultEtp2.setNumeroPage(-1);
         //formConsultEtp2.setNombrePages(0);
      }
      

   }
   
   
   private void etape2consultationAffichable(
         Test2001Formulaire formulaire) {
      
      // Initialise
      ConsultationAffichableFormulaire formConsult = formulaire.getConsultationAffichable();

//      getConsultationAffichableTestService().appelWsOpConsultationSoapFault(
//            formulaire.getUrlServiceWeb(),
//            formConsult,
//            ViStyle.VI_OK,
//            "sae_ConsultationAffichableParametrageIncorrect",
//            null);
//      
      
      getConsultationAffichableTestService().appelWsOpConsultationSoapFault(
            formulaire.getUrlServiceWeb(),
            formConsult,
            ViStyle.VI_OK,
            "sae_ConsultationAffichableParametrageIncorrect_1",
            null);
   }
 
}
