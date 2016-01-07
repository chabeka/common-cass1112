package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheParIterateurFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsRechercheParIterateurFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeRangeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;


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
      metaVal.setCode("Denomination");
      metaVal.setValeur("Test 2300-Recherche-Iterateur-OK-Test-Libre");
      metaFixes.add(metaVal);
      formRecherche.setMetaFixes(metaFixes);
      
      MetadonneeRangeValeur metaVariable = new MetadonneeRangeValeur();
      metaVariable.setCode("DateArchivage");
      metaVariable.setValeurMin("20160105");
      metaVariable.setValeurMax("20160106");
      formRecherche.setMetaVariable(metaVariable);
      
      MetadonneeValeurList metaEqualFilter = new MetadonneeValeurList();
      MetadonneeValeur equalFilter = new MetadonneeValeur();
      equalFilter.setCode("DocumentArchivable");
      equalFilter.setValeur("true");
      metaEqualFilter.add(equalFilter);
      formRecherche.setEqualFilter(metaEqualFilter);
      
      MetadonneeValeurList metaNotEqualFilter = new MetadonneeValeurList();
      MetadonneeValeur notEqualFilter = new MetadonneeValeur();
      notEqualFilter.setCode("DocumentArchivable");
      notEqualFilter.setValeur("");
      metaNotEqualFilter.add(notEqualFilter);
      formRecherche.setNotEqualFilter(metaNotEqualFilter);
            
      formRecherche.setNbDocParPage(50);
      
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("Denomination");
      codesMeta.add("DateArchivage");
      codesMeta.add("DocumentArchivable");
      codesMeta.add("NumeroRecours");
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
