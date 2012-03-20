package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsRechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 301-Recherche-OK-Standard
 */
@Controller
@RequestMapping(value = "test301")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test301Controller extends
      AbstractTestWsController<TestWsRechercheFormulaire> {

   /**
    * 
    */
   private static final int WAITED_COUNT = 7;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "301";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsRechercheFormulaire getFormulairePourGet() {

      TestWsRechercheFormulaire formulaire = new TestWsRechercheFormulaire();
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Pas de métadonnées spécifiques à récupérer
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("CodeOrganismeGestionnaire");
      codesMeta.add("CodeOrganismeProprietaire");
      codesMeta.add("CodeRND");
      codesMeta.add("ContratDeService");
      codesMeta.add("DateArchivage");
      codesMeta.add("DateCreation");
      codesMeta.add("DateReception");
      codesMeta.add("FormatFichier");
      codesMeta.add("Hash");
      codesMeta.add("NomFichier");
      codesMeta.add("TailleFichier");
      codesMeta.add("Titre");
      codesMeta.add("NumeroRecours");
      formRecherche.setCodeMetadonnees(codesMeta);

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsRechercheFormulaire formulaire) {
      recherche(formulaire.getUrlServiceWeb(), formulaire.getRecherche());
   }

   private void recherche(String urlServiceWeb, RechercheFormulaire formulaire) {

      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

      // Résultats attendus
      int nbResultatsAttendus = WAITED_COUNT;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(urlServiceWeb,
                  formulaire, nbResultatsAttendus,
                  flagResultatsTronquesAttendu, TypeComparaison.DateCreation);

      // Vérifications en profondeur
      if ((response != null)
            && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {

         // Tri les résultats par ordre croissant de DateCreation
         List<ResultatRechercheType> resultatsTries = Arrays.asList(response
               .getRechercheResponse().getResultats().getResultat());
         Collections.sort(resultatsTries, new ResultatRechercheComparator(
               TypeComparaison.DateCreation));

         // Vérifie chaque résultat
         verifieResultat1(resultatsTries.get(0), resultatTest);
         verifieResultat2(resultatsTries.get(1), resultatTest);
         verifieResultat3(resultatsTries.get(2), resultatTest);
         verifieResultat4(resultatsTries.get(3), resultatTest);
         verifieResultat5(resultatsTries.get(4), resultatTest);
         verifieResultat6(resultatsTries.get(5), resultatTest);
         verifieResultat7(resultatsTries.get(6), resultatTest);
      }

      // Au mieux, si le test est OK, on le passe "A contrôler", pour la
      // vérification
      // de la date d'archivage
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.AControler);
      }

   }

   private void verifieResultat1(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "1";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("Titre", "Attestation de vigilance");
      valeursAttendues.add("DateCreation", "2010-10-01");
      valeursAttendues.add("DateReception", "2011-11-01");
      valeursAttendues.add("CodeOrganismeProprietaire", "CER69");
      valeursAttendues.add("CodeOrganismeGestionnaire", "UR750");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      valeursAttendues.add("NomFichier", "doc1.PDF");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("TailleFichier", "56587");
      valeursAttendues.add("ContratDeService", "ATT_PROD_001");
      // valeursAttendues.add("DateArchivage",); // <= à vérifier manuellement

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

   private void verifieResultat2(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "2";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("Titre", "Attestation de vigilance");
      valeursAttendues.add("DateCreation", "2010-10-02");
      valeursAttendues.add("DateReception", "2011-11-02");
      valeursAttendues.add("CodeOrganismeProprietaire", "CER69");
      valeursAttendues.add("CodeOrganismeGestionnaire", "UR750");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("Hash", "f9e67c1ea944b5042e855c5955ef4d5ea83ff308");
      valeursAttendues.add("NomFichier", "doc4.PDF");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("TailleFichier", "48637");
      valeursAttendues.add("ContratDeService", "ATT_PROD_001");
      // valeursAttendues.add("DateArchivage",); // <= à vérifier manuellement

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

   private void verifieResultat3(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "3";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("Titre", "Attestation de vigilance");
      valeursAttendues.add("DateCreation", "2010-10-03");
      valeursAttendues.add("DateReception", "2011-11-03");
      valeursAttendues.add("CodeOrganismeProprietaire", "CER69");
      valeursAttendues.add("CodeOrganismeGestionnaire", "UR750");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("Hash", "3d85ba8d3eb78ea55f6f26c167e0d676ac473086");
      valeursAttendues.add("NomFichier", "doc7.PDF");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("TailleFichier", "48646");
      valeursAttendues.add("ContratDeService", "ATT_PROD_001");
      // valeursAttendues.add("DateArchivage",); // <= à vérifier manuellement

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

   private void verifieResultat4(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "4";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("Titre", "Attestation de vigilance");
      valeursAttendues.add("DateCreation", "2010-10-04");
      valeursAttendues.add("DateReception", "2011-11-04");
      valeursAttendues.add("CodeOrganismeProprietaire", "CER69");
      valeursAttendues.add("CodeOrganismeGestionnaire", "UR750");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("Hash", "5071b0a0581e23fd912980e8b341aa962821608c");
      valeursAttendues.add("NomFichier", "doc11.PDF");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("TailleFichier", "59459");
      valeursAttendues.add("ContratDeService", "ATT_PROD_001");
      // valeursAttendues.add("DateArchivage",); // <= à vérifier manuellement

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

   private void verifieResultat5(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "5";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("Titre", "Attestation de marché public");
      valeursAttendues.add("DateCreation", "2010-10-05");
      valeursAttendues.add("DateReception", "2011-11-05");
      valeursAttendues.add("CodeOrganismeProprietaire", "CER69");
      valeursAttendues.add("CodeOrganismeGestionnaire", "UR750");
      valeursAttendues.add("CodeRND", "2.3.1.1.8");
      valeursAttendues.add("Hash", "bc6ad31335a3af36d3c157382b6ccad98a8fbe2a");
      valeursAttendues.add("NomFichier", "doc70.PDF");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("TailleFichier", "47308");
      valeursAttendues.add("ContratDeService", "ATT_PROD_001");
      // valeursAttendues.add("DateArchivage",); // <= à vérifier manuellement

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

   private void verifieResultat6(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "6";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("Titre", "Attestation de marché public");
      valeursAttendues.add("DateCreation", "2010-10-06");
      valeursAttendues.add("DateReception", "2011-11-06");
      valeursAttendues.add("CodeOrganismeProprietaire", "CER69");
      valeursAttendues.add("CodeOrganismeGestionnaire", "UR750");
      valeursAttendues.add("CodeRND", "2.3.1.1.8");
      valeursAttendues.add("Hash", "48da11810a23e2f0d3faad730bb3355e3d5c4ac4");
      valeursAttendues.add("NomFichier", "doc77.PDF");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("TailleFichier", "58555");
      valeursAttendues.add("ContratDeService", "ATT_PROD_001");
      // valeursAttendues.add("DateArchivage",); // <= à vérifier manuellement

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

   private void verifieResultat7(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "7";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("Titre", "Attestation de marché public");
      valeursAttendues.add("DateCreation", "2010-10-07");
      valeursAttendues.add("DateReception", "2011-11-07");
      valeursAttendues.add("CodeOrganismeProprietaire", "CER69");
      valeursAttendues.add("CodeOrganismeGestionnaire", "UR750");
      valeursAttendues.add("CodeRND", "2.3.1.1.8");
      valeursAttendues.add("Hash", "f167d69e3a1b444539ede74c46663b35eee00d1d");
      valeursAttendues.add("NomFichier", "doc132.PDF");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("TailleFichier", "46887");
      valeursAttendues.add("ContratDeService", "ATT_PROD_001");
      // valeursAttendues.add("DateArchivage",); // <= à vérifier manuellement

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

}
