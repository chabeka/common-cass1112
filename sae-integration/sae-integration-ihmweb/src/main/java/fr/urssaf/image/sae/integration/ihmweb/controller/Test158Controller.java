package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsCaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.utils.ViUtils;


/**
 * Test 158<br>
 * <br>
 * On vérifie que la bonne erreur est renvoyée lorsque la liste des métadonnées 
 * contient une métadonnée qui n'est pas spécifiable lors de l'archivage
 */
@Controller
@RequestMapping(value = "test158")
public class Test158Controller extends AbstractTestWsController<TestWsCaptureUnitaireFormulaire> {


   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "158";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsCaptureUnitaireFormulaire getFormulairePourGet() {
      
      TestWsCaptureUnitaireFormulaire formulaire = new TestWsCaptureUnitaireFormulaire();
      
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      
      
      // URL ECDE
      formCapture.setUrlEcde("ecde://ecde.cer69.recouv/SAE_INTEGRATION/20110822/CaptureUnitaire-158-CaptureUnitaire-KO-MetadonneeNonArchivable/documents/doc1.PDF");
      
      
      // Métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("Titre","Attestation de vigilance");
      metadonnees.add("DateCreation","2011-09-01");
      metadonnees.add("ApplicationProductrice","ADELAIDE");
      metadonnees.add("CodeOrganismeProprietaire","AC750");
      metadonnees.add("CodeOrganismeGestionnaire","CER69");
      metadonnees.add("CodeRND","2.3.1.1.12");
      metadonnees.add("Hash","a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("TypeHash","SHA-1");
      metadonnees.add("NbPages","2");
      metadonnees.add("FormatFichier","fmt/354");
      metadonnees.add("Denomination","Test 158-CaptureUnitaire-KO-MetadonneeNonArchivable");
      metadonnees.add("DureeConservation","1000"); // la métadonnée DureeConservation n'est pas spécifiable à l'archivage
      metadonnees.add("CodeFonction","2"); // la métadonnée CodeFonction n'est pas spécifiable à l'archivage 
      
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
            urlServiceWeb, 
            formulaire,
            ViUtils.FIC_VI_OK,
            "sae_CaptureMetadonneesInterdites",
            new String[] {"CodeFonction, DureeConservation"});
      
   }
   
 
}
