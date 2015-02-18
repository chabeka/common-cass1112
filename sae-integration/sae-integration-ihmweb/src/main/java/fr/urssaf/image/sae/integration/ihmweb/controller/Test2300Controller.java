package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheParIterateurFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsRechercheParIterateurFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.IdentifiantPage;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeRangeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;


/**
 * 2300-Recherche-Par-Iterateur
 */
@Controller
@RequestMapping(value = "test2300")
public class Test2300Controller extends AbstractTestWsController<TestWsRechercheParIterateurFormulaire> {
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2300";
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
      metaVal.setCode("Siret");
      metaVal.setValeur("123*");
      metaFixes.add(metaVal);
      formRecherche.setMetaFixes(metaFixes);
      
      MetadonneeRangeValeur metaVariable = new MetadonneeRangeValeur();
      metaVariable.setCode("DateCreation");
      metaVariable.setValeurMin("20150101");
      metaVariable.setValeurMax("20150131");
      formRecherche.setMetaVariable(metaVariable);
      
      formRecherche.setNbDocParPage(100);
      
      CodeMetadonneeList codesMeta = ReferentielMetadonneesService.getMetadonneesExemplePourRecherche();
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
      getRechercheParIterateurTestService().appelWsOpRechercheParIterateurTestLibre(urlWebService, formulaire);
   }
}
