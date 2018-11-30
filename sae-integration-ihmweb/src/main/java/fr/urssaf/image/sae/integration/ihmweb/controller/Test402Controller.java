package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.EnvoiSoapFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test402Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;


/**
 * 402-Consultation-OK-version-120150
 */
@Controller
@RequestMapping(value = "test402")
public class Test402Controller extends AbstractTestWsController<Test402Formulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "402";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test402Formulaire getFormulairePourGet() {
      
      Test402Formulaire formulaire = new Test402Formulaire();
      
      // Initialisation du formulaire de l'étape de capture unitaire
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      
      // L'URL ECDE du fichier de test
      formCapture.setUrlEcde(
            getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/Consultation-402-Consulation-OK-version-120150/documents/doc1.PDF"));
      
      // Les métadonnées      
      MetadonneeValeurList metadonnees = new MetadonneeValeurList(); 
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice","ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire","CER69");
      metadonnees.add("CodeOrganismeProprietaire","AC750");
      metadonnees.add("CodeRND","2.3.1.1.12");
      metadonnees.add("DateCreation","2011-09-01");
      metadonnees.add("Denomination","Test 402-Consulation-OK-version-120150");
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
   protected final void doPost(Test402Formulaire formulaire) {
      
      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {
         
         etape1captureUnitaire(formulaire);
         
      } else if ("2".equals(etape)) {
         
         // etape2consultation(formulaire);
         
      } else{
         
         throw new IntegrationRuntimeException("L'étape " + etape + " est inconnue !");
         
      }
      
   }
   
   
   private void etape1captureUnitaire(
         Test402Formulaire formulaire) {
      
      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptureUnitaire();

      // Vide le résultat du test précédent de l'étape 2 
      EnvoiSoapFormulaire formEnvoiSoapEtp2 = formulaire.getEnvoiSoap();
      formEnvoiSoapEtp2.setSoapRequest(StringUtils.EMPTY);
      
      // Lance le test
      CaptureUnitaireResultat consultResult = 
         getCaptureUnitaireTestService().appelWsOpCaptureUnitaireReponseAttendue(
               formulaire.getUrlServiceWeb(),
               formCaptureEtp1);
      
      // Si le test est en succès ...
      if (formCaptureEtp1.getResultats().getStatus().equals(TestStatusEnum.Succes)) {
         
         // On prépare le message SOAP de l'étape 2
         String idArchivage = consultResult.getIdArchivage();
         String soapRequest = construitMessageSoapConsult(idArchivage);
         formEnvoiSoapEtp2.setSoapRequest(soapRequest);
         
      }
      

   }
   
   
   private String construitMessageSoapConsult(String idArchivage) {
      
      ClassPathResource resource = new ClassPathResource("SoapMessages/402_Consultation.xml");
      
      String soapRequest = StringUtils.EMPTY;
      try {
         soapRequest = FileUtils.readFileToString(resource.getFile(), CharEncoding.UTF_8);
      } catch (IOException e) {
         throw new IntegrationRuntimeException(e);
      }
      
      soapRequest = StringUtils.replace(
            soapRequest, "@@ID_ARCHIVE@@", idArchivage);
      
      return soapRequest;
      
   }
   
   
 
}
