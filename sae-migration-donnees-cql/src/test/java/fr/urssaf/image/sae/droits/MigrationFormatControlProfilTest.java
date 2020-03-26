/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.droits;

import java.util.Date;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.droit.MigrationFormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.dao.serializer.FormatProfilSerializer;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.FormatControlProfilCqlSupport;



/**
 * (AC75095351) Classe de test migration des FormatControlProfil
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class MigrationFormatControlProfilTest {

  private static final Date DATE = new Date();

  @Autowired
  private FormatControlProfilCqlSupport supportCql;

  @Autowired
  private FormatControlProfilSupport supportThrift;

  @Autowired
  MigrationFormatControlProfil migrationFormatControlProfil;

  @Autowired
  private CassandraServerBean server;

  final private Javers javers = JaversBuilder
      .javers()
      .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
      .build();




  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationFormatControlProfilTest.class);


  String[] listCode = {"IDENT_VALID_FMT_354", "VALID_FMT_354", "IDENT_FMT_354"};

  String[] listDescription = {"format de controle gérant  la validation et l'identification du fmt/354",
                              "format de controle gérant exclusivement la validation du fmt/354",
  "format de controle gérant exclusivement l'identification du fmt/354"};

  String[] listControlProfil = {"{\"formatIdentification\":true,\"formatValidation\":true,\"formatValidationMode\":\"STRICT\",\"fileFormat\":\"fmt/354\"}",
                                "{\"formatIdentification\":false,\"formatValidation\":true,\"formatValidationMode\":\"STRICT\",\"fileFormat\":\"fmt/354\"} ",
  "{\"formatIdentification\":true,\"formatValidation\":false,\"formatValidationMode\":\"STRICT\",\"fileFormat\":\"fmt/354\"} "};

  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Migration des données de FormatControlProfil vers formatcontrolprofilcql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {

      populateTableThrift();
      migrationFormatControlProfil.migrationFromThriftToCql(javers);
      final List<FormatControlProfil> listThrift = supportThrift.findAll();
      final List<FormatControlProfil> listCql = supportCql.findAll();

      Assert.assertEquals(listThrift.size(), listCode.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      final Diff diff = migrationFormatControlProfil.compareFormatControlProfil(listThrift, listCql, javers);
      MigrationFormatControlProfilTest.LOGGER.info(diff.toString());
      Assert.assertTrue(diff.getChanges().isEmpty());
    }
    catch (final Exception ex) {
      MigrationFormatControlProfilTest.LOGGER.debug("exception=" + ex);
    }
  }

  /**
   * Migration des données de formatcontrolprofilcql vers FormatControlProfil
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    final List<FormatControlProfil> listCql = supportCql.findAll();
    migrationFormatControlProfil.migrationFromCqlTothrift(javers);
    final List<FormatControlProfil> listThrift = supportThrift.findAll();
    Assert.assertEquals(listThrift.size(), listCode.length);
    Assert.assertEquals(listThrift.size(), listCql.size());
    final Diff diff = migrationFormatControlProfil.compareFormatControlProfil(listThrift, listCql, javers);
    MigrationFormatControlProfilTest.LOGGER.info(diff.toString());
    Assert.assertTrue(diff.getChanges().isEmpty());

  }

  /**
   * On crée les enregistremenst dans la table formatcontrolprofilcql
   */
  private void populateTableCql() {
    int i = 0;
    for (final String code : listCode) {
      final FormatControlProfil formatControlProfil = new FormatControlProfil();
      formatControlProfil.setFormatCode(code);
      formatControlProfil.setDescription(listDescription[i]);
      // Deserialisation
      final FormatProfil formatProfil = FormatProfilSerializer.get().fromBytes(listControlProfil[i].getBytes());
      formatControlProfil.setControlProfil(formatProfil);
      supportCql.create(formatControlProfil);
      i++;
    }
  }

  /**
   * On crée les enregistremenst dans la table FormatControlProfil
   */
  private void populateTableThrift() {
    int i = 0;
    for (final String code : listCode) {
      final FormatControlProfil formatControlProfil = new FormatControlProfil();
      formatControlProfil.setFormatCode(code);
      formatControlProfil.setDescription(listDescription[i]);
      // Deserialisation
      final FormatProfil formatProfil = FormatProfilSerializer.get().fromBytes(listControlProfil[i].getBytes());
      formatControlProfil.setControlProfil(formatProfil);
      supportThrift.create(formatControlProfil, new Date().getTime());
      i++;
    }
  }
  @Test
  public void diffAddTest() throws Exception {
    server.resetData();
    populateTableThrift();
    migrationFormatControlProfil.migrationFromThriftToCql(javers);
    final List<FormatControlProfil> listThrift = supportThrift.findAll();
    final FormatControlProfil formatControlProfil = new FormatControlProfil();
    formatControlProfil.setFormatCode("FORMATCODEADD");
    formatControlProfil.setDescription("DESCADD");
    supportCql.create(formatControlProfil);
    final List<FormatControlProfil> listCql = supportCql.findAll();
    final Diff diff = migrationFormatControlProfil.compareFormatControlProfil(listThrift, listCql, javers);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("NewObject{ new object: fr.urssaf.image.sae.droit.dao.model.FormatControlProfil/FORMATCODEADD }"));
  }

  @Test
  public void diffDescTest() throws Exception {
    server.resetData();
    populateTableThrift();
    migrationFormatControlProfil.migrationFromThriftToCql(javers);

    final List<FormatControlProfil> listThrift = supportThrift.findAll();
    final List<FormatControlProfil> listCql = supportCql.findAll();
    listCql.get(0).setDescription("DESCDIFF");

    final Diff diff = migrationFormatControlProfil.compareFormatControlProfil(listThrift, listCql, javers);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertEquals(changes,"ValueChange{ 'description' value changed from 'format de controle gérant  la validation et l'identification du fmt/354' to 'DESCDIFF' }");
  }
}
