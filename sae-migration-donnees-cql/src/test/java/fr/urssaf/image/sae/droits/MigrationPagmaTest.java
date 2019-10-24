/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droits;

import java.util.Arrays;
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
import fr.urssaf.image.sae.droit.MigrationPagma;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmaCqlSupport;
import fr.urssaf.image.sae.testutils.CompareUtils;


/**
 * (AC75095351) Classe de test migration des pagma
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationPagmaTest {

  private static final Date DATE = new Date();

  @Autowired
  private PagmaCqlSupport supportCql;

  @Autowired
  private PagmaSupport supportThrift;

  @Autowired
  MigrationPagma migrationPagma;

  @Autowired
  private CassandraServerBean server;




  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagmaTest.class);



  String[] listCode = {"PAGM_V2_ARCHIVAGE_QD31C_QD31_L02_PAGMa", "PAGM_V2_ARCHIVAGE_QD81B_RD76_L14_PAGMa", "PAGM_RECHERCHE_DOCUMENTAIRE_GNS_PAGMa",
                       "PAGM_FRONTAL_GNS2_PAGMa", "PAGM_FRONTAL_GNS1_PAGMa", "ACCES_FULL_PAGMa"};

  String[][] actionsUnitaires = {
                                 {"archivage_masse", "Ajout de document attache", "reprise_masse"},
                                 {"archivage_masse", "reprise_masse"},
                                 {"ajout_note", "consultation", "deblocage", "modification", "modification_masse", "recherche", "recherche_iterateur",
                                   "suppression", "suppression_masse"},
                                 {"ajout_doc_attache", "ajout_note", "archivage_masse", "archivage_unitaire", "consultation", "copie", "deblocage",
                                     "modification", "modification_masse", "recherche", "recherche_iterateur", "reprise_masse", "restore_masse",
                                     "suppression", "suppression_masse"},
                                 {"ajout_doc_attache", "ajout_note", "archivage_masse", "archivage_unitaire", "consultation", "copie", "deblocage",
                                       "modification", "modification_masse", "recherche", "recherche_iterateur", "reprise_masse", "restore_masse",
                                       "suppression", "suppression_masse"},
                                 {"archivage_masse", "archivage_unitaire", "consultation", "recherche"}

  };

  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Migration des données de DroitPagma vers droitpagmacql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {
      populateTableThrift();
      migrationPagma.migrationFromThriftToCql();
      final List<Pagma> listThrift = supportThrift.findAll();
      final List<Pagma> listCql = supportCql.findAll();

      Assert.assertEquals(listThrift.size(), listCode.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
    }
  }

  /**
   * Migration des données de DroitPagma vers droitpagmacql
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationPagma.migrationFromCqlTothrift();

    final List<Pagma> listThrift = supportThrift.findAll();
    final List<Pagma> listCql = supportCql.findAll();

    Assert.assertEquals(listCql.size(), listCode.length);
    Assert.assertEquals(listThrift.size(), listCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
  }

  /**
   * On crée les enregistrements dans la table droitpagmacql
   */
  private void populateTableCql() {
    int i = 0;
    for (final String code : listCode) {
      final Pagma pagma = new Pagma();
      pagma.setCode(code);
      final String[] tabActionsUnitaires = actionsUnitaires[i];
      pagma.setActionUnitaires(Arrays.asList(tabActionsUnitaires));
      supportCql.create(pagma);
      i++;
    }
  }

  /**
   * On crée les enregistrements dans la table DroitPagma
   */
  private void populateTableThrift() {
    int i = 0;
    for (final String code : listCode) {
      final Pagma pagma = new Pagma();
      pagma.setCode(code);
      final String[] tabActionsUnitaires = actionsUnitaires[i];
      pagma.setActionUnitaires(Arrays.asList(tabActionsUnitaires));
      supportThrift.create(pagma, new Date().getTime());
      i++;
    }
  }
}
