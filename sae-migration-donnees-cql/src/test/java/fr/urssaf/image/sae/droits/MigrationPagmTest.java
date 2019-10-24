/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droits;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import fr.urssaf.image.sae.droit.MigrationPagm;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.modelcql.PagmCql;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmCqlSupport;
import fr.urssaf.image.sae.testutils.CompareUtils;


/**
 * (AC75095351) Classe de test migration des pagm
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationPagmTest {

  @Autowired
  private PagmCqlSupport supportCql;

  @Autowired
  private PagmSupport supportThrift;

  @Autowired
  MigrationPagm migrationPagm;

  @Autowired
  private CassandraServerBean server;




  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagmTest.class);

  private static final String ID_CLIENT = "INT_CS_TEST";

  String[] listCode = {"INT_PAGM_CS_TEST_ARCH", "INT_PAGM_CS_TEST_ARCH_MASSE", "INT_PAGM_CS_TEST_ARCH_UNIT",
                       "INT_PAGM_CS_TEST_CONSULT", "INT_PAGM_CS_TEST_RECHERCHE"};

  // String mapXml = "<?xml version='1.0' encoding='UTF-8'?><map><entry><string>Cle1</string><string>Valeur1</string></entry><entry><string>Cle2</string><string>Valeur2</string></entry></map>";

  /*
   * String[] listPagm = {"{\"code\":\"INT_PAGM_CS_TEST_ARCH\",\"pagma\":\"INT_PAGM_CS_TEST_ARCH_PAGMa\",\"pagmp\":\"INT_PAGM_CS_TEST_ARCH_PAGMp\",\"description\":\"Archivage unitaire et de masse\",\"parametres\":{}}",
   * "{\"code\":\"INT_PAGM_CS_TEST_ARCH_MASSE\",\"pagma\":\"INT_PAGM_CS_TEST_ARCH_MASSE_PAGMa\",\"pagmp\":\"INT_PAGM_CS_TEST_ARCH_MASSE_PAGMp\",\"description\":\"Archivage de masse\",\"parametres\":{}}",
   * "{\"code\":\"INT_PAGM_CS_TEST_ARCH_UNIT\",\"pagma\":\"INT_PAGM_CS_TEST_ARCH_UNIT_PAGMa\",\"pagmp\":\"INT_PAGM_CS_TEST_ARCH_UNIT_PAGMp\",\"description\":\"Archivage unitaire\",\"parametres\":{}}",
   * "{\"code\":\"INT_PAGM_CS_TEST_CONSULT\",\"pagma\":\"INT_PAGM_CS_TEST_CONSULT_PAGMa\",\"pagmp\":\"INT_PAGM_CS_TEST_CONSULT_PAGMp\",\"description\":\"Consultation\",\"parametres\":{}}",
   * "{\"code\":\"INT_PAGM_CS_TEST_RECHERCHE\",\"pagma\":\"INT_PAGM_CS_TEST_RECHERCHE_PAGMa\",\"pagmp\":\"INT_PAGM_CS_TEST_RECHERCHE_PAGMp\",\"description\":\"Recherche\",\"parametres\":{}}"
   * };
   */

  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Migration des données DroitPagm vers droitpagmcql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {

      populateTableThrift();
      final List<Pagm> listThrift = supportThrift.find(ID_CLIENT);
      final List<String> keys = migrationPagm.migrationFromThriftToCql();
      final List<PagmCql> listThrift3 = migrationPagm.getListPagmCqlFromThrift(keys);
      @SuppressWarnings("unused")
      final List<PagmCql> listThrift2 = supportThrift.findAll();

      final List<PagmCql> listCql = supportCql.findAll();

      Assert.assertEquals(listThrift.size(), listCode.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift3, listCql));
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
      Assert.assertTrue(false);
    }
  }

  /**
   * Migration des données droitpagmcql vers DroitPagm
   */
  @Test
  public void migrationFromCqlTothrift() {
    try {
      populateTableCql();
      migrationPagm.migrationFromCqlTothrift();
      final List<Pagm> listThrift = supportThrift.find(ID_CLIENT);
      final List<PagmCql> listCql = supportCql.findAll();
      final List<String> keys = migrationPagm.migrationFromThriftToCql();
      final List<PagmCql> listThrift2 = migrationPagm.getListPagmCqlFromThrift(keys);
      Assert.assertEquals(listCql.size(), listCode.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift2, listCql));
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
      Assert.assertTrue(false);
    }
  }

  /**
   * On crée les enregistrements dans la table DroitPagm
   */
  private void populateTableThrift() {
    int i = 0;
    for (final String code : listCode) {
      final Pagm pagm = new Pagm();
      pagm.setCode("CODE" + i);
      pagm.setCompressionPdfActive(false);
      pagm.setDescription("TEST" + i);
      pagm.setPagma("PAGMA" + i);
      pagm.setPagmp("PAGMP" + i);
      pagm.setPagmf("PAGMF" + i);
      final Map<String, String> parametres = new HashMap<>();
      parametres.put("cle1", "valeur1");
      parametres.put("cle2", "valeur2");
      pagm.setParametres(parametres);
      pagm.setSeuilCompressionPdf(80);

      supportThrift.create(ID_CLIENT, pagm, new Date().getTime());
      i++;
    }
  }
  /**
   * On crée les enregistrements dans la table droitpagmcql
   */
  private void populateTableCql() {
    int i = 0;
    for (final String code : listCode) {
      final PagmCql pagmCql = new PagmCql();
      pagmCql.setIdClient(ID_CLIENT);
      pagmCql.setCode("CODE" + i);
      pagmCql.setCompressionPdfActive(false);
      pagmCql.setDescription("TEST" + i);
      pagmCql.setPagma("PAGMA" + i);
      pagmCql.setPagmp("PAGMP" + i);
      pagmCql.setPagmf("PAGMF" + i);
      final HashMap<String, String> parametres = new HashMap<>();
      parametres.put("cle1", "valeur1");
      parametres.put("cle2", "valeur2");
      pagmCql.setParametres(parametres);
      pagmCql.setSeuilCompressionPdf(80);

      supportCql.create(pagmCql);
      i++;
    }
  }


}
