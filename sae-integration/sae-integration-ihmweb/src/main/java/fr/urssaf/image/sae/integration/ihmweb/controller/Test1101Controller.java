package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test1101Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 1101-Droits-Conformite-Recherche-Attestation
 */
@Controller
@RequestMapping(value = "test1101")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1101Controller extends
      AbstractTestWsController<Test1101Formulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1101";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testDrCuRe";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test1101Formulaire getFormulairePourGet() {

      Test1101Formulaire formulaire = new Test1101Formulaire();

      // capture unitaire
      CaptureUnitaireFormulaire captUnit = formulaire.getCaptUnit();

      // L'URL ECDE
      captUnit
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Droit-1101-Droits-Conformite-Archivage-Unitaire-ATT-VIGI/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));

      // Le nom du fichier
      captUnit.setNomFichier("ADELPF_710_PSNV211157BPCA1L0000.pdf");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      captUnit.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "UR750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-01");
      metadonnees.add("Denomination",
            "Test 1101-Droits-Conformite-Archivage-Unitaire-ATT-VIGI");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "d145ea8e0ca28b8c97deb0c2a550f0a969a322a3");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");

      // formulaire de recherche

      RechercheFormulaire formRecherche = formulaire.getRechercheFormulaire();
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Pas de métadonnées spécifiques à récupérer
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("ApplicationProductrice");
      codesMeta.add("ApplicationTraitement");
      codesMeta.add("CodeActivite");
      codesMeta.add("CodeCategorieV2");
      codesMeta.add("CodeFonction");
      codesMeta.add("CodeOrganismeGestionnaire");
      codesMeta.add("CodeOrganismeProprietaire");
      codesMeta.add("CodeSousCategorieV2");
      codesMeta.add("CodeRND");
      codesMeta.add("ContratDeService");
      codesMeta.add("DateArchivage");
      codesMeta.add("DateCreation");
      codesMeta.add("DateDebutConservation");
      codesMeta.add("DateSignature");
      codesMeta.add("DateFinConservation");
      codesMeta.add("DateReception");
      codesMeta.add("Denomination");
      codesMeta.add("DureeConservation");
      codesMeta.add("FormatFichier");
      codesMeta.add("Gel");
      codesMeta.add("Hash");
      codesMeta.add("IdTraitementMasse");
      codesMeta.add("IdTraitementMasseInterne");
      codesMeta.add("NbPages");
      codesMeta.add("NniEmployeur");
      codesMeta.add("NomFichier");
      codesMeta.add("NumeroCompteExterne");
      codesMeta.add("NumeroCompteInterne");
      codesMeta.add("NumeroIntControle");
      codesMeta.add("NumeroPersonne");
      codesMeta.add("NumeroRecours");
      codesMeta.add("NumeroStructure");
      codesMeta.add("Periode");
      codesMeta.add("PseudoSiret");
      codesMeta.add("Siren");
      codesMeta.add("Siret");
      codesMeta.add("SiteAcquisition");
      codesMeta.add("TailleFichier");
      codesMeta.add("Titre");
      codesMeta.add("TracabilitePreArchivage");
      codesMeta.add("TracabilitePostArchivage");
      codesMeta.add("TypeHash");
      codesMeta.add("VersionRND");

      
      formRecherche.setCodeMetadonnees(codesMeta);

      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("INT_CS_ATT_VIGI");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add("INT_PAGM_ATT_VIGI_ARCH_UNIT");

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test1101Formulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {
         etape1captureUnitaireAppelWs(formulaire);
         PagmList pagmList = new PagmList();
         pagmList.add("INT_PAGM_ATT_VIGI_RECH");
         formulaire.getViFormulaire().setPagms(pagmList);
      } else if ("2".equals(etape)) {
         recherche(formulaire.getUrlServiceWeb(), formulaire
               .getRechercheFormulaire(), formulaire.getViFormulaire());
      }
   }

   private void etape1captureUnitaireAppelWs(Test1101Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptUnit();

      // Lance le test
      getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1, formulaire.getViFormulaire());
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

      valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
      valeursAttendues.add("ApplicationTraitement", StringUtils.EMPTY);
      valeursAttendues.add("CodeActivite", "3");
      valeursAttendues.add("CodeCategorieV2", StringUtils.EMPTY);
      valeursAttendues.add("CodeFonction", "2");
      valeursAttendues.add("CodeOrganismeGestionnaire", "CER69");
      valeursAttendues.add("CodeOrganismeProprietaire", "UR750");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("CodeSousCategorieV2",  StringUtils.EMPTY);
      valeursAttendues.add("ContratDeService", "INT_CS_ATT_VIGI");
//      valeursAttendues.add("DateArchivage", "");
      valeursAttendues.add("DateCreation", "2011-09-01");
//      valeursAttendues.add("DateDebutConservation","" );
//      valeursAttendues.add("DateFinConservation", "");
      valeursAttendues.add("DateReception", StringUtils.EMPTY);
      valeursAttendues.add("Denomination", "Test 1101-Droits-Conformite-Archivage-Unitaire-ATT-VIGI");
      valeursAttendues.add("DureeConservation", "1825");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("Gel", "false");
      valeursAttendues.add("Hash", "d145ea8e0ca28b8c97deb0c2a550f0a969a322a3");
      valeursAttendues.add("IdTraitementMasse", StringUtils.EMPTY);
      valeursAttendues.add("IdTraitementMasseInterne", StringUtils.EMPTY);
      valeursAttendues.add("NbPages", "2");
      valeursAttendues.add("NniEmployeur", StringUtils.EMPTY);
      valeursAttendues.add("NomFichier", "ADELPF_710_PSNV211157BPCA1L0000.pdf");
      valeursAttendues.add("NumeroCompteExterne", StringUtils.EMPTY);
      valeursAttendues.add("NumeroCompteInterne", StringUtils.EMPTY);
      valeursAttendues.add("NumeroIntControle", StringUtils.EMPTY);
      valeursAttendues.add("NumeroPersonne", StringUtils.EMPTY);
      valeursAttendues.add("NumeroRecours", StringUtils.EMPTY);
      valeursAttendues.add("NumeroStructure", StringUtils.EMPTY);
      valeursAttendues.add("Periode", StringUtils.EMPTY);
      valeursAttendues.add("PseudoSiret", StringUtils.EMPTY);
      valeursAttendues.add("Siren", StringUtils.EMPTY);
      valeursAttendues.add("Siret", StringUtils.EMPTY);
      valeursAttendues.add("SiteAcquisition", StringUtils.EMPTY);
      valeursAttendues.add("TailleFichier", "28569");
      valeursAttendues.add("Titre", "Attestation de vigilance");
      valeursAttendues.add("TracabilitePreArchivage",
            StringUtils.EMPTY);
      valeursAttendues.add("TracabilitePostArchivage",
            StringUtils.EMPTY);
      valeursAttendues.add("TypeHash", "SHA-1");
      valeursAttendues.add("VersionRND", "11.2");

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);
     
   }

}
