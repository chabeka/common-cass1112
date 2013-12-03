package fr.urssaf.image.sae.format.aspect;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.UtilsTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class ParamReferentielTest {

   private static final String MESSAGE_ILLEGAL_ARGUMENT = "Une exception IllegalArgumentException aurait dû être levée";
   private static final String MESSAGE_EXCEPT_INCORRECT = "Le message de l'exception est incorrect";

   @Autowired
   private ReferentielFormatSupport refFormatSupport;

   /********************************************************************************************************************************/
   /********************************************************************************************************************************/
   /********************************************************************************************************************************/
   /************** ERREUR --- PARAM SUPPORT *****************************************************/
   /********************************************************************************************************************************/
   @Test
   public void refFormatSupportCreateRefClockNull() {
      try {
         refFormatSupport.create(null, null);

         Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [ReferentielFormat, clock].",
                     ex.getMessage());
      }
   }

   @Test
   public void refFormatSupportCreateClockNull() {
      try {
         FormatFichier refFormat = UtilsTest.genererRefFormatPdfa();

         refFormatSupport.create(refFormat, null);

         Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [clock].",
                     ex.getMessage());
      }
   }

   @Test
   public void refFormatSupportCreateRefFormatParamManquant() {
      try {
         FormatFichier refFormat = UtilsTest.getRefFormParamObligManquant();

         refFormatSupport.create(refFormat, Long.valueOf(15));

         Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, description].",
                     ex.getMessage());
      }
   }

   @Test
   public void refFormatSupportDeleteIdFormatClockNull()
         throws ReferentielRuntimeException, UnknownFormatException {
      try {
         refFormatSupport.delete(null, null);

         Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, clock].",
                     ex.getMessage());
      }
   }

   @Test
   public void refFormatSupportDeleteIdFormatVide()
         throws ReferentielRuntimeException, UnknownFormatException {
      try {
         refFormatSupport.delete("    ", Long.valueOf(15));

         Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat].",
                     ex.getMessage());
      }
   }

   @Test
   public void refFormatSupportDeleteClockVide()
         throws ReferentielRuntimeException, UnknownFormatException {
      try {
         refFormatSupport.delete("idFormat", null);

         Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [clock].",
                     ex.getMessage());
      }
   }

   @Test
   public void refFormatSupportFindIdFormatNull()
         throws ReferentielRuntimeException, UnknownFormatException {
      try {
         refFormatSupport.find(null);

         Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat].",
                     ex.getMessage());
      }
   }

   @Test
   public void refFormatSupportFindIdFormatVide()
         throws ReferentielRuntimeException, UnknownFormatException {
      try {
         refFormatSupport.find("       ");

         Assert.fail(MESSAGE_ILLEGAL_ARGUMENT);
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat].",
                     ex.getMessage());
      }
   }
}
