package fr.urssaf.image.sae.documents.executable.aspect;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.documents.executable.exception.ParametreRuntimeException;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.service.TraitementService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class ParamTraitementTest {

   @Autowired
   private TraitementService traitementService;

   @Test
   public void validIdentifierValiderFichiersParametresNull() {
      try {
         traitementService.identifierValiderFichiers(null);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [parametres].",
                     ex.getMessage());
      }
   }

   @Test
   public void validIdentifierValiderFichiersMetadonneesNonAutorisees() {
      FormatValidationParametres parametres = new FormatValidationParametres();
      parametres.setMetadonnees(Arrays.asList(new String[] { "dco",
            "SM_LIFE_CYCLE_REFERENCE_DATE", "gel", "SM_DIGEST" }));

      try {
         traitementService.identifierValiderFichiers(parametres);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La liste des métadonnées ne doit pas contenir une ou plusieurs métadonnées non autorisées : [dco, SM_LIFE_CYCLE_REFERENCE_DATE, gel, SM_DIGEST].",
                     ex.getMessage());
      }
   }
   
   @Test
   public void validAddMetadatasToDocumentsParametresNull() {
      try {
         traitementService.addMetadatasToDocuments(null);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [parametres].",
                     ex.getMessage());
      }
   }
   
   @Test
   public void validAddMetadatasToDocumentsFromCSVParametresNull() {
      try {
         traitementService.addMetadatasToDocumentsFromCSV(null);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [parametres].",
                     ex.getMessage());
      }
   }
}
