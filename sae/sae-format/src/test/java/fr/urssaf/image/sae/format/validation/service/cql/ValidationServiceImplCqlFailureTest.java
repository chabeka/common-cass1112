package fr.urssaf.image.sae.format.validation.service.cql;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentificationRuntimeException;
import fr.urssaf.image.sae.format.utils.AbstractReferentielFormatCqlFailureTest;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorUnhandledException;
import fr.urssaf.image.sae.format.validation.service.impl.ValidationServiceImpl;
import org.junit.Assert;

/**
 * 
 * Classe testant les services de la classe {@link ValidationServiceImpl}
 * 
 * Rappel : Pour les tests unitaires sur les paramètres, ces derniers sont
 * testés dans le package "aspect"
 */
@RunWith(SpringJUnit4ClassRunner.class)
// @DirtiesContext
public class ValidationServiceImplCqlFailureTest extends AbstractReferentielFormatCqlFailureTest {

  @Autowired
  private ValidationServiceImpl validationService;


  private final File file = new File(
                                     "src/test/resources/validation/PdfaValide.pdf");

  private static final String MESS_EXCEPT_ERRONE = "Le message de l'exception est incorrect";
  private static final String FMT_354 = "fmt/354";

  @Test
  public void valideServiceFailureBeanIntrouvable()
      throws IdentificationRuntimeException, UnknownFormatException,
      IOException, ValidatorUnhandledException {

    try {
      validationService.validateFile(FMT_354, file);
      Assert
      .fail("Une exception IdentifierInitialisationException aurait dû être levée");

    } catch (final ValidatorInitialisationException ex) {
      Assert
      .assertEquals(
                    MESS_EXCEPT_ERRONE,
                    "Il n'est pas possible de récupérer une instance du validateur.",
                    ex.getMessage());
    }

  }

}
