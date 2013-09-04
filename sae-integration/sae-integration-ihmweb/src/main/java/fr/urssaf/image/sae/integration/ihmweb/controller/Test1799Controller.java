package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.ModificationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsModificationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;

/**
 * 1799-SAI-Modification-TestLibre
 */
@Controller
@RequestMapping(value = "test1799")
public class Test1799Controller extends
      AbstractTestWsController<TestWsModificationFormulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1799";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsModificationFormulaire getFormulairePourGet() {

      
      TestWsModificationFormulaire formulaire = new TestWsModificationFormulaire();
      
      ModificationFormulaire formModif = formulaire.getModification();
      
      formModif.setIdDocument(UUID.randomUUID());
      
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formModif.setMetadonnees(metadonnees);
      metadonnees.add(new MetadonneeValeur("Siret", "123456"));
      metadonnees.add(new MetadonneeValeur("Siren", StringUtils.EMPTY));
      
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsModificationFormulaire formulaire) {
      modification(formulaire.getUrlServiceWeb(), formulaire
            .getModification());
   }

   private void modification(String urlWebService,
         ModificationFormulaire formulaire) {

      // Appel de la méthode de test
      getModificationTestService().appelWsOpModificationTestLibre(
            urlWebService, formulaire);

   }

}
