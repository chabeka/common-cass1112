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
 * 308-Recherche-OK-avec-espace-et-joker
 */
@Controller
@RequestMapping(value = "test308")
public class Test308Controller extends
      AbstractTestWsController<TestWsRechercheFormulaire> {

   /**
    * 
    */
   private static final int RETURN_COUNT = 7;
   /**
    * 
    */
   private static final int WAITED_COUNT = 8;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "308";
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
      codesMeta.add("Denomination");
      codesMeta.add("NumeroRecours");

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

         // Passe le test en succès si aucune erreur détectée
         if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
            resultatTest.setStatus(TestStatusEnum.Succes);
         }

      }

   }

   private MetadonneeValeurList getValeursAttendues(int numeroResultat) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      if (numeroResultat >= 1 && numeroResultat <= WAITED_COUNT) {
         valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
         valeursAttendues.add("CodeRND", "2.3.1.1.12");
         valeursAttendues.add("Denomination",
               "Test 308-Recherche-OK-avec-espace-et-joker");

         int value;
         switch (numeroResultat) {
         case 1:
            value = 1;
            break;
         case 2:
            value = 2;
            break;
         case 3:
            value = 3;
            break;
         case 4:
            value = 5;
            break;
         case 5:
            value = 6;
            break;
         case 6:
            value = 7;
            break;
         case 7:
            value = 9;
            break;
         case 8:
            value = 10;
            break;
         default:
            value = 0;
            break;
         }

         valeursAttendues.add("NumeroRecours", String.valueOf(value));

      } else {
         throw new IntegrationRuntimeException("Numéro de résultat "
               + numeroResultat + " inconnu");
      }

      // Renvoi du résultat
      return valeursAttendues;

   }

}
