/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.rnd;

import java.util.Calendar;
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
import fr.urssaf.image.sae.rnd.dao.support.CorrespondancesRndSupport;
import fr.urssaf.image.sae.rnd.dao.support.cql.CorrespondancesRndCqlSupport;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.EtatCorrespondance;
import fr.urssaf.image.sae.testutils.CompareUtils;

/**
 * (AC75095351) Classe de test migration des referentielFormat
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-migration-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationCorrespondancesRndTest {

  @Autowired
  private CorrespondancesRndCqlSupport supportCql;

  @Autowired
  private CorrespondancesRndSupport supportThrift;

  @Autowired
  MigrationCorrespondancesRnd migrationCorrespondancesRnd;

  @Autowired
  private CassandraServerBean server;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationCorrespondancesRndTest.class);

  private final static int nbCorrespondances = 6;



  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Migration des données Rnd vers referentielFormatcql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {

      populateTableThrift();
      final List<Correspondance> listThrift = supportThrift.getAllCorrespondances();
      migrationCorrespondancesRnd.migrationFromThriftToCql();
      final List<Correspondance> listCql = supportCql.findAll();
      Assert.assertEquals(listThrift.size(), nbCorrespondances);
      Assert.assertEquals(listThrift.size(), listCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
      Assert.assertTrue(false);
    }
  }

  private void populateTableThrift() {
    for (int i = 0; i < nbCorrespondances; i++) {
      supportThrift.ajouterCorrespondance(createCorrespondance(i), new Date().getTime());
    }
  }

  /**
   * Migration des données droitreferentielFormatcql vers DroitRnd
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationCorrespondancesRnd.migrationFromCqlTothrift();
    final List<Correspondance> listThrift = supportThrift.getAllCorrespondances();
    final List<Correspondance> listCql = supportCql.findAll();

    Assert.assertTrue(!listThrift.isEmpty());
    Assert.assertTrue(!listCql.isEmpty());
    Assert.assertEquals(listThrift.size(), listCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
  }

  /**
   * On crée les enregistrements dans la table correspondancesrndcql
   */
  private void populateTableCql() {
    for (int i = 0; i < nbCorrespondances; i++) {
      supportCql.ajouterCorrespondance(createCorrespondance(i));
    }
  }

  /**
   * Création de l'entité Correspondance lié à l'indice i
   * 
   * @param i
   */
  private Correspondance createCorrespondance(final int i) {
    final Correspondance correspondance = new Correspondance();
    try {
      correspondance.setCodeTemporaire("CodeTemp" + i);
      correspondance.setCodeDefinitif("CodeDef" + i);
      correspondance.setVersionCourante("VersionCour" + i);
      correspondance.setDateDebutMaj(Calendar.getInstance().getTime());
      correspondance.setDateFinMaj(Calendar.getInstance().getTime());
      correspondance.setEtat(EtatCorrespondance.CREATED);
    }
    catch (final Exception e) {
      System.out.println("Exception: i=" + i + " " + e.getMessage());
    }

    return correspondance;
  }
}
