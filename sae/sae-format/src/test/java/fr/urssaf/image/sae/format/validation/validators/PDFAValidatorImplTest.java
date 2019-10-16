package fr.urssaf.image.sae.format.validation.validators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.pdfbox.exception.FormatValidationException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorUnhandledException;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;
import fr.urssaf.image.sae.format.validation.validators.pdfa.PdfaValidatorImpl;
import junit.framework.Assert;

/**
 * 
 * TU pour la classe {@link PdfaValidatorImpl}
 * 
 * Rappel : Pour les tests unitaires sur les paramètres, ces derniers sont
 * testés dans le package "aspect"
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
@DirtiesContext
public class PDFAValidatorImplTest {

  @Autowired
  private PdfaValidatorImpl pdfaValidator;

  private final File file = new File(
      "src/test/resources/validation/PdfaValide.pdf");
  private final File docErrone = new File(
      "src/test/resources/validation/Test.word");
  private final File doc = new File("src/test/resources/validation/Test.doc");
  private static final String RESULTAT_ERRONE = "résultat erroné";
  private static final String MESSAGE_ERRONE = "Message erroné";

  @Test
  public void validateFileSuccess() throws FileNotFoundException,
  FormatValidationException, ValidatorUnhandledException {

    final ValidationResult result = pdfaValidator.validateFile(file);

    Assert.assertNotNull(result);

    Assert.assertEquals(RESULTAT_ERRONE, true, result.isValid());
    Assert.assertNull(result.getDetails());
  }

  @Test
  public void validateFileFailureDocErrone() throws FormatValidationException,
  ValidatorUnhandledException {
    try {
      pdfaValidator.validateFile(docErrone);
      Assert.fail("exception attendue");

    } catch (final IllegalArgumentException except) {
      Assert.assertEquals(MESSAGE_ERRONE,
                          "Le fichier passé en paramètre est introuvable.", except
                          .getMessage());
    }
  }

  @Test
  public void validateFileFailure() throws FileNotFoundException,
  FormatValidationException, ValidatorUnhandledException {

    final ValidationResult result = pdfaValidator.validateFile(doc);

    Assert.assertNotNull(result);

    Assert.assertEquals(RESULTAT_ERRONE, false, result.isValid());
    Assert.assertEquals(true, result.getDetails().size() > 0);
  }

  /****************************************************************************/
  /****************************************************************************/
  /***** STREAM */
  /****************************************************************************/
  /****************************************************************************/

  @Test
  public void validateStreamSuccess() throws FormatValidationException,
  IOException, ValidatorUnhandledException {

    final InputStream inputStream = new FileInputStream(file);

    final ValidationResult result = pdfaValidator.validateStream(inputStream);

    Assert.assertNotNull(result);

    Assert.assertEquals(RESULTAT_ERRONE, true, result.isValid());
    Assert.assertNull(result.getDetails());

    inputStream.close();
  }

  @Test(expected = IOException.class)
  public void validateStreamFailureDocErrone()
      throws FormatValidationException, IOException,
      ValidatorUnhandledException {
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(docErrone);
      pdfaValidator.validateStream(inputStream);
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }
  }

  @Test
  public void validateStreamFailure() throws FormatValidationException,
  IOException, ValidatorUnhandledException {

    final InputStream inputStream = new FileInputStream(doc);
    final ValidationResult result = pdfaValidator.validateStream(inputStream);

    Assert.assertNotNull(result);

    Assert.assertEquals(RESULTAT_ERRONE, false, result.isValid());
    Assert.assertEquals(true, result.getDetails().size() > 0);
    inputStream.close();
  }

}
