package fr.urssaf.image.sae.droit.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
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
  // @Qualifier("formatControlProfilServiceFacadeImpl")
  private FormatControlProfilService formControlProfilService;

  @Autowired
  private CassandraServerBean cassandraServer;

  @Before
  public void setup() throws Exception {

    cassandraServer.resetData(true, MODE_API.HECTOR);

  }

  @Test
  public void getFormatControlProfilSuccess()
      throws Exception {

    final FormatControlProfil formatControl = formControlProfilService
        .getFormatControlProfil(CODE_FORMAT_CONTROL_PROFIL);

    Assert.assertNotNull(formatControl);
    Assert.assertEquals(RESULTAT_INCORRECT, "INT_FORMAT_PROFIL_ATT_VIGI",
                        formatControl.getFormatCode());

    Assert.assertEquals(RESULTAT_INCORRECT,
                        "Contrôle sur les fichiers fournis par l'attestation vigilance.",
                        formatControl.getDescription());

    final FormatProfil format = formatControl.getControlProfil();
    final String formatValidationMode = format.getFormatValidationMode();
    final boolean identification = format.isFormatIdentification();
    final boolean validation = format.isFormatValidation();

    Assert.assertNotNull(format);

    Assert.assertEquals(RESULTAT_INCORRECT, EnumValidationMode.STRICT
                        .toString(), formatValidationMode);
    Assert.assertEquals(RESULTAT_INCORRECT, false, identification);
    Assert.assertEquals(RESULTAT_INCORRECT, true, validation);

  }

  @Test
  public void getFormatControlProfilFailure() throws Exception {

    try {
      formControlProfilService.getFormatControlProfil("CODE");
    } catch (final FormatControlProfilNotFoundException except) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    "Aucun profil de contrôle n'a été trouvé avec l'identifiant : CODE.",
                    except.getMessage());
    }
  }

  @Test
  public void getAllFormatControlSuccess() throws Exception {

    final List<FormatControlProfil> list = formControlProfilService
        .getAllFormatControlProfil();

    Assert.assertFalse(list.isEmpty());
  }

  @Test
  public void createFormatControlProfilSuccess()
      throws FormatControlProfilNotFoundException {
    final FormatControlProfil formatControlProfil = new FormatControlProfil();

    formatControlProfil.setDescription("description");
    formatControlProfil.setFormatCode(FORMAT_CODE);

    final FormatProfil control = new FormatProfil();
    control.setFileFormat("fileFormat");
    control.setFormatIdentification(false);
    control.setFormatValidation(true);
    control.setFormatValidationMode(EnumValidationMode.STRICT.toString());

    formatControlProfil.setControlProfil(control);
    formControlProfilService.addFormatControlProfil(formatControlProfil);

    final FormatControlProfil formatControl = formControlProfilService
        .getFormatControlProfil(FORMAT_CODE);
    Assert.assertNotNull(formatControl);
  }

  /**
   * A vérifier nouveau message: FormatControlProfilNotFoundException: Le profil de contrôle à supprimer : [formatCode] n'existe pas en base
   * 
   * @throws FormatControlProfilNotFoundException
   */
  @Test
  public void deleteFormatControlProfilSuccess()
      throws FormatControlProfilNotFoundException {
    final FormatControlProfil formatControlProfil = new FormatControlProfil();

    formatControlProfil.setDescription("description");
    formatControlProfil.setFormatCode(FORMAT_CODE);

    final FormatProfil control = new FormatProfil();
    control.setFileFormat("fileFormat");
    control.setFormatIdentification(false);
    control.setFormatValidation(true);
    control.setFormatValidationMode(EnumValidationMode.STRICT.toString());

    formatControlProfil.setControlProfil(control);
    formControlProfilService.addFormatControlProfil(formatControlProfil);

    formControlProfilService.deleteFormatControlProfil(FORMAT_CODE);

    try {
      formControlProfilService.getFormatControlProfil(FORMAT_CODE);
    } catch (final FormatControlProfilNotFoundException except) {
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
    } catch (final FormatControlProfilNotFoundException except) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    "Le profil de contrôle à supprimer : [formatCode] n'existe pas en base.",
                    except.getMessage());
    }
  }

}
