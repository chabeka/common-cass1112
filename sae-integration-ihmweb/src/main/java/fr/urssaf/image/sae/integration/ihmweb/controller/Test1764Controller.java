package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.ModificationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsModificationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;


/**
 * 1799-SAI-Modification-TestLibre
 */
@Controller
@RequestMapping(value = "test1764")
public class Test1764Controller extends
      AbstractTestWsController<TestWsModificationFormulaire> {

   private static final String ID_ARCHIVE_TEST = 
      "00000000-0000-0000-0000-000000000000";
  
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1764";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsModificationFormulaire getFormulairePourGet() {

      
      TestWsModificationFormulaire formulaire = new TestWsModificationFormulaire();
      
      ModificationFormulaire formModif = formulaire.getModification();
      
      formModif.setIdDocument(UUID.fromString(ID_ARCHIVE_TEST));
      
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formModif.setMetadonnees(metadonnees);
      metadonnees.add(new MetadonneeValeur("Siret", "123456"));
      
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
      getModificationTestService().appelWsOpModificationSoapFault(
            urlWebService, formulaire, ViStyle.VI_OK,
            "sae_ModificationArchiveNonTrouvee",
            new Object[] {formulaire.getIdDocument().toString()});     
 
   }

}
