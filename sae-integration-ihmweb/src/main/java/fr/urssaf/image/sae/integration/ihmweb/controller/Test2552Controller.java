package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ComptagesTdmFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test2552Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.FichierType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.NonIntegratedDocumentType;

/**
 * 2552-Note-Ajout-Traitement-Masse-KO
 */
@Controller
@RequestMapping(value = "test2552")
public class Test2552Controller extends
      AbstractTestWsController<Test2552Formulaire> {

   /**
    * Nombre d'occurence attendu
    */
   private static final int COUNT_WAITED = 0;
   private static final int WAITED_COUNT = 10;
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2552";
   }

   private String getDebutUrlEcde() {
      return getEcdeService()
            .construitUrlEcde(
                  "SAE_INTEGRATION/20110822/Note-2552-Note-Ajout-Traitement-Masse-KO/");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test2552Formulaire getFormulairePourGet() {

      Test2552Formulaire formulaire = new Test2552Formulaire();

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
      rechFormulaire.setRequeteLucene(getCasTest().getLuceneExemple());

      CodeMetadonneeList codeMetadonneeList = new CodeMetadonneeList();
      rechFormulaire.setCodeMetadonnees(codeMetadonneeList);
      codeMetadonneeList.add("DateArchivage");
      codeMetadonneeList.add("Note");

      return formulaire;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test2552Formulaire formulaire) {

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
         etape4Comptages(formulaire);
      } else {
         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");
      }
   }

   private void etape1captureMasseAppelWs(String urlWebService,
         Test2552Formulaire formulaire) {

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
      fichierType.setCheminEtNomDuFichier("doc1.PDF");

      ErreurType erreurType = new ErreurType();
      erreurType.setCode("SAE-CA-BUL002");
      erreurType
            .setLibelle("Une erreur s'est produite lors de l'ajout d'une note. Détails : Note redmine 6919");
//cf Redmine 6919 de Christine du 04/09/2015 non résolu à ce jour
      ListeErreurType listeErreurType = new ListeErreurType();
      listeErreurType.getErreur().add(erreurType);

      NonIntegratedDocumentType documentType = new NonIntegratedDocumentType();
      documentType.setErreurs(listeErreurType);
      documentType.setObjetNumerique(fichierType);

      getCaptureMasseTestService().testResultatsTdmReponseKOAttendue(
            formulaire, WAITED_COUNT, documentType);
           
   }

   private void etape3Recherche(Test2552Formulaire formulaire) {

      getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            formulaire.getUrlServiceWeb(), formulaire.getRechFormulaire(), 0,
            false, null);
      
   }


   private void etape4Comptages(Test2552Formulaire formulaire) {

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
