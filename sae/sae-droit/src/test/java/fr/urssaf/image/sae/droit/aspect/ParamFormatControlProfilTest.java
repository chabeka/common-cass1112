package fr.urssaf.image.sae.droit.aspect;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;

/**
 * 
 * Classe Test de la classe {@link ParamPagmf}
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class ParamFormatControlProfilTest {

   private static final String MESSAGE_PARAM_FORMAT_RUNTIME = "Une exception IllegalArgumentException aurait dû être levée";
   private static final String MESSAGE_EXCEPT_INCORRECT = "Le message de l'exception est incorrect";
   
   private static final String DESCRIPTION = "description";
   private static final String CODE = "code";
   
   private static final String MESSAGE_EXCEPT = "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : ";
   private static final String FORMAT_PROFIL = "[formatProfile].";

   @Autowired
   private FormatControlProfilSupport formatControlProfilSupport;

   /********************************************************************************************************************************/
   /********************************************************************************************************************************/
   /********************************************************************************************************************************/
   /************** ERREUR --- PARAM FormatControlProfilSupport *****************************************************/
   /********************************************************************************************************************************/
   @Test
   public void createFormatNull() {
      try {
         long clock = 1;
         formatControlProfilSupport.create(null, clock);
         Assert.fail(MESSAGE_PARAM_FORMAT_RUNTIME);
      } catch (IllegalArgumentException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [profil].",
                     except.getMessage());
      }
   }

   @Test
   public void createFormatVideClockNull() {
      try {
         FormatControlProfil format = new FormatControlProfil();
         formatControlProfilSupport.create(format, Long.valueOf(-1));
         Assert.fail(MESSAGE_PARAM_FORMAT_RUNTIME);
      } catch (IllegalArgumentException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [clock, codeProfil, description, controlProfil].",
                     except.getMessage());
      }
   }

   @Test
   public void createFailureCodeFormatNull() {
      try {
         FormatControlProfil format = new FormatControlProfil();
         format.setDescription(DESCRIPTION);
         FormatProfil formatProfil = new FormatProfil();
         formatProfil.setFormatIdentification(true);
         formatProfil.setFormatValidation(true);
         formatProfil.setFormatValidationMode("Monitor");

         format.setControlProfil(formatProfil);
         formatControlProfilSupport.create(format, Long.valueOf(1));

         Assert.fail(MESSAGE_PARAM_FORMAT_RUNTIME);
      } catch (IllegalArgumentException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [codeProfil].",
                     except.getMessage());
      }
   }

   @Test
   public void createFailureValidationModeErrone() {
      try {
         FormatControlProfil format = new FormatControlProfil();
         format.setDescription(DESCRIPTION);
         format.setFormatCode(CODE);

         FormatProfil formatProfil = new FormatProfil();
         formatProfil.setFormatIdentification(true);
         formatProfil.setFormatValidation(false);
         formatProfil.setFormatValidationMode("Monitor");

         format.setControlProfil(formatProfil);
         formatControlProfilSupport.create(format, Long.valueOf(1));

         Assert.fail(MESSAGE_PARAM_FORMAT_RUNTIME);
      } catch (IllegalArgumentException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "Le mode de validation ne doit pas être renseigné.",
                     except.getMessage());
      }
   }

   @Test
   public void createFailureValidationModeErrone2() {
      try {
         FormatControlProfil format = new FormatControlProfil();
         format.setDescription(DESCRIPTION);
         format.setFormatCode(CODE);

         FormatProfil formatProfil = new FormatProfil();
         formatProfil.setFormatIdentification(true);
         formatProfil.setFormatValidation(true);
         formatProfil.setFormatValidationMode("Moni");

         format.setControlProfil(formatProfil);
         formatControlProfilSupport.create(format, Long.valueOf(1));

         Assert.fail(MESSAGE_PARAM_FORMAT_RUNTIME);
      } catch (IllegalArgumentException except) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT,
               "Le mode de validation n'est pas STRICT ou MONITOR.", except
                     .getMessage());
      }
   }

   @Test
   public void createFailureValidationModeErrone3() {
      try {
         FormatControlProfil format = new FormatControlProfil();
         format.setDescription(DESCRIPTION);
         format.setFormatCode(CODE);

         FormatProfil formatProfil = new FormatProfil();
         formatProfil.setFormatIdentification(true);
         formatProfil.setFormatValidation(false);
         formatProfil.setFormatValidationMode("Monitor");

         format.setControlProfil(formatProfil);
         formatControlProfilSupport.create(format, Long.valueOf(1));

         Assert.fail(MESSAGE_PARAM_FORMAT_RUNTIME);
      } catch (IllegalArgumentException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "Le mode de validation ne doit pas être renseigné.",
                     except.getMessage());
      }
   }

   @Test
   public void createSuccessValidationModeFalse()
         throws FormatControlProfilNotFoundException {

      FormatControlProfil format = new FormatControlProfil();
      format.setDescription(DESCRIPTION);
      format.setFormatCode(CODE);

      FormatProfil formatProfil = new FormatProfil();
      formatProfil.setFormatIdentification(true);
      formatProfil.setFormatValidation(false);
      formatProfil.setFormatValidationMode(null);
      format.setControlProfil(formatProfil);

      formatControlProfilSupport.create(format, Long.valueOf(1));
      Assert.assertNotNull(formatControlProfilSupport.find(CODE));
      
   }

   @Test
   public void createSuccessValidationModeNONE()
         throws FormatControlProfilNotFoundException {

      FormatControlProfil format = new FormatControlProfil();
      format.setDescription(DESCRIPTION);
      format.setFormatCode(CODE);

      FormatProfil formatProfil = new FormatProfil();
      formatProfil.setFormatIdentification(true);
      formatProfil.setFormatValidation(false);
      formatProfil.setFormatValidationMode("NONE");
      format.setControlProfil(formatProfil);

      formatControlProfilSupport.create(format, Long.valueOf(1));
      Assert.assertNotNull(formatControlProfilSupport.find(CODE));
   }

   @Test
   public void deleteCodeFormatNull()
         throws FormatControlProfilNotFoundException {
      try {
         formatControlProfilSupport.delete(null, Long.valueOf(1));
         Assert.fail(MESSAGE_PARAM_FORMAT_RUNTIME);
      } catch (IllegalArgumentException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     MESSAGE_EXCEPT + FORMAT_PROFIL,
                     except.getMessage());
      }
   }

   @Test
   public void deleteCodeFormatVide()
         throws FormatControlProfilNotFoundException {
      try {
         formatControlProfilSupport.delete("", Long.valueOf(1));
         Assert.fail(MESSAGE_PARAM_FORMAT_RUNTIME);
      } catch (IllegalArgumentException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     MESSAGE_EXCEPT + FORMAT_PROFIL,
                     except.getMessage());
      }
   }

   @Test
   public void deleteCodeFormatEspace()
         throws FormatControlProfilNotFoundException {
      try {
         formatControlProfilSupport.delete("     ", Long.valueOf(1));
         Assert.fail(MESSAGE_PARAM_FORMAT_RUNTIME);
      } catch (IllegalArgumentException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     MESSAGE_EXCEPT + FORMAT_PROFIL,
                     except.getMessage());
      }
   }

   @Test
   public void findCodeFormatNull() {
      try {
         formatControlProfilSupport.find(null);
         Assert.fail(MESSAGE_PARAM_FORMAT_RUNTIME);
      } catch (IllegalArgumentException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     MESSAGE_EXCEPT + FORMAT_PROFIL,
                     except.getMessage());
      }
   }

   @Test
   public void findCodeFormatVide() {
      try {
         formatControlProfilSupport.find("");
         Assert.fail(MESSAGE_PARAM_FORMAT_RUNTIME);
      } catch (IllegalArgumentException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     MESSAGE_EXCEPT + FORMAT_PROFIL,
                     except.getMessage());
      }
   }

   @Test
   public void findCodeFormatEspace() {
      try {
         formatControlProfilSupport.find("     ");
         Assert.fail(MESSAGE_PARAM_FORMAT_RUNTIME);
      } catch (IllegalArgumentException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     MESSAGE_EXCEPT + FORMAT_PROFIL,
                     except.getMessage());
      }
   }

   @Test
   public void findCodeFormatInexistant() {
      FormatControlProfil format = formatControlProfilSupport.find("test");
      Assert.assertNull(format); // format inexistant
   }

}
