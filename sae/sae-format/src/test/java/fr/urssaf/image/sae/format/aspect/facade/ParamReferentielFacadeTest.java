package fr.urssaf.image.sae.format.aspect.facade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.support.facade.ReferentielFormatSupportFacade;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.AbstractReferentielFormatCqlTest;
import fr.urssaf.image.sae.format.utils.Utils;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class ParamReferentielFacadeTest extends AbstractReferentielFormatCqlTest {

  private static final String MESSAGE_ILLEGAL_ARGUMENT = "Une exception IllegalArgumentException aurait dû être levée";
  private static final String MESSAGE_EXCEPT_INCORRECT = "Le message de l'exception est incorrect";

  @Autowired
  private ReferentielFormatSupportFacade refFormatSupport;

  /********************************************************************************************************************************/
  /********************************************************************************************************************************/
  /********************************************************************************************************************************/
  /************** ERREUR --- PARAM SUPPORT *****************************************************/
  /********************************************************************************************************************************/
  @Test
  public void refFormatSupportCreateRefNull() {
    try {
      refFormatSupport.create(null);

      Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
    } catch (final IllegalArgumentException ex) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : ReferentielFormat.",
                    ex.getMessage());
    }
  }

  @Test
  public void refFormatSupportCreateRefFormatParamManquant() {
    try {
      final FormatFichier refFormat = Utils.getRefFormParamObligManquant();

      refFormatSupport.create(refFormat);

      Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
    } catch (final IllegalArgumentException ex) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : ReferentielFormat.",
                    ex.getMessage());
    }
  }

  @Test
  public void refFormatSupportDeleteIdFormatNull()
      throws ReferentielRuntimeException, UnknownFormatException {
    try {
      refFormatSupport.delete(null);

      Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
    } catch (final IllegalArgumentException ex) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : idFormat.",
                    ex.getMessage());
    }
  }

  @Test
  public void refFormatSupportDeleteIdFormatVide()
      throws ReferentielRuntimeException, UnknownFormatException {
    try {
      refFormatSupport.delete("    ");

      Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
    } catch (final IllegalArgumentException ex) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : idFormat.",
                    ex.getMessage());
    }
  }



  @Test
  public void refFormatSupportFindIdFormatNull()
      throws ReferentielRuntimeException, UnknownFormatException {
    try {
      refFormatSupport.find(null);

      Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
    } catch (final IllegalArgumentException ex) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : idFormat.",
                    ex.getMessage());
    }
  }

  @Test
  public void refFormatSupportFindIdFormatVide()
      throws ReferentielRuntimeException, UnknownFormatException {
    try {
      refFormatSupport.find("       ");

      Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
    } catch (final IllegalArgumentException ex) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : idFormat.",
                    ex.getMessage());
    }
  }
}
