package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ModificationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test1701Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;

/**
 *   1760-Ged-Technique-KO-AM-Meme-Meta
 */
@Controller
@RequestMapping(value = "test1760")
public class Test1760Controller extends
      AbstractTestWsController<Test1701Formulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1760";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test1701Formulaire getFormulairePourGet() {

      
      Test1701Formulaire formulaire = new Test1701Formulaire();
      
      // capture unitaire
      CaptureUnitaireFormulaire captUnit = formulaire.getCaptureUnitaire();

      // L'URL ECDE
      captUnit
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Ged-Technique-1760-Ged-Technique-KO-AM-Meme-Meta/documents/doc1.PDF"));

      // Le nom du fichier
      captUnit.setNomFichier("doc1.PDF");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      captUnit.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "UR750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-05");
      metadonnees.add("Denomination","Test 1760-Ged-Technique-KO-AM-Meme-Meta");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre", "AUTRE COURRIER ENTRANT RELATIF A LA GESTION DES DONNEES ADMINISTRATIVES");
      metadonnees.add("TypeHash", "SHA-1");

      // Initialise le formulaire de recherche
      
      RechercheFormulaire rechFormulaire = formulaire.getRechFormulaire();
      
      CodeMetadonneeList codeMetadonneeList = new CodeMetadonneeList();
      codeMetadonneeList.add("Periode");
      
      rechFormulaire.setRequeteLucene(getCasTest().getLuceneExemple());
      rechFormulaire.setCodeMetadonnees(codeMetadonneeList);
      
      // formulaire de modification
      ModificationFormulaire formModification = formulaire.getModification();
      formModification.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      // Les métadonnées
      MetadonneeValeurList metaModif = new MetadonneeValeurList();
      metaModif.add("Periode","PERI");
      metaModif.add("Periode","0402");
      formModification.setMetadonnees(metaModif);
      
      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test1701Formulaire formulaire) {
      String etape = formulaire.getEtape();
      
      if ("1".equals(etape)) {
         etape1captureUnitaireAppelWs(formulaire);
      } else if ("2".equals(etape)) {
         modification(formulaire.getUrlServiceWeb(), formulaire
               .getModification());
      } else if ("3".equals(etape)) {
         recherche(formulaire.getUrlServiceWeb(), formulaire
               .getRechFormulaire(), formulaire.getViFormulaire());
      } 
   }
   
   private void etape1captureUnitaireAppelWs(Test1701Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptureUnitaire();

      // Lance le test
      CaptureUnitaireResultat resultat = getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1, formulaire.getViFormulaire());
      
      // Si le test n'est pas en échec, alors on peut initialiser l'id du document à supprimer
      if (!TestStatusEnum.Echec.equals(formulaire.getCaptureUnitaire().getResultats().getStatus())) {
         formulaire.getModification().setIdDocument(UUID.fromString(resultat.getIdArchivage()));
      }
   }
   
   private void modification(String urlWebService,
         ModificationFormulaire formulaire) {

      // Appel de la méthode de test
      getModificationTestService().appelWsOpModificationSoapFault(
            urlWebService, formulaire, ViStyle.VI_OK,
            "sae_ModificationMetadonneeDoublon",
            new Object[] { "Periode" });      
   }

   private void recherche(String urlServiceWeb, RechercheFormulaire formulaire,
         ViFormulaire viParams) {

      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

      // Résultats attendus
      int nbResultatsAttendus = 1;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(urlServiceWeb,
                  formulaire, nbResultatsAttendus,
                  flagResultatsTronquesAttendu, null);

      // Vérifications en profondeur
      if ((response != null)
            && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {

         // Récupère l'unique résultat
         ResultatRechercheType resultatRecherche = response
               .getRechercheResponse().getResultats().getResultat()[0];

         // Le vérifie
         verifieResultatN(1, resultatRecherche, resultatTest);

      }

      // Si le test n'est pas en échec, alors on peut le passer en succès,
      // car tout a pu être vérifié
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }
   }
   
   private void verifieResultatN(int numeroResultatRecherche,
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("Periode", "");
      
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);
   }

}
