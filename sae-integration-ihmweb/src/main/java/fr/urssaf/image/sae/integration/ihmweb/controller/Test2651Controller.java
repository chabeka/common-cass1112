package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsCaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;


/**
 * 2650-IdGed-ArchivageUnitaire-KO-IdGed-Existant
 */
@Controller
@RequestMapping(value = "test2651")
public class Test2651Controller extends AbstractTestWsController<TestWsCaptureUnitaireFormulaire> {


   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2651";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsCaptureUnitaireFormulaire getFormulairePourGet() {
      
      TestWsCaptureUnitaireFormulaire formulaire = new TestWsCaptureUnitaireFormulaire();
      
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      formCapture.setUrlEcde("ecde://cnp69devecde.cer69.recouv/SAE_INTEGRATION/20110822/IdGed-2651-IdGed-ArchivageUnitaire-KO-IdGed-MauvaisFormat/documents/doc1.PDF");
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "AC750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-01");
      metadonnees.add("Denomination", "Test 2651-IdGed-ArchivageUnitaire-KO-IdGed-MauvaisFormat");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");      
      metadonnees.add("IdGed","Mauvais format");      
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");

      formCapture.setMetadonnees(metadonnees);
      
      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsCaptureUnitaireFormulaire formulaire) {
      
      captureUnitaire(
            formulaire.getUrlServiceWeb(),
            formulaire.getCaptureUnitaire());
      
   }
   

   private void captureUnitaire(
         String urlServiceWeb,
         CaptureUnitaireFormulaire formulaire) {
      
      // Appel de la méthode de test
       getCaptureUnitaireTestService().appelWsOpCaptureUnitaireSoapFault(
            urlServiceWeb, formulaire, ViStyle.VI_OK,
            "sae_ErreurInterneCapture", null);
      
//      getCaptureUnitaireTestService().appelWsOpCaptureUnitaireSoapFault(
//            urlServiceWeb, 
//            formulaire,
//            ViStyle.VI_OK,
//            "sae_ErreurInterneCapture",
//            "Une erreur interne à l'application est survenue lors de la capture."});
            //new String[] {"f8325fc9-b12a-44ed-8072-8344d2461234"});
      
  }
   
 
}
