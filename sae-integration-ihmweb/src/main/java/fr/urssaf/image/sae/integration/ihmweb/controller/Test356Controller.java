package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsRechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;


/**
 * 356-Recherche-KO-MetadonneeRechercheNonAutorisee
 */
@Controller
@RequestMapping(value = "test356")
public class Test356Controller extends AbstractTestWsController<TestWsRechercheFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "356";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsRechercheFormulaire getFormulairePourGet() {
      
      TestWsRechercheFormulaire formulaire = new TestWsRechercheFormulaire();
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      
      // Requête LUCENE avec des métadonnées non utilisables comme critère de recherche
      // (ObjectType, startPage)
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
            ViStyle.VI_OK,   
            "sae_RechercheMetadonneesInterdite",
            new Object[] {"FormatFichier, TailleFichier, VersionRND"});
      
   }
   
}
