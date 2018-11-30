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
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;


/**
 * 2351-Recherche-Iterateur-KO-MetadonneeRechercheInexistante
 */
@Controller
@RequestMapping(value = "test2356")
public class Test2356Controller extends AbstractTestWsController<TestWsRechercheParIterateurFormulaire> {
  
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2356";
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
      metaVal.setValeur("Test 2356-Recherche-Iterateur-KO-MemeMeta2filtres");
     
      metaFixes.add(metaVal);
      formRecherche.setMetaFixes(metaFixes);
      
      MetadonneeRangeValeur metaVariable = new MetadonneeRangeValeur();
      metaVariable.setCode("DateCreation");
      metaVariable.setValeurMin("20110907");
      metaVariable.setValeurMax("20110908");
      formRecherche.setMetaVariable(metaVariable);
      
      MetadonneeValeurList metaEqualFilter = new MetadonneeValeurList();
      MetadonneeValeur equalFilter = new MetadonneeValeur();
      equalFilter.setCode("NumeroRecours");
      equalFilter.setValeur("34");
      metaEqualFilter.add(equalFilter);
      formRecherche.setEqualFilter(metaEqualFilter);
      
      MetadonneeValeurList metaNotEqualFilter = new MetadonneeValeurList();
      MetadonneeValeur notEqualFilter = new MetadonneeValeur();
      notEqualFilter.setCode("NumeroRecours");
      notEqualFilter.setValeur("137");
      metaNotEqualFilter.add(notEqualFilter);
      formRecherche.setNotEqualFilter(metaNotEqualFilter);

      formRecherche.setNbDocParPage(200);
      
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("Denomination");
      codesMeta.add("DateArchivage");
      codesMeta.add("DocumentArchivable");
      codesMeta.add("NumeroRecours");
      
      formRecherche.setCodeMetadonnees(codesMeta);
      
      //Pour initialiser le flag à côté du résultat cf NonLance
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
   
   private void recherche(String urlServiceWeb, RechercheParIterateurFormulaire formulaire) {
      
      // Appel de la méthode de test
      getRechercheParIterateurTestService().appelWsOpRechercheParIterateurSoapFault(
            urlServiceWeb, formulaire, ViStyle.VI_OK,
            "sae_RechercheMetadonneesDoublons",
            new Object[] {"NumeroRecours"}
            );
   }
}
