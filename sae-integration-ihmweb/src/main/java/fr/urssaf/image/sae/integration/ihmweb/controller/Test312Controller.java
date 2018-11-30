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
 * 312-Recherche-OK-Date-Reception
 */
@Controller
@RequestMapping(value = "test312")
public class Test312Controller extends
      AbstractTestWsController<TestWsRechercheFormulaire> {

   /**
    * 
    */
   private static final int WAITED_COUNT = 3;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "312";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsRechercheFormulaire getFormulairePourGet() {

      TestWsRechercheFormulaire formulaire = new TestWsRechercheFormulaire();
      RechercheFormulaire formRecherche = formulaire.getRecherche();

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Les métadonnées que l'on souhaite en retour
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      formRecherche.setCodeMetadonnees(codesMeta);
      codesMeta.add("ApplicationProductrice");
      codesMeta.add("CodeRND");
      codesMeta.add("DateReception");
      codesMeta.add("Denomination");
      codesMeta.add("NumeroRecours");
      codesMeta.add("Siren");

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
                  flagResultatsTronquesAttendu, TypeComparaison.NumeroRecours);

      // Vérifications en profondeur
      if ((response != null)
            && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {

         // Tri les résultats par ordre croissant de NumeroRecours
         List<ResultatRechercheType> resultatsTries = Arrays.asList(response
               .getRechercheResponse().getResultats().getResultat());
         Collections.sort(resultatsTries, new ResultatRechercheComparator(
               TypeComparaison.NumeroRecours));

         // Vérifie chaque résultat
         verifieResultat1(resultatsTries.get(0), resultatTest);
         verifieResultat2(resultatsTries.get(1), resultatTest);
         verifieResultat3(resultatsTries.get(2), resultatTest);

         // Passe le test en succès si aucune erreur détectée
         if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
            resultatTest.setStatus(TestStatusEnum.Succes);
         }

      }

   }
   

   private void verifieResultat1(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "1";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("DateReception", "2011-11-03");
      valeursAttendues.add("Denomination", "Test 312-Recherche-OK-Date-Reception");
      valeursAttendues.add("NumeroRecours", "3");
      valeursAttendues.add("Siren", "123456789");

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }
   
   
   private void verifieResultat2(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "2";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("DateReception", "2011-11-04");
      valeursAttendues.add("Denomination", "Test 312-Recherche-OK-Date-Reception");
      valeursAttendues.add("NumeroRecours", "4");
      valeursAttendues.add("Siren", "123456789");

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }
   
   
   private void verifieResultat3(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "3";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
      valeursAttendues.add("CodeRND", "2.3.1.1.8");
      valeursAttendues.add("DateReception", "2011-11-05");
      valeursAttendues.add("Denomination", "Test 312-Recherche-OK-Date-Reception");
      valeursAttendues.add("NumeroRecours", "5");
      valeursAttendues.add("Siren", "123456789");

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

}
