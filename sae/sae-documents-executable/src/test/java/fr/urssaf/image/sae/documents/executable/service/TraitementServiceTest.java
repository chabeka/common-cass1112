package fr.urssaf.image.sae.documents.executable.service;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres.MODE_VERIFICATION;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class TraitementServiceTest {

   @Autowired
   private TraitementService traitementService;
   
   private FormatValidationParametres createParametres(String requeteLucene, MODE_VERIFICATION mode) {
      FormatValidationParametres parametres = new FormatValidationParametres();
      parametres.setModeVerification(mode);
      parametres.setRequeteLucene(requeteLucene);
      parametres.setNombreMaxDocs(10);
      parametres.setTaillePasExecution(5);
      parametres.setTaillePool(1);
      parametres.setTempsMaxTraitement(0);
      parametres.setMetadonnees(new ArrayList<String>());
      return parametres;
   }
   
   @Test
   public void identifierValiderFichiersIdentification() {
      
      // cas d'identification valide
      FormatValidationParametres parametres = createParametres("iti:73132b50-d404-11e2-9df1-005056c00008", 
            MODE_VERIFICATION.IDENTIFICATION);
      traitementService.identifierValiderFichiers(parametres);
      
      // cas d'identification non valide
      parametres = createParametres("srt:41882050200023", 
            MODE_VERIFICATION.IDENTIFICATION);
      traitementService.identifierValiderFichiers(parametres);
   }

   @Test
   public void identifierValiderFichiersValidation() {
      
      // cas de validation (document non valide)
      FormatValidationParametres parametres = createParametres("iti:73132b50-d404-11e2-9df1-005056c00008", 
            MODE_VERIFICATION.VALIDATION);
      traitementService.identifierValiderFichiers(parametres);
      
      // cas de validation (unknown format)
      parametres = createParametres("srt:41882050200023", 
            MODE_VERIFICATION.VALIDATION);
      traitementService.identifierValiderFichiers(parametres);
   }
   
   @Test
   public void identifierValiderFichiersIdentValidation() {
      
      // cas d'identification et validation (document non valide)
      FormatValidationParametres parametres = createParametres("iti:73132b50-d404-11e2-9df1-005056c00008", 
            MODE_VERIFICATION.IDENT_VALID);
      traitementService.identifierValiderFichiers(parametres);
      
      // cas d'identification et validation (unknown format)
      parametres = createParametres("srt:41882050200023", 
            MODE_VERIFICATION.IDENT_VALID);
      traitementService.identifierValiderFichiers(parametres);
   }
}
