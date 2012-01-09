package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsCaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.utils.ViUtils;


/**
 * Test 157<br>
 * <br>
 * On vérifie que la bonne erreur est renvoyée lorsque la liste 
 * des métadonnées contient une métadonnée dont la valeur n'est 
 * pas conforme au format attendu
 */
@Controller
@RequestMapping(value = "test157")
public class Test157Controller extends AbstractTestWsController<TestWsCaptureUnitaireFormulaire> {


   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "157";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsCaptureUnitaireFormulaire getFormulairePourGet() {
      
      TestWsCaptureUnitaireFormulaire formulaire = new TestWsCaptureUnitaireFormulaire();
      
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      
      
      // URL ECDE
      formCapture.setUrlEcde("ecde://ecde.cer69.recouv/SAE_INTEGRATION/20110822/CaptureUnitaire-157-CaptureUnitaire-KO-MetadonneeFormatIncorrect/documents/doc1.PDF");
      
      
      // Métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("Titre","Attestation de vigilance");
      metadonnees.add("DateCreation","MauvaiseDate"); // Date incorrecte
      metadonnees.add("ApplicationProductrice","ADELAIDE");
      metadonnees.add("CodeOrganismeProprietaire","AC750");
      metadonnees.add("CodeOrganismeGestionnaire","CER69");
      metadonnees.add("CodeRND","2.3.1.1.12");
      metadonnees.add("Hash","a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("TypeHash","SHA-1");
      metadonnees.add("FormatFichier","fmt/354");
      metadonnees.add("Denomination","Test 157-CaptureUnitaire-KO-MetadonneeFormatIncorrect");
      metadonnees.add("NbPages","NbPagesIncorrect"); // incorrect car format "numérique"
      metadonnees.add("CodeSousCategorieV2","12345"); // incorrect car taille max = 2
      metadonnees.add("DateReception","2011-15-01"); // incorrect car formatage attendu yyyy-mm-dd
            
      
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
            "sae_CaptureMetadonneesFormatTypeNonValide",
            new String[] {"CodeSousCategorieV2, DateCreation, DateReception, NbPages"});
      
   }
   
 
}
