package fr.urssaf.image.sae.droit.service;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import junit.framework.Assert;

/**
 * Classe Test de la classe {@link FormatControlProfilService}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-droit-test.xml"})
public class FormatControlProfilServiceDatasTest {

  private static final String MESSAGE_EXCEPT_INCORRECT = "Le message de l'exception est incorrect";

  private static final String RESULTAT_INCORRECT = "Le resultat est incorrect";

  private static final String FORMAT_CODE = "formatCode";

  private static final String CODE_FORMAT_CONTROL_PROFIL = "INT_FORMAT_PROFIL_ATT_VIGI";

  private static final Logger LOGGER = LoggerFactory
      .getLogger(FormatControlProfilServiceDatasTest.class);

  @Autowired
  // @Qualifier("formatControlProfilServiceFacadeImpl")
  private FormatControlProfilService formControlProfilService;

  @Autowired
  private FormatControlProfilSupport formControlProfilServiceSupport;

  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  private JobClockSupport clockSupport;

  @After
  public void end() throws Exception {
    cassandraServer.resetData(true, MODE_API.HECTOR);
  }

  @Test
  public void testFormatControlProfilExistant() {
    try {


      final FormatControlProfil formatControlProfil = new FormatControlProfil();

      formatControlProfil.setFormatCode("IDENT_FMT_354");

      formatControlProfil.setDescription("format de controle gérant exclusivement l'identification du fmt/354");

      final FormatProfil formatProfil = new FormatProfil();
      formatProfil.setFileFormat("fmt/354");
      formatProfil.setFormatIdentification(true);
      formatProfil.setFormatValidation(false);
      formatProfil.setFormatValidationMode("STRICT");
      formatControlProfil.setControlProfil(formatProfil);

      formControlProfilServiceSupport.create(formatControlProfil, clockSupport.currentCLock());

      formControlProfilService.addFormatControlProfil(formatControlProfil);
    }
    catch (final Exception e) {
      LOGGER.debug("EXCEPTION", e.getMessage());
    }
    Assert.assertFalse("l'autorisation doit etre refusée", false);// TEST NON VALIDE

  }

  @Test
  public void testFormatControlProfilSucces() throws Exception {

    final FormatControlProfil formatControlProfil = new FormatControlProfil();

    formatControlProfil.setFormatCode("IDENT_FMT_354");

    formatControlProfil.setDescription("format de controle gérant exclusivement l'identification du fmt/354");

    final FormatProfil formatProfil = new FormatProfil();
    formatProfil.setFileFormat("fmt/354");
    formatProfil.setFormatIdentification(true);
    formatProfil.setFormatValidation(false);
    formatProfil.setFormatValidationMode("STRICT");
    formatControlProfil.setControlProfil(formatProfil);

    formControlProfilService.addFormatControlProfil(formatControlProfil);

    final FormatControlProfil storedFormatControlProfil = formControlProfilServiceSupport.find("IDENT_FMT_354");

    Assert.assertEquals("les deux actions doivent etre identiques",
                        formatControlProfil,
                        storedFormatControlProfil);

  }
}
