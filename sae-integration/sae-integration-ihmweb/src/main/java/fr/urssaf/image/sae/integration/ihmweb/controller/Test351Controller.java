package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsRechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.utils.ViUtils;


/**
 * Test 351-Recherche-KO-RequeteVide<br>
 * <br>
 * On vérifie que la recherche renvoie la bonne erreur lorsque la requête de recherche est vide
 */
@Controller
@RequestMapping(value = "test351")
public class Test351Controller extends AbstractTestWsController<TestWsRechercheFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "351";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsRechercheFormulaire getFormulairePourGet() {
      
      TestWsRechercheFormulaire formulaire = new TestWsRechercheFormulaire();
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      
      // Requête vide (un espace)
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());
      
      // CodeMetadonneeList codesMeta = ReferentielMetadonneesService.getMetadonneesExemplePourRecherche();
      CodeMetadonneeList codesMeta = new CodeMetadonneeList(); // <= liste des métadonnées vides
      formRecherche.setCodeMetadonnees(codesMeta);
      
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);
      
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
      
      // Appel de la méthode de test
      this.getRechercheTestService().appelWsOpRechercheSoapFault(
            urlServiceWeb, 
            formulaire,
            ViUtils.FIC_VI_OK,
            "sae_RequeteLuceneVideOuNull",
            null);
      
   }
   
}
