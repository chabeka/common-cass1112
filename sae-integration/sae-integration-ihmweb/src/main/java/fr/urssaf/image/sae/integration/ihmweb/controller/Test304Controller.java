package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
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
 * 304-Recherche-OK-Complexe-Dates
 */
@Controller
@RequestMapping(value = "test304")
public class Test304Controller extends
      AbstractTestWsController<TestWsRechercheFormulaire> {

   /**
    * 
    */
   private static final int RETURN_COUNT = 8;
   /**
    * 
    */
   private static final int WAITED_COUNT = 9;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "304";
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
      codesMeta.add("CodeRND");
      codesMeta.add("Denomination");
      codesMeta.add("NumeroRecours");
      codesMeta.add("DateCreation");

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
         for (int i = 0; i < RETURN_COUNT; i++) {

            getRechercheTestService().verifieResultatRecherche(
                  resultatsTries.get(i), Integer.toString(i + 1), resultatTest,
                  getValeursAttendues(i + 1));

         }

      }

      // Au mieux, si le test est OK, on le passe "A contrôler", pour la
      // vérification
      // de la date d'archivage
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.AControler);
      }

   }

   private MetadonneeValeurList getValeursAttendues(int numeroResultat) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      if (numeroResultat == 1) {

         valeursAttendues.add("CodeRND", "2.3.1.1.12");
         valeursAttendues.add("Denomination",
               "Test 304-Recherche-OK-Complexe-Dates");
         valeursAttendues.add("NumeroRecours", "1");
         valeursAttendues.add("DateCreation", "2005-06-18");

      } else if (numeroResultat == 2) {

         valeursAttendues.add("CodeRND", "2.3.1.1.12");
         valeursAttendues.add("Denomination",
               "Test 304-Recherche-OK-Complexe-Dates");
         valeursAttendues.add("NumeroRecours", "2");
         valeursAttendues.add("DateCreation", "2005-07-18");

      } else if (numeroResultat == 3) {

         valeursAttendues.add("CodeRND", "2.3.1.1.12");
         valeursAttendues.add("Denomination",
               "Test 304-Recherche-OK-Complexe-Dates");
         valeursAttendues.add("NumeroRecours", "3");
         valeursAttendues.add("DateCreation", "2005-07-19");

      } else if (numeroResultat == 4) {

         valeursAttendues.add("CodeRND", "2.3.1.1.12");
         valeursAttendues.add("Denomination",
               "Test 304-Recherche-OK-Complexe-Dates");
         valeursAttendues.add("NumeroRecours", "4");
         valeursAttendues.add("DateCreation", "2005-07-20");

      } else if (numeroResultat == 5) {

         valeursAttendues.add("CodeRND", "2.3.1.1.12");
         valeursAttendues.add("Denomination",
               "Test 304-Recherche-OK-Complexe-Dates");
         valeursAttendues.add("NumeroRecours", "5");
         valeursAttendues.add("DateCreation", "2005-07-21");

      } else if (numeroResultat == 6) {

         valeursAttendues.add("CodeRND", "2.3.1.1.12");
         valeursAttendues.add("Denomination",
               "Test 304-Recherche-OK-Complexe-Dates");
         valeursAttendues.add("NumeroRecours", "6");
         valeursAttendues.add("DateCreation", "2005-07-22");

      } else if (numeroResultat == 7) {

         valeursAttendues.add("CodeRND", "2.3.1.1.12");
         valeursAttendues.add("Denomination",
               "Test 304-Recherche-OK-Complexe-Dates");
         valeursAttendues.add("NumeroRecours", "8");
         valeursAttendues.add("DateCreation", "2005-08-19");

      } else if (numeroResultat == 8) {

         valeursAttendues.add("CodeRND", "2.3.1.1.12");
         valeursAttendues.add("Denomination",
               "Test 304-Recherche-OK-Complexe-Dates");
         valeursAttendues.add("NumeroRecours", "9");
         valeursAttendues.add("DateCreation", "2005-08-20");

      } else if (numeroResultat == 9) {

         valeursAttendues.add("CodeRND", "2.3.1.1.12");
         valeursAttendues.add("Denomination",
               "Test 304-Recherche-OK-Complexe-Dates");
         valeursAttendues.add("NumeroRecours", "10");
         valeursAttendues.add("DateCreation", "2005-08-21");

      } else {
         throw new IntegrationRuntimeException("Numéro de résultat "
               + numeroResultat + " inconnu");
      }

      // Renvoi du résultat
      return valeursAttendues;

   }

}
