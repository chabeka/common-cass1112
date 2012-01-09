package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsCaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.utils.ViUtils;


/**
 * Test 150<br>
 * <br>
 * Ce test vérifie que l'authentification applicative est activée 
 * sur l'opération "archivageUnitaire" du service web SaeService. 
 * Pour cela, on invoque l'opération "archivageUnitaire" en omettant 
 * le VI dans le message SOAP.
 */
@Controller
@RequestMapping(value = "test150")
public class Test150Controller extends AbstractTestWsController<TestWsCaptureUnitaireFormulaire> {


   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "150";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsCaptureUnitaireFormulaire getFormulairePourGet() {
      
      TestWsCaptureUnitaireFormulaire formulaire = new TestWsCaptureUnitaireFormulaire();
      
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      formCapture.setUrlEcde("ecde://ecdeX/numeroCS/20110527/idTraitement/documents/nom_du_fichier");
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      metadonnees.add(new MetadonneeValeur("CodeBidon1", "ValeurBidon1"));
      metadonnees.add(new MetadonneeValeur("CodeBidon2", "ValeurBidon2"));
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
            urlServiceWeb, 
            formulaire,
            ViUtils.FIC_VI_SANS_VI,
            "wsse_SecurityTokenUnavailable",
            null);
      
   }
   
 
}
