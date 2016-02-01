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
 * 2401-ActivationDocumentArchivable-NumeroIdArchivage-Present
 */
@Controller
@RequestMapping(value = "test2401")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test2401Controller extends
      AbstractTestWsController<TestWsRechercheFormulaire> {

   /**
    * 
    */
   private static final int WAITED_COUNT = 5;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2401";
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

      // Les métadonnées que l'on souhaite en retour
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      formRecherche.setCodeMetadonnees(codesMeta);
      codesMeta.add("DocumentArchivable");
      codesMeta.add("NumeroIdArchivage");
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
         verifieResultat2(resultatsTries.get(1), resultatTest);
         verifieResultat3(resultatsTries.get(2), resultatTest);
         verifieResultat4(resultatsTries.get(3), resultatTest);
         verifieResultat5(resultatsTries.get(4), resultatTest);

      }

      // Si pas en échec, alors test en OK (tout a été vérifié)
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

   private void verifieResultat1(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "1";
      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      valeursAttendues.add("NumeroRecours", "1");
      valeursAttendues.add("DocumentArchivable", "true");
      valeursAttendues.add("NumeroIdArchivage","UR227802014064000656");
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);
   }

   private void verifieResultat2(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "2";
      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      valeursAttendues.add("NumeroRecours", "2");
      valeursAttendues.add("DocumentArchivable", "true");
      valeursAttendues.add("NumeroIdArchivage","UR227802014134001136");
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);
   }

   private void verifieResultat3(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "3";
      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      valeursAttendues.add("NumeroRecours", "3");
      valeursAttendues.add("DocumentArchivable", "true");
      valeursAttendues.add("NumeroIdArchivage","UR227802014113000848");
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);
   }

   private void verifieResultat4(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "4";
      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      valeursAttendues.add("NumeroRecours", "4");
      valeursAttendues.add("DocumentArchivable", "true");
      valeursAttendues.add("NumeroIdArchivage","UR227802014106000842");
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);
   }

   private void verifieResultat5(ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest) {

      String numeroResultatRecherche = "5";
      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      valeursAttendues.add("NumeroRecours", "5");
      valeursAttendues.add("DocumentArchivable", "true");
      valeursAttendues.add("NumeroIdArchivage","UR227802014078000493");
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);
   }

}
