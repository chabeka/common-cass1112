package fr.urssaf.image.sae.format.aspect;

import java.io.File;
import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.pdfbox.exception.FormatValidationException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidationRuntimeException;
import fr.urssaf.image.sae.format.validation.validators.Validator;
import fr.urssaf.image.sae.format.validation.validators.pdfa.PdfaValidatorImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class ParamValidationTest {

   private static final String MESSAGE_REF_RUNTIME = "Une exception ReferentielRuntimeException aurait dû être levée";
   private static final String MESSAGE_EXCEPT_INCORRECT = "Le message de l'exception est incorrect";
   private static final String TEST = "test";
   private static final String FICHIER_OBLIG_NUL = "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [fichier].";
   private static final String STREAM_OBLIG_NUL = "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [stream].";

   @Autowired
   private Validator validator;

   @Autowired
   private PdfaValidatorImpl pdfaValidator;


   /********************************************************************************************************************************/
   /********************************************************************************************************************************/
   /********************************************************************************************************************************/
   /************** ERREUR --- PARAM VALIDATOR ***************************************************************/
   /********************************************************************************************************************************/
   @Test
   public void validatorValidateFileFileNull()
         throws ValidationRuntimeException, FileNotFoundException,
         FormatValidationException {
      try {
         validator.validateFile(null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     FICHIER_OBLIG_NUL,
                     ex.getMessage());
      }
   }

   @Test
   public void validatorValidateFileFileNotExist()
         throws ValidationRuntimeException, FileNotFoundException,
         FormatValidationException {
      try {
         File fichierTest = new File(TEST);
         validator.validateFile(fichierTest);
         Assert
               .fail("Une exception FileNotFoundException aurait dû être levée");
      } catch (IllegalArgumentException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT,
               "Le fichier pass\u00E9 en param\u00E8tre est introuvable.", ex
                     .getMessage());
      }
   }

   @Test
   public void validatorValidateStreamNull() throws ValidationRuntimeException,
         FormatValidationException {
      try {
         validator.validateStream(null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     STREAM_OBLIG_NUL,
                     ex.getMessage());
      }
   }

   /********************************************************************************************************************************/
   /********************************************************************************************************************************/
   /********************************************************************************************************************************/
   /************** ERREUR --- PARAM PDFAVALIDATORIMPL *******************************************************/
   /********************************************************************************************************************************/

   @Test
   public void pdfaValidatorValidateFileFileNull()
         throws ValidationRuntimeException, FileNotFoundException,
         FormatValidationException {
      try {
         pdfaValidator.validateFile(null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     FICHIER_OBLIG_NUL,
                     ex.getMessage());
      }
   }

   @Test
   public void pdfaValidatorValidateFileFileNotExist()
         throws ValidationRuntimeException, FileNotFoundException,
         FormatValidationException {
      try {
         File fichierTest = new File(TEST);
         pdfaValidator.validateFile(fichierTest);
         Assert
               .fail("Une exception FileNotFoundException aurait dû être levée");
      } catch (IllegalArgumentException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT,
               "Le fichier pass\u00E9 en param\u00E8tre est introuvable.", ex
                     .getMessage());
      }
   }

   @Test
   public void pdfaValidatorValidateStreamNull()
         throws ValidationRuntimeException, FormatValidationException {
      try {
         pdfaValidator.validateStream(null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     STREAM_OBLIG_NUL,
                     ex.getMessage());
      }
   }

}
