package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsCaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.utils.ViUtils;


/**
 * Test 153<br>
 * <br>
 * On vérifie que la bonne erreur est renvoyée lorsqu'une métadonnée 
 * obligatoire est omise de la liste des métadonnées.
 */
@Controller
@RequestMapping(value = "test153")
public class Test153Controller extends AbstractTestWsController<TestWsCaptureUnitaireFormulaire> {


   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "153";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsCaptureUnitaireFormulaire getFormulairePourGet() {
      
      TestWsCaptureUnitaireFormulaire formulaire = new TestWsCaptureUnitaireFormulaire();
      
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      
      
      // URL ECDE
      formCapture.setUrlEcde(getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureUnitaire-153-CaptureUnitaire-KO-MetadonneeObligatoireOmise/documents/doc1.PDF"));
      
      
      // Métadonnées
      // Note : absence des métadonnées obligatoires CodeOrganismeProprietaire et CodeRND
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("Titre","Attestation de vigilance");
      metadonnees.add("DateCreation","2011-09-01");
      metadonnees.add("ApplicationProductrice","ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire","CER69");
      metadonnees.add("Hash","a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("TypeHash","SHA-1");
      metadonnees.add("NbPages","2");
      metadonnees.add("FormatFichier","fmt/354");
      metadonnees.add("Denomination","Test 153-CaptureUnitaire-KO-MetadonneeObligatoireOmise");
      
      
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
            "sae_CaptureMetadonneesArchivageObligatoire",
            new String[] {"CodeOrganismeProprietaire, CodeRND"});
      
   }
   
 
}
