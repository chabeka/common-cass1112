/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droits;

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
import fr.urssaf.image.sae.droit.MigrationActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.ActionUnitaireCqlSupport;
import fr.urssaf.image.sae.utils.CompareUtils;



/**
 * (AC75095351) Classe de test migration des actions unitaires
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class MigrationActionUnitaireTest {

  private static final Date DATE = new Date();

  @Autowired
  private ActionUnitaireCqlSupport supportCql;

  @Autowired
  private ActionUnitaireSupport supportThrift;

  @Autowired
  MigrationActionUnitaire migrationActionUnitaire;

  @Autowired
  private CassandraServerBean server;




  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationActionUnitaireTest.class);


  String[] listCode = {"deblocage", "ajout_doc_attache", "recherche", "transfert_masse", "ajout_note", "reprise"};

  String[] listDescription = {"deblocage de traitement de masse", "Ajout de document attache", "recherche", "transfert de masse", "Ajout de notes",
  "reprise de traitement de masse"};

  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Méthode pour migrer les données vers DroitActionUnitaire vers droitactionunitairecql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {
      populateTableThrift();
      migrationActionUnitaire.migrationFromThriftToCql();
      final List<ActionUnitaire> listThrift = supportThrift.findAll();
      final List<ActionUnitaire> listCql = supportCql.findAll();

      Assert.assertEquals(listThrift.size(), listCode.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      // LOGGER.info("SizeThriftToCqlActionUnitaire=" + listThrift.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
    }
  }

  /**
   * Méthode pour migrer les données de droitactionunitairecql vers DroitActionUnitaire
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationActionUnitaire.migrationFromCqlTothrift();

    final List<ActionUnitaire> listThrift = supportThrift.findAll();
    final List<ActionUnitaire> listCql = supportCql.findAll();

    Assert.assertEquals(listCql.size(), listCode.length);
    Assert.assertEquals(listThrift.size(), listCql.size());
    // LOGGER.info("SizeCqlToThriftActionUnitaire=" + listThrift.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
  }

  /**
   * On crée les enregistremenst dans la table droitactionunitairecql
   */
  private void populateTableCql() {
    int i = 0;
    for (final String code : listCode) {
      final ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode(code);
      actionUnitaire.setDescription(listDescription[i]);
      supportCql.create(actionUnitaire);
      i++;
    }
  }

  /**
   * On crée les enregistremenst dans la table DroitActionUnitaire
   */
  private void populateTableThrift() {
    int i = 0;
    for (final String code : listCode) {
      final ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode(code);
      actionUnitaire.setDescription(listDescription[i]);
      supportThrift.create(actionUnitaire, new Date().getTime());
      i++;
    }
  }


}
