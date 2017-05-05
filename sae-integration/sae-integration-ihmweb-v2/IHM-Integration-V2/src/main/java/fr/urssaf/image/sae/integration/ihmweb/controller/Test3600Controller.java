package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ComptagesTdmFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test3600Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TransfertMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TransfertMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureMasseResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.TransfertMasseResultat;


@Controller
@RequestMapping(value = "test3600")
public class Test3600Controller extends AbstractTestWsController<Test3600Formulaire> {

   
   @Override
   protected String getNumeroTest() {
      // TODO Auto-generated method stub
      return "3600";
   }

   @Override
   protected Test3600Formulaire getFormulairePourGet() {
      
Test3600Formulaire formulaire = new Test3600Formulaire();
      
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
      
      TransfertMasseFormulaire formtransfert = formulaire.getTransfertMasseDeclenchement();
      formtransfert.setUrlSommaire(getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/TransfertMasse-3600/sommaire.xml"));
      formtransfert.setHash("23ec83cefdd26f30b68ecbbae1ce6cf6560bca44");
      formtransfert.setTypeHash("SHA-1");
      formtransfert.setAvecHash(Boolean.TRUE);
      formtransfert.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      TransfertMasseResultatFormulaire formResultat = formulaire.getTransfertMasseResultat();
      formResultat.setUrlSommaire(formtransfert.getUrlSommaire());
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      ComptagesTdmFormulaire comptageFormulaire = formulaire.getComptagesFormulaire();
      comptageFormulaire.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      return formulaire;
   }

   @Override
   protected void doPost(Test3600Formulaire formulaire) {
      
      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {
         
         etape1TransfertMasseAppelWs(
               formulaire.getUrlServiceWeb(),
               formulaire);
         
      } else if ("2".equals(etape)) {
         
         etape2TransfertMasseResultats(
               formulaire.getTransfertMasseResultat());
         
      } else if ("3".equals(etape)) {

         etape3Comptages(formulaire.getComptagesFormulaire());

      } else if ("4".equals(etape)){
         
         etape1captureMasseAppelWs(
               formulaire.getUrlServiceWeb(),
               formulaire);
         
      }
      else {
         
         throw new IntegrationRuntimeException("L'étape " + etape + " est inconnue !");
         
      }
      
      
   }
   
   private void etape1captureMasseAppelWs(
         String urlWebService,
         Test3600Formulaire formulaire) {
      
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

   private void etape1TransfertMasseAppelWs(
         String urlWebService,
         Test3600Formulaire formulaire) {
      
      // Vide le résultat du test précédent de l'étape 2
      TransfertMasseResultatFormulaire formTransfertMassRes = formulaire.getTransfertMasseResultat();
      formTransfertMassRes.getResultats().clear();
      formTransfertMassRes.setUrlSommaire(null);
      
      // Vide le résultat du test précédent de l'étape 3
      ComptagesTdmFormulaire formComptages = formulaire.getComptagesFormulaire();
      formComptages.getResultats().clear();
      formComptages.setIdTdm(null);
      
      // Appel de la méthode de test
      TransfertMasseResultat cmResult = getTransfertMasseTestService().appelWsOpTransMasseTestLibre(
            urlWebService, 
            formulaire.getTransfertMasseDeclenchement(), formulaire.getViFormulaire());
      
      // Renseigne le formulaire de l'étape 2
      formTransfertMassRes.setUrlSommaire(formulaire.getTransfertMasseDeclenchement().getUrlSommaire());
      
      // Si pas d'erreur, on pré-remplit le formulaire de l'étape 3
      if ((cmResult!=null) && (cmResult.isAppelAvecHashSommaire())) {
         formComptages.setIdTdm(cmResult.getIdTraitement());
      }
      
   }
   
   
   
   private void etape2TransfertMasseResultats(
         TransfertMasseResultatFormulaire formulaire) {
      
      getTransfertMasseTestService().regardeResultatsTdm(formulaire);
      
   }
   
   
   private void etape3Comptages(ComptagesTdmFormulaire formulaire) {

      // Initialisation de l'objet ResultatTest
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      
      // Lecture de l'identifiant du traitement de masse
      String idTdm = formulaire.getIdTdm();

      // Appel du service de comptages
      getTransfertMasseTestService().comptages(idTdm, resultatTest,
            null);
      
      // Test sans status de résultat
      resultatTest.setStatus(TestStatusEnum.SansStatus);

   }
   
}
