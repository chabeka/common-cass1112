package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsCaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;


/**
 * 164-CaptureUnitaire-KO-MetadonneeValeurVide
 */
@Controller
@RequestMapping(value = "test164")
public class Test164Controller extends AbstractTestWsController<TestWsCaptureUnitaireFormulaire> {


   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "164";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsCaptureUnitaireFormulaire getFormulairePourGet() {
      
      TestWsCaptureUnitaireFormulaire formulaire = new TestWsCaptureUnitaireFormulaire();
      
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      
      
      // URL ECDE et nom du fichier
      formCapture.setUrlEcde(getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureUnitaire-164-CaptureUnitaire-KO-MetadonneeValeurVide/documents/doc1.PDF"));
      formCapture.setNomFichier("doc1.PDF");
      
      
      // Métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice","ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire","CER69");
      metadonnees.add("CodeOrganismeProprietaire","AC750");
      metadonnees.add("CodeRND","2.3.1.1.12");
      metadonnees.add("DateCreation","2011-09-01");
      metadonnees.add("Denomination","Test 163-CaptureUnitaire-KO-TailleZero");
      metadonnees.add("FormatFichier","fmt/354");
      metadonnees.add("Hash","a2f93f1f121ebba0faef2c0596f2f126eacae77b ");
      metadonnees.add("NbPages","2");
      metadonnees.add("Siret",StringUtils.EMPTY);
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
      getCaptureUnitaireTestService().appelWsOpCaptureUnitaireSoapFault(
            urlServiceWeb, 
            formulaire,
            ViStyle.VI_OK,
            "sae_CaptureMetadonneesFormatTypeNonValide",
            new String[] {"Siret"});
      
   }
   
 
}
