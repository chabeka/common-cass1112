package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ComptagesTdmFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ModificationMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ModificationMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test3700Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModificationMasseResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

@Controller
@RequestMapping(value = "test3700")
public class Test3700Controller extends AbstractTestWsController<Test3700Formulaire>{

   @Override
   protected String getNumeroTest() {
      // TODO Auto-generated method stub
      return "3700";
   }

   @Override
   protected Test3700Formulaire getFormulairePourGet() {
      
      Test3700Formulaire formulaire = new Test3700Formulaire();
      
   // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      
      ModificationMasseFormulaire formModification = formulaire.getModifMasseDecl();
      formModification.setUrlSommaire(getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/TransfertMasse-3700/sommaire.xml"));
      formModification.setHash("23ec83cefdd26f30b68ecbbae1ce6cf6560bca44");
      formModification.setTypeHash("SHA-1");
      formModification.setAvecHash(Boolean.TRUE);
      formModification.getResultats().setStatus(TestStatusEnum.SansStatus);
      formModification.setCodeTraitement("UR827");
      
      ModificationMasseResultatFormulaire formResultat = formulaire.getModifMasseResult();
      formResultat.setUrlSommaire(formModification.getUrlSommaire());
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      ComptagesTdmFormulaire comptageFormulaire = formulaire.getComptagesFormulaire();
      comptageFormulaire.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      return formulaire;
   }

   @Override
   protected void doPost(Test3700Formulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {
         
         etape1ModificationMasseAppelWs(
               formulaire.getUrlServiceWeb(),
               formulaire);
         
      } else if ("2".equals(etape)) {
         
         etape2TransfertMasseResultats(
               formulaire.getModifMasseResult());
         
      } else if ("3".equals(etape)) {

         etape3Comptages(formulaire.getComptagesFormulaire());

      } else {
         
         throw new IntegrationRuntimeException("L'étape " + etape + " est inconnue !");
         
      }  
      
   }
   
   private void etape1ModificationMasseAppelWs(
         String urlWebService,
         Test3700Formulaire formulaire) {
      
      // Vide le résultat du test précédent de l'étape 2
      ModificationMasseResultatFormulaire formModificationMassRes = formulaire.getModifMasseResult();
      formModificationMassRes.getResultats().clear();
      formModificationMassRes.setUrlSommaire(null);
      
      // Vide le résultat du test précédent de l'étape 3
      ComptagesTdmFormulaire formComptages = formulaire.getComptagesFormulaire();
      formComptages.getResultats().clear();
      formComptages.setIdTdm(null);
      
      // Appel de la méthode de test
      ModificationMasseResultat cmResult = getModificationMasseTestService().appelWsOpModifMasseTestLibre(
            urlWebService, 
            formulaire.getModifMasseDecl(), formulaire.getViFormulaire());
      
      // Renseigne le formulaire de l'étape 2
      formModificationMassRes.setUrlSommaire(formulaire.getModifMasseDecl().getUrlSommaire());
      
      // Si pas d'erreur, on pré-remplit le formulaire de l'étape 3
      if ((cmResult!=null) && (cmResult.isAppelAvecHashSommaire())) {
         formComptages.setIdTdm(cmResult.getIdTraitement());
      }
      
   }

   private void etape2TransfertMasseResultats(
         ModificationMasseResultatFormulaire formulaire) {
      
      getModificationMasseTestService().regardeResultatsTdm(formulaire);
      
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
