package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestStockageMasseAllFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.FichierType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.NonIntegratedDocumentType;

/**
 * 270-CaptureMasse-KO-Tor-3000-rollback
 */
@Controller
@RequestMapping(value = "test270")
public class Test270Controller extends
      AbstractTestWsController<TestStockageMasseAllFormulaire> {

   /**
    * 
    */
   private static final int ERROR_NUMBER = 2001;
   /**
    * 
    */
   private static final int WAITED_COUNT = 3000;
   

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "270";
   }
   
   
   private String getDebutUrlEcde() {
      return getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-270-CaptureMasse-KO-Tor-3000-Rollback/");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestStockageMasseAllFormulaire getFormulairePourGet() {

      TestStockageMasseAllFormulaire formulaire = new TestStockageMasseAllFormulaire();

      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getDebutUrlEcde() + "sommaire.xml");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde() + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      RechercheFormulaire rechFormulaire = formulaire.getRechFormulaire();
      rechFormulaire
            .setRequeteLucene(getCasTest().getLuceneExemple());

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestStockageMasseAllFormulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("2".equals(etape)) {

         etape2captureMasseResultats(formulaire.getCaptureMasseResultat());

      } else if ("3".equals(etape)) {

         etape3Recherche(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureMasseAppelWs(String urlWebService,
         TestStockageMasseAllFormulaire formulaire) {

      // Vide le résultat du test précédent de l'étape 2
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(urlWebService,
            formulaire.getCaptureMasseDeclenchement());

   }

   private void etape2captureMasseResultats(
         CaptureMasseResultatFormulaire formulaire) {

      FichierType fichierType = new FichierType();
      fichierType.setCheminEtNomDuFichier("doc0.PDF");

      ErreurType erreurType = new ErreurType();
      erreurType.setCode("SAE-EC-SOM002");
      erreurType
            .setLibelle("Impossible d'accéder au document doc0.PDF. Détails : "
                  + "L'objet numérique : doc0.PDF ne représente pas un fichier existant.");

      ListeErreurType listeErreurType = new ListeErreurType();
      listeErreurType.getErreur().add(erreurType);

      NonIntegratedDocumentType documentType = new NonIntegratedDocumentType();
      documentType.setErreurs(listeErreurType);
      documentType.setObjetNumerique(fichierType);

      getCaptureMasseTestService().testResultatsTdmReponseKOAttendue(
            formulaire, WAITED_COUNT, documentType, ERROR_NUMBER);

   }

   private void etape3Recherche(TestStockageMasseAllFormulaire formulaire) {

      getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            formulaire.getUrlServiceWeb(), formulaire.getRechFormulaire(), 0,
            false, null);

   }
}
