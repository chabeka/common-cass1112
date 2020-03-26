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
import fr.urssaf.image.sae.droit.MigrationPagmf;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.serializer.FormatProfilSerializer;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmfSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.FormatControlProfilCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmfCqlSupport;
import fr.urssaf.image.sae.utils.CompareUtils;



/**
 * (AC75095351) Classe de test migration des pagmf
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationPagmfTest {

  private static final Date DATE = new Date();


  private static final String FORMAT_CODE="IDENT_VALID_FMT_354";
  private static final String FORMAT_DESCRIPTION="\"format de controle gérant  la validation et l'identification du fmt/354\"";
  private static final String FORMAT_XML="{\"formatIdentification\":true,\"formatValidation\":true,\"formatValidationMode\":\"STRICT\",\"fileFormat\":\"fmt/354\"}";

  @Autowired
  private PagmfCqlSupport supportCql;

  @Autowired
  private PagmfSupport supportThrift;

  @Autowired
  private FormatControlProfilSupport supportFormatControlProfilThrift;

  @Autowired
  private FormatControlProfilCqlSupport supportFormatControlProfilCql;

  @Autowired
  MigrationPagmf migrationPagmf;

  @Autowired
  private CassandraServerBean server;

  final private Javers javers = JaversBuilder
                                             .javers()
                                             .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
                                             .build();


  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagmfTest.class);



  String[] listCodePagmf = {"INT_FMT_PAGMf", "INT_FMT_PAGMf1", "INT_FMT_PAGMf2",
                            "INT_FMT_PAGMf3", "INT_FMT_PAGMf4", "INT_FMT_PAGMf5"};

  String[] listDescription = {"Contrôle sur les fichiers fournis par l’attestation vigilance", "Contrôle sur les fichiers fournis par l’attestation vigilance1",
                              "Contrôle sur les fichiers fournis par l’attestation vigilance2",
                              "Contrôle sur les fichiers fournis par l’attestation vigilance3",
                              "Contrôle sur les fichiers fournis par l’attestation vigilance4",
  "Contrôle sur les fichiers fournis par l’attestation vigilance4"};

  String[] listFormatProfile = {"IDENT_VALID_FMT_354", "IDENT_VALID_FMT_354", "IDENT_VALID_FMT_354",
                                "IDENT_VALID_FMT_354", "IDENT_VALID_FMT_354", "IDENT_VALID_FMT_354"};


  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Migration des données de DroitPagmf vers droitpagmfcql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {

      populateTableThrift();
      migrationPagmf.migrationFromThriftToCql(javers);
      final List<Pagmf> listThrift = supportThrift.findAll();
      final List<Pagmf> listCql = supportCql.findAll();

      Assert.assertEquals(listThrift.size(), listCodePagmf.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
    }
    catch (final Exception ex) {
      MigrationPagmfTest.LOGGER.debug("exception=" + ex);
    }
  }

  /**
   * Migration des données de droitpagmfcql vers DroitPagmf
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationPagmf.migrationFromCqlTothrift(javers);

    final List<Pagmf> listThrift = supportThrift.findAll();
    final List<Pagmf> listCql = supportCql.findAll();

    Assert.assertEquals(listCql.size(), listCodePagmf.length);
    Assert.assertEquals(listThrift.size(), listCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
  }

  /**
   * On crée les enregistrements dans la table droitpagmfcql
   */
  private void populateTableCql() {
    // Ajout du format
    addFormatControlProfilCql();
    addFormatControlProfilThrift();
    int i = 0;
    for (final String code : listCodePagmf) {
      final Pagmf pagmf = new Pagmf();
      pagmf.setCodePagmf(code);
      pagmf.setDescription(listDescription[i]);
      pagmf.setCodeFormatControlProfil(listFormatProfile[i]);
      supportCql.create(pagmf);
      i++;
    }
  }

  /**
   * On crée les enregistrements dans la table formatcontrolprofilcql nécessaires pour les pagmf
   */
  private void addFormatControlProfilCql() {
    final FormatControlProfil formatControlProfil = new FormatControlProfil();
    formatControlProfil.setFormatCode(MigrationPagmfTest.FORMAT_CODE);
    formatControlProfil.setDescription(MigrationPagmfTest.FORMAT_DESCRIPTION);
    // Deserialisation
    final FormatProfil formatProfil = FormatProfilSerializer.get().fromBytes(MigrationPagmfTest.FORMAT_XML.getBytes());
    formatControlProfil.setControlProfil(formatProfil);
    supportFormatControlProfilCql.create(formatControlProfil);
  }

  /**
   * On crée les enregistrements dans la table droitpagmf
   */
  private void populateTableThrift() {
    // Ajout du format
    addFormatControlProfilThrift();
    int i = 0;
    for (final String code : listCodePagmf) {
      final Pagmf pagmf = new Pagmf();
      pagmf.setCodePagmf(code);
      pagmf.setDescription(listDescription[i]);
      pagmf.setCodeFormatControlProfil(listFormatProfile[i]);
      supportThrift.create(pagmf, new Date().getTime());
      i++;
    }
  }

  /**
   * On crée les enregistrements dans la table FormatControlProfil nécessaires pour les pagmf
   */
  private void addFormatControlProfilThrift() {
    final FormatControlProfil formatControlProfil = new FormatControlProfil();
    formatControlProfil.setFormatCode(MigrationPagmfTest.FORMAT_CODE);
    formatControlProfil.setDescription(MigrationPagmfTest.FORMAT_DESCRIPTION);
    // Deserialisation
    final FormatProfil formatProfil = FormatProfilSerializer.get().fromBytes(MigrationPagmfTest.FORMAT_XML.getBytes());
    formatControlProfil.setControlProfil(formatProfil);
    supportFormatControlProfilThrift.create(formatControlProfil, new Date().getTime());
  }

  @Test
  public void diffAddTest() throws Exception {
    populateTableThrift();
    migrationPagmf.migrationFromThriftToCql(javers);
    final List<Pagmf> listThrift = supportThrift.findAll();
    final Pagmf pagmf = new Pagmf();
    pagmf.setCodePagmf("CODEADD");
    supportCql.create(pagmf);
    final List<Pagmf> listCql = supportCql.findAll();
    final Diff diff = migrationPagmf.comparePagmfs(listThrift, listCql);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("NewObject{ new object: fr.urssaf.image.sae.droit.dao.model.Pagmf/CODEADD }"));
  }

  @Test
  public void diffDescTest() throws Exception {
    populateTableThrift();
    migrationPagmf.migrationFromThriftToCql(javers);

    final List<Pagmf> listThrift = supportThrift.findAll();
    final List<Pagmf> listCql = supportCql.findAll();
    listCql.get(0).setDescription("DESCDIFF");

    final Diff diff = migrationPagmf.comparePagmfs(listThrift, listCql);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();

    Assert.assertTrue(changes.equals("ValueChange{ 'description' value changed from 'Contrôle sur les fichiers fournis par l’attestation vigilance1' to 'DESCDIFF' }"));



  }
}
