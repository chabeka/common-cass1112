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
 * Test 307-RECHERCHE-OK-META-I-META-NI<br>
 * <br>
 * Recherche sur des métadonnées non indexée et indexée
 */
@Controller
@RequestMapping(value = "test307")
public class Test307Controller extends
      AbstractTestWsController<TestWsRechercheFormulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "307";
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
      codesMeta.add("Siren");
      codesMeta.add("Denomination");
      codesMeta.add("NumeroRecours");
      codesMeta.add("NumeroStructure");

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
      int nbResultatsAttendus = 8;
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
         for (int i = 0; i < 7; i++) {

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

      if (numeroResultat >= 1 && numeroResultat <= 8) {
         valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
         valeursAttendues.add("Siren", "3070000001");
         valeursAttendues.add("Denomination",
               "Test 307-Recherche-OK-Meta-I-Meta-NI");
         valeursAttendues.add("NumeroStructure", "000050221");

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
