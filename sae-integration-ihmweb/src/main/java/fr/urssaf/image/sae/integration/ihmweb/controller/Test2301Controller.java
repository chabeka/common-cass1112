package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheParIterateurFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsRechercheParIterateurFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeRangeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheParIterateurResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;


/**
 * 2300-Recherche-Par-Iterateur
 */
@Controller
@RequestMapping(value = "test2301")
public class Test2301Controller extends AbstractTestWsController<TestWsRechercheParIterateurFormulaire> {
   
   /**
    * 
    */
   private static final int WAITED_COUNT = 139;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2301";
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsRechercheParIterateurFormulaire getFormulairePourGet() {
      
      TestWsRechercheParIterateurFormulaire formulaire = new TestWsRechercheParIterateurFormulaire();
      RechercheParIterateurFormulaire formRecherche = formulaire.getRecherche();
      
      // Des métadonnées exemples
       
      MetadonneeValeurList metaFixes = new MetadonneeValeurList();
      MetadonneeValeur metaVal = new MetadonneeValeur();
      metaVal.setCode("Denomination");
      metaVal.setValeur("Test 2301-Recherche-Iterateur-OK-Test-Simple");
      metaFixes.add(metaVal);
      formRecherche.setMetaFixes(metaFixes);
      
      MetadonneeRangeValeur metaVariable = new MetadonneeRangeValeur();
      metaVariable.setCode("DateCreation");
      metaVariable.setValeurMin("20110907");
      metaVariable.setValeurMax("20110908");
      formRecherche.setMetaVariable(metaVariable);
      
      MetadonneeValeurList metaEqualFilter = new MetadonneeValeurList();
      MetadonneeValeur equalFilter = new MetadonneeValeur();
      equalFilter.setCode("DocumentArchivable");
      equalFilter.setValeur("true");
      metaEqualFilter.add(equalFilter);
      formRecherche.setEqualFilter(metaEqualFilter);
      
      MetadonneeValeurList metaNotEqualFilter = new MetadonneeValeurList();
      MetadonneeValeur notEqualFilter = new MetadonneeValeur();
      notEqualFilter.setCode("NumeroRecours");
      notEqualFilter.setValeur("5");
      metaNotEqualFilter.add(notEqualFilter);
      formRecherche.setNotEqualFilter(metaNotEqualFilter);
            
      formRecherche.setNbDocParPage(150);
      
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("Denomination");
      codesMeta.add("DateArchivage");
      codesMeta.add("DocumentArchivable");
      codesMeta.add("NumeroRecours");
      codesMeta.add("DateCreation");
      formRecherche.setCodeMetadonnees(codesMeta);
      
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      return formulaire;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsRechercheParIterateurFormulaire form) {
      recherche(form.getUrlServiceWeb(),form.getRecherche());
   }
   
   private void recherche(String urlWebService, RechercheParIterateurFormulaire formulaire) {

      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

      // Résultats attendus
      int nbResultatsAttendus = WAITED_COUNT;

      // Appel de la méthode de test
      RechercheParIterateurResponse response = getRechercheParIterateurTestService()
            .appelWsOpRechercheParIterateurReponseCorrecteAttendue(urlWebService,
                  formulaire, nbResultatsAttendus, TypeComparaison.NumeroRecours, null);

      // Vérifications en profondeur
      if ((response != null)
            && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {

         // Tri les résultats par ordre croissant de NumeroRecours
         List<ResultatRechercheType> resultatsTries = Arrays.asList(response
               .getRechercheParIterateurResponse().getResultats().getResultat());
         Collections.sort(resultatsTries, new ResultatRechercheComparator(
               TypeComparaison.NumeroRecours));

         // Pas de vérification pour chaque résultat
//         for (int i = 0; i < RETURN_COUNT; i++) {
//
//            getRechercheTestService().verifieResultatRecherche(
//                  resultatsTries.get(i), Integer.toString(i + 1), resultatTest,
//                  getValeursAttendues(i + 1));
//
//         }

         // Passe le test en succès si aucune erreur détectée
         if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
            resultatTest.setStatus(TestStatusEnum.Succes);
         }
      }
  }
}
