package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test319Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 319-Recherche-OK-Toutes-Metadonnes-Recherchable
 */
@Controller
@RequestMapping(value = "test319")
public class Test319Controller extends
      AbstractTestWsController<Test319Formulaire> {

   /**
    * Nombre d'occurence attendu
    */
   private static final int COUNT_WAITED = 1;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "319";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test319Formulaire getFormulairePourGet() {

      Test319Formulaire formulaire = new Test319Formulaire();

      // List<RechercheFormulaire> rechFormulaireList =
      // formulaire.getRechFormulaireList();

      List<RechercheFormulaire> rechFormulaireList = formulaire
            .getRechFormulaireList();

      CodeMetadonneeList codeMetadonneeList = new CodeMetadonneeList();
      codeMetadonneeList.add("ApplicationProductrice");
      codeMetadonneeList.add("CodeRND");
      codeMetadonneeList.add("Denomination");
      codeMetadonneeList.add("NumeroRecours");

      for (int i = 0; i < getCasTest().getLuceneExempleList().size(); i++) {
         RechercheFormulaire formulaireRecherche = new RechercheFormulaire(
               formulaire);
         formulaireRecherche.setRequeteLucene(getCasTest()
               .getLuceneExempleList().get(i));
         formulaireRecherche.setCodeMetadonnees(codeMetadonneeList);
         rechFormulaireList.add(formulaireRecherche);
      }

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test319Formulaire formulaire) {

      String sousEtape = StringUtils.EMPTY;
      String etape = formulaire.getEtape();
      if (etape.length() > 1) {
         sousEtape = StringUtils.substringAfter(etape, ".");
         etape = etape.substring(0, 1);
      }
      for (RechercheFormulaire f : formulaire.getRechFormulaireList()) {
         f.setParent(formulaire);
      }
      if ("1".equals(etape)) {

         etape3Recherche(formulaire, new Integer(sousEtape));

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape3Recherche(Test319Formulaire formulaire, int sousEtape) {

      // for(RechercheFormulaire rechFormulaire :
      // formulaire.getRechFormulaireList()){

      // Appel le service de test de la recherche
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getRechFormulaireList().get(sousEtape),
                  COUNT_WAITED, false, TypeComparaison.NumeroRecours);

      ResultatTest resultatTest = formulaire.getRechFormulaireList().get(
            sousEtape).getResultats();

      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {

         // Récupère l'unique résultat
         ResultatRechercheType resultatRecherche = response
               .getRechercheResponse().getResultats().getResultat()[0];

         // Le vérifie
         verifieResultatRecherche(resultatRecherche, resultatTest, sousEtape);

         // Si le test n'est pas en échec, alors on peut le passer en succès,
         // car tout a pu être vérifié
         if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
            resultatTest.setStatus(TestStatusEnum.Succes);
         }
      }

      // }

   }

   private void verifieResultatRecherche(
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest,
         int sousEtape) {

      String numeroResultatRecherche = "1";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues = getValeursAttendues(sousEtape);

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }




   private MetadonneeValeurList getValeursAttendues(int numeroResultat) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();


         valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
         valeursAttendues.add("CodeRND", "2.3.1.1.12");
         valeursAttendues.add("Denomination", "Test 319-Recherche-OK-Toutes-Meta-Recherchables");
         valeursAttendues.add("NumeroRecours", "1");

      // Renvoi du résultat
      return valeursAttendues;

   }

}
