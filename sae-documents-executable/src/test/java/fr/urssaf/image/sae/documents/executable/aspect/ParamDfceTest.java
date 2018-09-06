package fr.urssaf.image.sae.documents.executable.aspect;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.sae.documents.executable.exception.ParametreRuntimeException;
import fr.urssaf.image.sae.documents.executable.service.DfceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class ParamDfceTest {

   @Autowired
   private DfceService dfceService;

   @Test
   public void validExecuterRequeteRequeteLuceneNull() throws SearchQueryParseException {
      try {
         dfceService.executerRequete(null);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [requeteLucene].",
                     ex.getMessage());
      }
   }

   @Test
   public void validRecupererContenuDocumentNull() {
      try {
         dfceService.recupererContenu(null);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [document].",
                     ex.getMessage());
      }
   }
   
   @Test
   public void validGetDocumentByIdIdDocNull() {
      try {
         dfceService.getDocumentById(null);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idDoc].",
                     ex.getMessage());
      }
   }
}
