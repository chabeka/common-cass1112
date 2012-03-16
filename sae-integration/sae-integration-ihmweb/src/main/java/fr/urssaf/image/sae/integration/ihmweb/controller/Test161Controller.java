package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsCaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeArchivageUnitaireEnum;
import fr.urssaf.image.sae.integration.ihmweb.utils.ViUtils;


/**
 * 161-CaptureUnitaire-KO-TailleZero
 */
@Controller
@RequestMapping(value = "test161")
public class Test161Controller extends AbstractTestWsController<TestWsCaptureUnitaireFormulaire> {


   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "161";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsCaptureUnitaireFormulaire getFormulairePourGet() {
      
      TestWsCaptureUnitaireFormulaire formulaire = new TestWsCaptureUnitaireFormulaire();
      
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      
      
      // URL ECDE et nom du fichier
      formCapture.setUrlEcde(getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureUnitaire-161-CaptureUnitaire-KO-TailleZero/documents/fichier_vide.txt"));
      formCapture.setNomFichier("fichier_vide.txt");
      
      
      // Métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice","ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire","CER69");
      metadonnees.add("CodeOrganismeProprietaire","AC750");
      metadonnees.add("CodeRND","2.3.1.1.12");
      metadonnees.add("DateCreation","2011-09-01");
      metadonnees.add("Denomination","Test 161-CaptureUnitaire-KO-TailleZero");
      metadonnees.add("FormatFichier","fmt/354");
      metadonnees.add("Hash","da39a3ee5e6b4b0d3255bfef95601890afd80709");
      metadonnees.add("NbPages","2");
      metadonnees.add("Titre","Attestation de vigilance");
      metadonnees.add("TypeHash","SHA-1");
      
      
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
      
      if (
            (ModeArchivageUnitaireEnum.archivageUnitairePJContenuAvecMtom.equals(formulaire.getModeCapture())) || 
            (ModeArchivageUnitaireEnum.archivageUnitairePJContenuSansMtom.equals(formulaire.getModeCapture()))) {
         
         getCaptureUnitaireTestService().appelWsOpCaptureUnitaireSoapFault(
               urlServiceWeb, 
               formulaire,
               ViUtils.FIC_VI_OK,
               "sae_CaptureFichierVide_2",
               null);
         
      } else {
         
         getCaptureUnitaireTestService().appelWsOpCaptureUnitaireSoapFault(
               urlServiceWeb, 
               formulaire,
               ViUtils.FIC_VI_OK,
               "sae_CaptureFichierVide",
               new String[] {"fichier_vide.txt"});
         
      }
      
      
      
      
   }
   
 
}
