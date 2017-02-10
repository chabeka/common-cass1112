package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ComptagesTdmFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test200Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureMasseResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;


/**
 * 200-CaptureMasse-TestLibre
 */
@Controller
@RequestMapping(value = "test200")
public class Test200Controller extends AbstractTestWsController<Test200Formulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "200";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test200Formulaire getFormulairePourGet() {
      
      Test200Formulaire formulaire = new Test200Formulaire();
      
      // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      
      CaptureMasseFormulaire formCapture = formulaire.getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-200/sommaire.xml"));
      formCapture.setHash("23ec83cefdd26f30b68ecbbae1ce6cf6560bca44");
      formCapture.setTypeHash("SHA-1");
      formCapture.setAvecHash(Boolean.TRUE);
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      CaptureMasseResultatFormulaire formResultat = formulaire.getCaptureMasseResultat();
      formResultat.setUrlSommaire(formCapture.getUrlSommaire());
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      ComptagesTdmFormulaire comptageFormulaire = formulaire.getComptagesFormulaire();
      comptageFormulaire.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test200Formulaire formulaire) {
      
      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {
         
         etape1captureMasseAppelWs(
               formulaire.getUrlServiceWeb(),
               formulaire);
         
      } else if ("2".equals(etape)) {
         
         etape2captureMasseResultats(
               formulaire.getCaptureMasseResultat());
         
      } else if ("3".equals(etape)) {

         etape3Comptages(formulaire.getComptagesFormulaire());

      } else {
         
         throw new IntegrationRuntimeException("L'étape " + etape + " est inconnue !");
         
      }
      
   }
   
   
   private void etape1captureMasseAppelWs(
         String urlWebService,
         Test200Formulaire formulaire) {
      
      // Vide le résultat du test précédent de l'étape 2
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire.getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(null);
      
      // Vide le résultat du test précédent de l'étape 3
      ComptagesTdmFormulaire formComptages = formulaire.getComptagesFormulaire();
      formComptages.getResultats().clear();
      formComptages.setIdTdm(null);
      
      // Appel de la méthode de test
      CaptureMasseResultat cmResult = getCaptureMasseTestService().appelWsOpArchiMasseTestLibre(
            urlWebService, 
            formulaire.getCaptureMasseDeclenchement(), formulaire.getViFormulaire());
      
      // Renseigne le formulaire de l'étape 2
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement().getUrlSommaire());
      
      // Si pas d'erreur, on pré-remplit le formulaire de l'étape 3
      if ((cmResult!=null) && (cmResult.isAppelAvecHashSommaire())) {
         formComptages.setIdTdm(cmResult.getIdTraitement());
      }
      
   }
   
   
   
   private void etape2captureMasseResultats(
         CaptureMasseResultatFormulaire formulaire) {
      
      getCaptureMasseTestService().regardeResultatsTdm(formulaire);
      
   }
   
   
   private void etape3Comptages(ComptagesTdmFormulaire formulaire) {

      // Initialisation de l'objet ResultatTest
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      
      // Lecture de l'identifiant du traitement de masse
      String idTdm = formulaire.getIdTdm();

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest,
            null);
      
      // Test sans status de résultat
      resultatTest.setStatus(TestStatusEnum.SansStatus);

   }
   
 
}
