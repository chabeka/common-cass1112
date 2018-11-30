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
 * 316-Recherche-OK-ValeurMetaIdentiqueNomMeta-DateCreation
 */
@Controller
@RequestMapping(value = "test316")
public class Test316Controller extends
      AbstractTestWsController<TestWsRechercheFormulaire> {

   /**
    * 
    */
   private static final int WAITED_COUNT = 1;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "316";
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
         verifieResultat1(resultatsTries.get(0), resultatTest);

         // Passe le test en "à contrôler" si aucune erreur détectée
         // Le test n'est pas passé en succès car il faudrait mettre à jour
         //  le mécanisme de vérification des métadonnées pour la gestion 
         //  des doubles. Le OK n'est donc pas fiable dans ce cas d'utilisation
         if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
            resultatTest.setStatus(TestStatusEnum.AControler);
         }

      }

   }

   
   
   
   private void verifieResultat1(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "1";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("Denomination", "Test 316-Recherche-OK-ValeurMetaIdentiqueNomMeta-DateCreation");
      valeursAttendues.add("NumeroRecours", "1");

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }
   
   

}
