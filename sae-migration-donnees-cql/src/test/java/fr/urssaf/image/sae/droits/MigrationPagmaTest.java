/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droits;

import java.util.Arrays;
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
import fr.urssaf.image.sae.droit.MigrationPagma;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmaCqlSupport;
import fr.urssaf.image.sae.utils.CompareUtils;



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

  final private Javers javers = JaversBuilder
                                             .javers()
                                             .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
                                             .build();


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
      migrationPagma.migrationFromThriftToCql(javers);
      final List<Pagma> listThrift = supportThrift.findAll();
      final List<Pagma> listCql = supportCql.findAll();

      Assert.assertEquals(listThrift.size(), listCode.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
    }
    catch (final Exception ex) {
      MigrationPagmaTest.LOGGER.debug("exception=" + ex);
    }
  }

  /**
   * Migration des données de DroitPagma vers droitpagmacql
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationPagma.migrationFromCqlTothrift(javers);

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
  @Test
  public void diffAddTest() throws Exception {
    server.resetData();
    populateTableThrift();
    migrationPagma.migrationFromThriftToCql(javers);
    final List<Pagma> listThrift = supportThrift.findAll();
    final Pagma pagma = new Pagma();
    pagma.setCode("CODEADD");
    supportCql.create(pagma);
    final List<Pagma> listCql = supportCql.findAll();
    final Diff diff = migrationPagma.comparePagmas(listThrift, listCql, javers);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("NewObject{ new object: fr.urssaf.image.sae.droit.dao.model.Pagma/CODEADD }"));
  }

  @Test
  public void diffActionsUnitairesTest() throws Exception {
    populateTableThrift();
    migrationPagma.migrationFromThriftToCql(javers);

    final List<Pagma> listThrift = supportThrift.findAll();
    final List<Pagma> listCql = supportCql.findAll();
    listCql.get(0).setActionUnitaires(Arrays.asList(new String[]{"ACTDIFF"}));

    final Diff diff = migrationPagma.comparePagmas(listThrift, listCql, javers);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString().trim();
    final String shortChanges =changes.substring(0, 51).trim();
    Assert.assertEquals(shortChanges,"ListChange{ 'actionUnitaires' collection changes :");
    /*Assert.assertEquals(changes,"ListChange{ 'actionUnitaires' collection changes :\r\n" + 
    		"  0. 'ajout_doc_attache' changed to 'ACTDIFF'\r\n" + 
    		"  1. 'ajout_note' removed\r\n" + 
    		"  2. 'archivage_masse' removed\r\n" + 
    		"  3. 'archivage_unitaire' removed\r\n" + 
    		"  4. 'consultation' removed\r\n" + 
    		"  5. 'copie' removed\r\n" + 
    		"  6. 'deblocage' removed\r\n" + 
    		"  7. 'modification' removed\r\n" + 
    		"  8. 'modification_masse' removed\r\n" + 
    		"  9. 'recherche' removed\r\n" + 
    		"  10. 'recherche_iterateur' removed\r\n" + 
    		"  11. 'reprise_masse' removed\r\n" + 
    		"  12. 'restore_masse' removed\r\n" + 
    		"  13. 'suppression' removed\r\n" + 
    		"  14. 'suppression_masse' removed }".trim());*/

  }
}
