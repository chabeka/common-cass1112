package fr.urssaf.image.sae.droit.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.utils.EnumValidationMode;

/**
 * 
 * Classe Test de la classe {@link FormatControlProfilService}
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class FormatControlProfilServiceTest {

   private static final String MESSAGE_EXCEPT_INCORRECT = "Le message de l'exception est incorrect";
   private static final String RESULTAT_INCORRECT = "Le resultat est incorrect";

   private static final String FORMAT_CODE = "formatCode";

   private static final String CODE_FORMAT_CONTROL_PROFIL = "INT_FORMAT_PROFIL_ATT_VIGI";

   @Autowired
   private FormatControlProfilService formControlProfilService;

   @Test
   public void getFormatControlProfilSuccess()
         throws FormatControlProfilNotFoundException {

      FormatControlProfil formatControl = formControlProfilService
            .getFormatControlProfil(CODE_FORMAT_CONTROL_PROFIL);

      Assert.assertNotNull(formatControl);
      Assert.assertEquals(RESULTAT_INCORRECT, "INT_FORMAT_PROFIL_ATT_VIGI",
            formatControl.getFormatCode());

      Assert.assertEquals(RESULTAT_INCORRECT,
            "Contrôle sur les fichiers fournis par l'attestation vigilance.",
            formatControl.getDescription());

      FormatProfil format = formatControl.getControlProfil();
      String formatValidationMode = format.getFormatValidationMode();
      boolean identification = format.isFormatIdentification();
      boolean validation = format.isFormatValidation();

      Assert.assertNotNull(format);

      Assert.assertEquals(RESULTAT_INCORRECT, EnumValidationMode.STRICT
            .toString(), formatValidationMode);
      Assert.assertEquals(RESULTAT_INCORRECT, false, identification);
      Assert.assertEquals(RESULTAT_INCORRECT, true, validation);

   }

   @Test
   public void getFormatControlProfilFailure() {

      try {
         formControlProfilService.getFormatControlProfil("CODE");
      } catch (FormatControlProfilNotFoundException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "Aucun profil de contrôle n'a été trouvé avec l'identifiant : CODE.",
                     except.getMessage());
      }
   }

   @Test
   public void getAllFormatControlSuccess() {

      List<FormatControlProfil> list = formControlProfilService
            .getAllFormatControlProfil();

      Assert.assertFalse(list.isEmpty());
   }

   @Test
   public void createFormatControlProfilSuccess()
         throws FormatControlProfilNotFoundException {
      FormatControlProfil formatControlProfil = new FormatControlProfil();

      formatControlProfil.setDescription("description");
      formatControlProfil.setFormatCode(FORMAT_CODE);

      FormatProfil control = new FormatProfil();
      control.setFileFormat("fileFormat");
      control.setFormatIdentification(false);
      control.setFormatValidation(true);
      control.setFormatValidationMode(EnumValidationMode.STRICT.toString());

      formatControlProfil.setControlProfil(control);
      formControlProfilService.addFormatControlProfil(formatControlProfil);

      FormatControlProfil formatControl = formControlProfilService
            .getFormatControlProfil(FORMAT_CODE);
      Assert.assertNotNull(formatControl);
   }

   @Test
   public void deleteFormatControlProfilSuccess()
         throws FormatControlProfilNotFoundException {
      FormatControlProfil formatControlProfil = new FormatControlProfil();

      formatControlProfil.setDescription("description");
      formatControlProfil.setFormatCode(FORMAT_CODE);

      FormatProfil control = new FormatProfil();
      control.setFileFormat("fileFormat");
      control.setFormatIdentification(false);
      control.setFormatValidation(true);
      control.setFormatValidationMode(EnumValidationMode.STRICT.toString());

      formatControlProfil.setControlProfil(control);
      formControlProfilService.addFormatControlProfil(formatControlProfil);

      formControlProfilService.deleteFormatControlProfil(FORMAT_CODE);

      try {
         formControlProfilService.getFormatControlProfil(FORMAT_CODE);
      } catch (FormatControlProfilNotFoundException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "Aucun profil de contrôle n'a été trouvé avec l'identifiant : formatCode.",
                     except.getMessage());
      }
   }

   @Test
   public void deleteFormatControlProfilFailure() {
      try {
         formControlProfilService.deleteFormatControlProfil(FORMAT_CODE);
      } catch (FormatControlProfilNotFoundException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "Le profil de contrôle à supprimer : [formatCode] n'existe pas en base.",
                     except.getMessage());
      }
   }

}
