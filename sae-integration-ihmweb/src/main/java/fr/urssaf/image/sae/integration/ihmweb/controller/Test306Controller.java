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
 * 306-Recherche-OK-RequeteLuceneAvecJokerDansValeurRecherchee
 */
@Controller
@RequestMapping(value = "test306")
public class Test306Controller extends AbstractTestWsController<TestWsRechercheFormulaire> {

   
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
      return "306";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsRechercheFormulaire getFormulairePourGet() {
      
      TestWsRechercheFormulaire formulaire = new TestWsRechercheFormulaire();
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      
      // Requête de recherche correspondant au jeu de test inséré en base d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());
      
      // Les métadonnées que l'on souhaite en retour
      CodeMetadonneeList codesMeta = new CodeMetadonneeList() ;
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
      recherche(
            formulaire.getUrlServiceWeb(),
            formulaire.getRecherche());
   }
   
   
   
   private void recherche(
         String urlServiceWeb,
         RechercheFormulaire formulaire) {
      
      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();
      
      // Résultats attendus
      int nbResultatsAttendus = WAITED_COUNT; 
      boolean flagResultatsTronquesAttendu = false;
      
      // Appel de la méthode de test
      RechercheResponse response = getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            urlServiceWeb,
            formulaire,
            nbResultatsAttendus,
            flagResultatsTronquesAttendu,
            TypeComparaison.NumeroRecours);
      
      // Vérifications en profondeur
      if ((response!=null) && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {
      
         // Tri les résultats par ordre croissant de NumeroRecours
         List<ResultatRechercheType> resultatsTries = 
            Arrays.asList(
                  response.getRechercheResponse().getResultats().getResultat());
         Collections.sort(
               resultatsTries, 
               new ResultatRechercheComparator(
                     TypeComparaison.NumeroRecours));
         
         // Vérifie chaque résultat
         for (int i = 0; i < RETURN_COUNT;i++) {
            
            getRechercheTestService().verifieResultatRecherche(
                  resultatsTries.get(i),
                  Integer.toString(i+1),
                  resultatTest,
                  getValeursAttendues(i+1));
            
         }
         
         // Passe le test en succès si aucune erreur détectée
         if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
            resultatTest.setStatus(TestStatusEnum.Succes);
         }
         
      }
      
   }
   
   
   
 private MetadonneeValeurList getValeursAttendues(int numeroResultat) {
      
      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      
      if (numeroResultat==1) {
         
         valeursAttendues.add("ApplicationProductrice","ADELAIDE");
         valeursAttendues.add("CodeRND","2.3.1.1.12");
         valeursAttendues.add("Denomination","Test 306-Recherche-OK-RequeteLuceneAvecJokerDansValeurRecherchee");
         valeursAttendues.add("NumeroRecours","1");
         
      } else if (numeroResultat==2) {
         
         valeursAttendues.add("ApplicationProductrice","ADELAIDE");
         valeursAttendues.add("CodeRND","2.3.1.1.8");
         valeursAttendues.add("Denomination","Test 306-Recherche-OK-RequeteLuceneAvecJokerDansValeurRecherchee");
         valeursAttendues.add("NumeroRecours","2");
         
      } else if (numeroResultat==3) {
         
         valeursAttendues.add("ApplicationProductrice","ADELAIDE");
         valeursAttendues.add("CodeRND","2.3.1.1.3");
         valeursAttendues.add("Denomination","Test 306-Recherche-OK-RequeteLuceneAvecJokerDansValeurRecherchee");
         valeursAttendues.add("NumeroRecours","3");
         
      } else if (numeroResultat==4) {
         
         valeursAttendues.add("ApplicationProductrice","ADELAIDE");
         valeursAttendues.add("CodeRND","2.3.1.1.12");
         valeursAttendues.add("Denomination","Test 306-Recherche-OK-RequeteLuceneAvecJokerDansValeurRecherchee");
         valeursAttendues.add("NumeroRecours","5");
         
      } else if (numeroResultat==5) {
         
         valeursAttendues.add("ApplicationProductrice","ADELAIDE");
         valeursAttendues.add("CodeRND","2.3.1.1.8");
         valeursAttendues.add("Denomination","Test 306-Recherche-OK-RequeteLuceneAvecJokerDansValeurRecherchee");
         valeursAttendues.add("NumeroRecours","6");
         
      } else if (numeroResultat==6) {
         
         valeursAttendues.add("ApplicationProductrice","ADELAIDE");
         valeursAttendues.add("CodeRND","2.3.1.1.3");
         valeursAttendues.add("Denomination","Test 306-Recherche-OK-RequeteLuceneAvecJokerDansValeurRecherchee");
         valeursAttendues.add("NumeroRecours","7");
         
      } else if (numeroResultat==7) {
         
         valeursAttendues.add("ApplicationProductrice","ADELAIDE");
         valeursAttendues.add("CodeRND","2.3.1.1.12");
         valeursAttendues.add("Denomination","Test 306-Recherche-OK-RequeteLuceneAvecJokerDansValeurRecherchee");
         valeursAttendues.add("NumeroRecours","9");
         
      } else if (numeroResultat==8) {
         
         valeursAttendues.add("ApplicationProductrice","ADELAIDE");
         valeursAttendues.add("CodeRND","2.3.1.1.8");
         valeursAttendues.add("Denomination","Test 306-Recherche-OK-RequeteLuceneAvecJokerDansValeurRecherchee");
         valeursAttendues.add("NumeroRecours","10");
         
      } else {
         throw new IntegrationRuntimeException("Numéro de résultat " + numeroResultat + " inconnu");
      }
      
      
      // Renvoi du résultat
      return valeursAttendues;
      
   }
   
 
}
