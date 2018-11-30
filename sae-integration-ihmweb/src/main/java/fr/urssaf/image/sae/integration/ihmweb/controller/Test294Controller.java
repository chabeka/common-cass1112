package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ComptagesTdmFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test294Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.FichierType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.NonIntegratedVirtualDocumentType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;

/**
 * 294-CaptureMasse-KO-Virtuel-Zero-Page
 */
@Controller
@RequestMapping(value = "test294")
public class Test294Controller extends
      AbstractTestWsController<Test294Formulaire> {

   /**
    * Nombre d'occurence attendu
    */
   private static final int COUNT_WAITED = 0;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "294";
   }
   
   
   private String getDebutUrlEcde() {
      return getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-294-CaptureMasse-KO-Virtuel-Zero-Page/");
   }
   

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test294Formulaire getFormulairePourGet() {

      Test294Formulaire formulaire = new Test294Formulaire();

      // Initialise le formulaire de capture de masse
      
      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getDebutUrlEcde() + "sommaire.xml");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde() + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      
      // Initialise le formulaire de recherche
      
      RechercheFormulaire rechFormulaire = formulaire.getRechFormulaire();
      rechFormulaire
            .setRequeteLucene(getCasTest().getLuceneExemple());
      
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test294Formulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("2".equals(etape)) {

         etape2captureMasseResultats(formulaire.getCaptureMasseResultat());
         
         // initialise l'identifiant de traitement de masse en lisant le fichier
         // debut_traitement.flag
         String idTdm = getCaptureMasseTestService().readIdTdmInDebutTrait(
               formulaire.getCaptureMasseDeclenchement().getUrlSommaire());
         ComptagesTdmFormulaire formComptage = formulaire
               .getComptagesFormulaire();
         formComptage.setIdTdm(idTdm);

      } else if ("3".equals(etape)) {

         etape3Recherche(formulaire);

      } else if ("4".equals(etape)) {

         etape5Comptages(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureMasseAppelWs(String urlWebService,
         Test294Formulaire formulaire) {

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

      ErreurType erreurType = new ErreurType();
      erreurType.setCode("SAE-CA-BUL001");
      erreurType.setLibelle("Une erreur interne à l'application est survenue lors de la capture du document virtuel. Détails : start page must be > endPage, acutal : startPage = 1, endPage = 0");
      
      ListeErreurType listeErreurType = new ListeErreurType();
      listeErreurType.getErreur().add(erreurType);
      
      FichierType fichierType = new FichierType();
      fichierType.setCheminEtNomDuFichier("doc1.PDF");
      
      NonIntegratedVirtualDocumentType documentType = new NonIntegratedVirtualDocumentType();
      documentType.setErreurs(listeErreurType);
      documentType.setObjetNumerique(fichierType);

      getCaptureMasseTestService()
            .testResultatsTdmReponseVirtualDocumentKOAttendue(formulaire, 1, documentType, 0, 0);

   }

   private void etape3Recherche(Test294Formulaire formulaire) {
      
      
      // Appel le service de test de la recherche
      getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getRechFormulaire(), COUNT_WAITED, false,
                  TypeComparaison.NumeroRecours);

      ResultatTest resultatTest = formulaire.getRechFormulaire().getResultats();
      
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }
   }


   
   

   private void etape5Comptages(Test294Formulaire formulaire) {

      // Récupération de l'objet ResultatTest
      ResultatTest resultatTest = formulaire.getComptagesFormulaire()
            .getResultats();
      resultatTest.clear();

      // Lecture de l'identifiant du traitement de masse
      String idTdm = formulaire.getComptagesFormulaire().getIdTdm();

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest,
            new Long(COUNT_WAITED));

      // Passe le test en OK si pas KO
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

}
