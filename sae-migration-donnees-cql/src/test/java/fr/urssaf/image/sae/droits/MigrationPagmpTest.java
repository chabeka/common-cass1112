/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droits;

import java.util.Date;
import java.util.List;

import org.javers.core.diff.Diff;
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
import fr.urssaf.image.sae.droit.MigrationPagmp;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmpCqlSupport;
import fr.urssaf.image.sae.utils.CompareUtils;



/**
 * (AC75095351) Classe de test migration des pagmp
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationPagmpTest {

  private static final Date DATE = new Date();

  @Autowired
  private PagmpCqlSupport supportCql;

  @Autowired
  private PagmpSupport supportThrift;

  @Autowired
  MigrationPagmp migrationPagmp;

  @Autowired
  private CassandraServerBean server;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagmpTest.class);



  String[] listCode = {"PAGM_V2_ARCHIVAGE_QD12K_QD12_L07_PAGMp", "PAGM_DCL_RECHERCHE_CONSULTATION_GNS_PAGMp", "PAGM_V2_ARCHIVAGE_QD12J_RD12_L05_PAGMp",
                       "PAGM_V2_ARCHIVAGE_PCA1E_PCA1_L02_PAGMp", "PAGM_V2_ARCHIVAGE_QDXXA_QD30_L01_PAGMp", "PAGM_V2_ARCHIVAGE_QD17A_QD17_L00_PAGMp"};

  String[] listDescription = {"QD12K QD12.L07", "PAGM_DCL_RECHERCHE_CONSULTATION_GNS_PAGMp", "QD12J RD12.L05",
                              "PCA1E PCA1.L02", "QDXXA QD30.L01", "QD17A QD17.L00"};

  String[] listPrmd = {"PRMD_V2_QD12K_QD12_L07", "PRMD_COTISANT", "PRMD_V2_QD12J_RD12_L05",
                       "PRMD_V2_PCA1E_PCA1_L02", "PRMD_V2_QDXXA_QD30_L01", "PRMD_V2_QD17A_QD17_L00"};


  @After
  public void after() throws Exception {
    server.resetData();

  }

  /**
   * Migration des données DroitPagmp vers droitpagmpcql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {
      populateTableThrift();

      migrationPagmp.migrationFromThriftToCql();
      final List<Pagmp> listThrift = supportThrift.findAll();
      final List<Pagmp> listCql = supportCql.findAll();

      Assert.assertEquals(listThrift.size(), listCode.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
    }
  }

  /**
   * Migration des données de droitpagmpcql vers DroitPagmp
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationPagmp.migrationFromCqlTothrift();

    final List<Pagmp> listThrift = supportThrift.findAll();
    final List<Pagmp> listCql = supportCql.findAll();

    Assert.assertEquals(listCql.size(), listCode.length);
    Assert.assertEquals(listThrift.size(), listCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
  }

  /**
   * On crée les enregistrements dans la table droitpagmpcql
   */
  private void populateTableCql() {
    int i = 0;
    for (final String code : listCode) {
      final Pagmp pagmp = new Pagmp();
      pagmp.setCode(code);
      pagmp.setDescription(listDescription[i]);
      pagmp.setPrmd(listPrmd[i]);

      supportCql.create(pagmp);
      i++;
    }
  }

  /**
   * On crée les enregistrements dans la table DroitPagmp
   */
  private void populateTableThrift() {
    int i = 0;
    for (final String code : listCode) {
      final Pagmp pagmp = new Pagmp();
      pagmp.setCode(code);
      pagmp.setDescription(listDescription[i]);
      pagmp.setPrmd(listPrmd[i]);
      supportThrift.create(pagmp, new Date().getTime());
      i++;
    }
  }
  @Test
  public void diffAddTest() throws Exception {
    populateTableThrift();
    migrationPagmp.migrationFromThriftToCql();
    final List<Pagmp> listThrift = supportThrift.findAll();
    final Pagmp pagmp = new Pagmp();
    pagmp.setCode("CODEADD");
    supportCql.create(pagmp);
    final List<Pagmp> listCql = supportCql.findAll();
    final Diff diff = migrationPagmp.comparePagmps(listThrift, listCql);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("NewObject{ new object: fr.urssaf.image.sae.droit.dao.model.Pagmp/CODEADD }"));
  }

  @Test
  public void diffDescTest() throws Exception {
    populateTableThrift();
    migrationPagmp.migrationFromThriftToCql();

    final List<Pagmp> listThrift = supportThrift.findAll();
    final List<Pagmp> listCql = supportCql.findAll();
    listCql.get(0).setDescription("DESCDIFF");
    
    final Diff diff = migrationPagmp.comparePagmps(listThrift, listCql);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
     Assert.assertTrue(changes.equals("ValueChange{ 'description' value changed from 'PAGM_DCL_RECHERCHE_CONSULTATION_GNS_PAGMp' to 'DESCDIFF' }"));
  }
    
  
  
  
}
