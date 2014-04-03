package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireDrCmRe;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.FichierType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.NonIntegratedDocumentType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;

/**
 * 1153-Droits-Archivage-Masse-KO-PRMD-Innacessible
 */
@Controller
@RequestMapping(value = "test1153")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1153Controller extends
      AbstractTestWsController<TestFormulaireDrCmRe> {

   private static final int WAITED_COUNT = 0;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1153";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testDrCmRe";
   }

   private String getDebutUrlEcde() {
      return getEcdeService()
            .construitUrlEcde(
                  "SAE_INTEGRATION/20110822/Droit-1153-Droits-Archivage-Masse-KO-PRMD-Innacessible/");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestFormulaireDrCmRe getFormulairePourGet() {

      TestFormulaireDrCmRe formulaire = new TestFormulaireDrCmRe();
      RechercheFormulaire formRecherche = formulaire.getRechercheFormulaire();
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getDebutUrlEcde() + "sommaire.xml");
      // formCapture.setHash("23ec83cefdd26f30b68ecbbae1ce6cf6560bca44");
      // formCapture.setTypeHash("SHA-1");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde() + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Pas de métadonnées spécifiques à récupérer
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("ApplicationProductrice");
      codesMeta.add("CodeRND");
      codesMeta.add("DateCreation");
      codesMeta.add("Denomination");
      codesMeta.add("NumeroRecours");
      codesMeta.add("Siren");
      formRecherche.setCodeMetadonnees(codesMeta);

      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("INT_CS_ATT_VIGI");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add("INT_PAGM_ATT_VIGI_ARCH_MASSE");

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestFormulaireDrCmRe formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire);
         PagmList pagmList = new PagmList();
         pagmList.add("INT_PAGM_ATT_VIGI_RECH");
         formulaire.getViFormulaire().setPagms(pagmList);

      } else if ("2".equals(etape)) {

         etape2captureMasseResultats(formulaire.getCaptureMasseResultat());

      } else if ("3".equals(etape)) {

         recherche(formulaire.getUrlServiceWeb(), formulaire
               .getRechercheFormulaire(), formulaire.getViFormulaire());
      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureMasseAppelWs(String urlWebService,
         TestFormulaireDrCmRe formulaire) {

      // Vide le résultat du test précédent de l'étape 2
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(urlWebService,
            formulaire.getCaptureMasseDeclenchement(),
            formulaire.getViFormulaire());

   }

   private void etape2captureMasseResultats(
         CaptureMasseResultatFormulaire formulaire) {

      ErreurType erreurType = new ErreurType();
      erreurType.setCode("SAE-CA-BUL002");
      erreurType
            .setLibelle("Les droits présents dans le vecteur d'identification sont insuffisants pour effectuer l'action demandée ");
      
      ListeErreurType listeErreurType = new ListeErreurType();
      listeErreurType.getErreur().add(erreurType);
      
      FichierType fichierType = new FichierType();
      fichierType.setCheminEtNomDuFichier("doc1.PDF");

      NonIntegratedDocumentType documentType = new NonIntegratedDocumentType();
      documentType.setErreurs(listeErreurType);
      documentType.setObjetNumerique(fichierType);

      getCaptureMasseTestService().testResultatsTdmReponseKOAttendue(
            formulaire, WAITED_COUNT, documentType, 2);

   }

   private void recherche(String urlServiceWeb, RechercheFormulaire formulaire,
         ViFormulaire viParams) {

      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

      int nbResultatsAttendus = WAITED_COUNT;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(urlServiceWeb,
                  formulaire, nbResultatsAttendus,
                  flagResultatsTronquesAttendu, TypeComparaison.NumeroRecours,
                  viParams);

      // On passe le test à OK si tous les contrôles sont passées
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

}
