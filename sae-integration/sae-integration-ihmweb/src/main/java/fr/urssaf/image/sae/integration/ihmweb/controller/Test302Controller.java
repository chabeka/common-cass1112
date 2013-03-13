package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test302Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

/**
 * 302-Recherche-OK-Tronquee
 */
@Controller
@RequestMapping(value = "test302")
public class Test302Controller extends
      AbstractTestWsController<Test302Formulaire> {

   /**
    * 
    */
   private static final int WAITED_COUNT_RECHERCHE = 200;
   private static final int WAITED_COUNT_COMPTAGE = 250;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "302";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test302Formulaire getFormulairePourGet() {

      Test302Formulaire formulaire = new Test302Formulaire();
      RechercheFormulaire formRecherche = formulaire.getRechFormulaire();
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Pas de métadonnées spécifiques à récupérer
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      formRecherche.setCodeMetadonnees(codesMeta);

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test302Formulaire formulaire) {
      
      String etape = formulaire.getEtape();
      if("1".equals(etape)){
         recherche(formulaire.getUrlServiceWeb(), formulaire.getRechFormulaire());
      }else if("2".equals(etape)){
         comptages(formulaire);
      }
   }

   private void recherche(String urlServiceWeb, RechercheFormulaire formulaire) {

      // Résultats attendus
      int nbResultatsAttendus = WAITED_COUNT_RECHERCHE;
      boolean flagResultatsTronquesAttendu = true;

      // Appel de la méthode de test
      getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            urlServiceWeb, formulaire, nbResultatsAttendus,
            flagResultatsTronquesAttendu, null);

      if (!TestStatusEnum.Echec.equals(formulaire.getResultats().getStatus())) {
         formulaire.getResultats().setStatus(TestStatusEnum.Succes);
      }

   }
   
   private void comptages(Test302Formulaire formulaire) {

      // Récupération de l'objet ResultatTest
      ResultatTest resultatTest = formulaire.getComptagesFormulaire()
            .getResultats();
      resultatTest.clear();

      // Lecture de l'identifiant du traitement de masse
      String idTdm = formulaire.getComptagesFormulaire().getIdTdm();

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest,
            new Long(WAITED_COUNT_COMPTAGE));

      // Passe le test en OK si pas KO
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

}
