package fr.urssaf.image.sae.format.validation.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.schlichtherle.io.FileInputStream;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.service.impl.ValidationServiceImpl;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;

/**
 * 
 * TU pour la classe {@link ValidationServiceImpl}
 * 
 * Rappel : Pour les tests unitaires sur les paramètres, ces derniers sont
 * testés dans le package "aspect"
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class ValidationServiceImplTest {

   @Autowired
   private ValidationServiceImpl validationService;

   private final File file = new File(
         "src/test/ressources/validation/PdfaValide.pdf");
   private final File docErrone = new File(
         "src/test/ressources/validation/Test.word");
   private final File doc = new File("src/test/ressources/validation/Test.doc");
   private static final String RESULTAT_ERRONE = "résultat erroné";
   private static final String MESSAGE_ERRONE = "Message erroné";
   private static final String FMT354 = "fmt/354";

   @Test
   public void validationFileSuccess() throws FileNotFoundException,
         UnknownFormatException, ValidatorInitialisationException {

      ValidationResult result = validationService.validateFile(FMT354, file);

      Assert.assertNotNull(result);

      Assert.assertEquals(RESULTAT_ERRONE, true, result.isValid());
      Assert.assertNull(result.getDetails());
   }

   @Test
   public void validateFileFailureDocErrone() throws UnknownFormatException,
         ValidatorInitialisationException {
      try {
         validationService.validateFile(FMT354, docErrone);
      } catch (FileNotFoundException except) {
         Assert.assertEquals(MESSAGE_ERRONE,
               "Le fichier passé en paramètre est introuvable.", except
                     .getMessage());
      }
   }

   @Test
   public void validateFileFailure() throws FileNotFoundException,
         UnknownFormatException, ValidatorInitialisationException {

      ValidationResult result = validationService.validateFile(FMT354, doc);

      Assert.assertNotNull(result);

      Assert.assertEquals(RESULTAT_ERRONE, false, result.isValid());
      Assert.assertEquals(true, result.getDetails().size() > 0);
   }

   @Test
   public void validationFileFormatInexistant() throws FileNotFoundException,
         ValidatorInitialisationException {

      try {
         validationService.validateFile(FMT354, doc);
      } catch (UnknownFormatException except) {
         Assert.assertEquals(MESSAGE_ERRONE,
               "Aucun format n'a été trouvé avec l'identifiant : 34", except
                     .getMessage());
      }
   }

//   @Ignore
//   @Test
//   public void validateFileValidatorIntrouvable() throws FileNotFoundException,
//         FormatValidationException {
//      // un test avec une erreur dans le validator à bien été fait. -> faire le
//      // test et modifier le dataSet
//      // try {
//      // pdfaValidator.validateFile(file);
//      // } catch (ValidatorInitialisationException except) {
//      // Assert.assertEquals(MESSAGE_ERRONE,
//      // "Impossible d'initialiser le validateur.", except.getMessage());
//      // }
//   }

   /******************************************************/
   /******************************************************/
   /*** STREAM ********************************************/
   /******************************************************/
   @Test
   public void validationStreamSuccess() throws UnknownFormatException,
         ValidatorInitialisationException, IOException {

      InputStream inputStream = new FileInputStream(file);
      ValidationResult result = validationService.validateStream(FMT354,
            inputStream);

      Assert.assertNotNull(result);

      Assert.assertEquals(RESULTAT_ERRONE, true, result.isValid());
      Assert.assertNull(result.getDetails());
   }

   @Test
   public void validateStreamFailureDocErrone() throws UnknownFormatException,
         ValidatorInitialisationException, IOException {
      InputStream inputStream = null;
      try {
         inputStream = new FileInputStream(docErrone);
         validationService.validateStream(FMT354, inputStream);
      } catch (FileNotFoundException except) {
         Assert.assertEquals(MESSAGE_ERRONE,
               "src\\test\\ressources\\validation\\Test.word (Le fichier spécifié est introuvable)", except
                     .getMessage());
      }
   }

   @Test
   public void validateStreamFailure() throws UnknownFormatException, ValidatorInitialisationException, IOException  {

      InputStream inputStream = new FileInputStream(doc);
      ValidationResult result = validationService
            .validateStream(FMT354, inputStream);

      Assert.assertNotNull(result);

      Assert.assertEquals(RESULTAT_ERRONE, false, result.isValid());
      Assert.assertEquals(true, result.getDetails().size() > 0);
   }

   @Test
   public void validationStreamFormatInexistant() throws ValidatorInitialisationException, IOException  {

      try {
         InputStream inputStream = new FileInputStream(doc);
         validationService.validateStream(FMT354, inputStream);
      } catch (UnknownFormatException except) {
         Assert.assertEquals(MESSAGE_ERRONE,
               "Aucun format n'a été trouvé avec l'identifiant : 34", except
                     .getMessage());
      }
   }

}
