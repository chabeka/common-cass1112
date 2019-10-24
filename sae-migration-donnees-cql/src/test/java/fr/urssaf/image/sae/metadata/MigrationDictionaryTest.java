/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.metadata;

import java.util.Date;
import java.util.List;

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
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.support.DictionarySupport;
import fr.urssaf.image.sae.metadata.referential.support.cql.DictionaryCqlSupport;
import fr.urssaf.image.sae.testutils.CompareUtils;

/**
 * (AC75095351) Classe de test migration des dictionary
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-migration-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationDictionaryTest {

  @Autowired
  private DictionaryCqlSupport supportCql;

  @Autowired
  private DictionarySupport supportThrift;

  @Autowired
  MigrationDictionary migrationDictionary;

  @Autowired
  private CassandraServerBean server;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationDictionaryTest.class);



  String[] listIdentifiants = {"Dict1", "Dict2", "Dict3",
                               "Dict4", "Dict5"};

  String[][] listEntries = {
                            {"MOT1", "MOT11", "MOT111"},
                            {"MOT2", "MOT22", "MOT222"},
                            {"MOT3", "MOT33", "MOT333"},
                            {"MOT4", "MOT44", "MOT444"},
                            {"MOT5", "MOT55", "MOT555", "MOT5555"},
  };

  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Migration des données DroitDictionary vers droitdictionarycql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {

      populateTableThrift();
      final List<Dictionary> listThrift = supportThrift.findAll();
      migrationDictionary.migrationFromThriftToCql();
      final List<Dictionary> listCql = supportCql.findAll();
      Assert.assertEquals(listThrift.size(), listIdentifiants.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
      Assert.assertTrue(false);
    }
  }

  private void populateTableThrift() {
    int i = 0;
    for (final String identifiant : listIdentifiants) {
      for (final String entries : listEntries[i]) {
        supportThrift.addElement(identifiant, entries, new Date().getTime());
      }
      i++;
    }
  }

  /**
   * Migration des données droitdictionarycql vers DroitDictionary
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationDictionary.migrationFromCqlTothrift();
    final List<Dictionary> listThrift = supportThrift.findAll();
    final List<Dictionary> listCql = supportCql.findAll();
    Assert.assertEquals(listCql.size(), listIdentifiants.length);
    Assert.assertEquals(listThrift.size(), listCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
  }

  /**
   * On crée les enregistrements dans la table droitdictionarycql
   */
  private void populateTableCql() {
    int i = 0;
    for (final String identifiant : listIdentifiants) {
      for (final String entries : listEntries[i]) {
        supportCql.addElement(identifiant, entries);
      }
      i++;
    }
  }
}
