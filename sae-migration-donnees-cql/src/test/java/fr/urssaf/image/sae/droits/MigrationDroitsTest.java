/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droits;

import java.util.Date;
import java.util.List;

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

import fr.urssaf.image.sae.droit.MigrationActionUnitaire;
import fr.urssaf.image.sae.droit.MigrationContratService;
import fr.urssaf.image.sae.droit.MigrationFormatControlProfil;
import fr.urssaf.image.sae.droit.MigrationPagm;
import fr.urssaf.image.sae.droit.MigrationPagma;
import fr.urssaf.image.sae.droit.MigrationPagmp;
import fr.urssaf.image.sae.droit.MigrationPrmd;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.modelcql.PagmCql;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.ActionUnitaireCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.ContratServiceCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.FormatControlProfilCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmaCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmpCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PrmdCqlSupport;
import fr.urssaf.image.sae.utils.CompareUtils;



/**
 * (AC75095351)
 * Classe de test pour migration de toutes les tables concernés par les droits
 * thrift vers cql et cql vers thrift
 * La mise en commentaire permet de tester séparément des classes de migration
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class MigrationDroitsTest {

  private static final Date DATE = new Date();

  @Autowired
  private FormatControlProfilCqlSupport supportFormatControlProfilCql;

  @Autowired
  private ActionUnitaireCqlSupport supportActionUnitaireCql;

  @Autowired
  private PagmaCqlSupport supportPagmaCql;

  @Autowired
  private PagmpCqlSupport supportPagmpCql;

  @Autowired
  private PrmdCqlSupport supportPrmdCql;

  @Autowired
  private PagmCqlSupport supportPagmCql;

  @Autowired
  private ContratServiceCqlSupport supportContratServiceCql;

  @Autowired
  private FormatControlProfilSupport supportFormatControlProfilThrift;

  @Autowired
  private ActionUnitaireSupport supportActionUnitaireThrift;

  @Autowired
  private PagmaSupport supportPagmaThrift;

  @Autowired
  private PagmpSupport supportPagmpThrift;

  @Autowired
  private PrmdSupport supportPrmdThrift;

  @Autowired
  private PagmSupport supportPagmThrift;

  @Autowired
  private ContratServiceSupport supportContratServiceThrift;

  @Autowired
  MigrationFormatControlProfil migrationFormatControlProfil;

  @Autowired
  MigrationActionUnitaire migrationActionUnitaire;

  @Autowired
  MigrationPagma migrationPagma;

  @Autowired
  MigrationPagmp migrationPagmp;

  @Autowired
  MigrationPrmd migrationPrmd;

  @Autowired
  MigrationPagm migrationPagm;

  @Autowired
  MigrationContratService migrationContratService;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationDroitsTest.class);

  @Test
  public void migrationFromThriftToCql() {
    try {

      // Actions Unitaires

      migrationActionUnitaire.migrationFromThriftToCql();
      final List<ActionUnitaire> listActionUnitaireThrift = supportActionUnitaireThrift.findAll();
      final List<ActionUnitaire> listActionUnitaireCql = supportActionUnitaireCql.findAll();
      Assert.assertEquals(listActionUnitaireThrift.size(), listActionUnitaireCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listActionUnitaireThrift, listActionUnitaireCql));

      // Format Control Profil

      migrationFormatControlProfil.migrationFromThriftToCql();
      final List<FormatControlProfil> listFormatControlProfilThrift = supportFormatControlProfilThrift.findAll();
      final List<FormatControlProfil> listFormatControlProfilCql = supportFormatControlProfilCql.findAll();
      Assert.assertEquals(listFormatControlProfilThrift.size(), listFormatControlProfilCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listFormatControlProfilThrift, listFormatControlProfilCql));

      // Pagma

      migrationPagma.migrationFromThriftToCql();
      final List<Pagma> listPagmaThrift = supportPagmaThrift.findAll();
      final List<Pagma> listPagmaCql = supportPagmaCql.findAll();
      Assert.assertEquals(listPagmaThrift.size(), listPagmaCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listPagmaThrift, listPagmaCql));

      // Pagmp

      migrationPagmp.migrationFromThriftToCql();
      final List<Pagmp> listPagmpThrift = supportPagmpThrift.findAll();
      final List<Pagmp> listPagmpCql = supportPagmpCql.findAll();
      Assert.assertEquals(listPagmpThrift.size(), listPagmpCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listPagmpThrift, listPagmpCql));
      // Prmd

      migrationPrmd.migrationFromThriftToCql();
      final List<Prmd> listPrmdThrift = supportPrmdThrift.findAll();
      final List<Prmd> listPrmdCql = supportPrmdCql.findAll();
      Assert.assertEquals(listPrmdThrift.size(), listPrmdCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listPrmdThrift, listPrmdCql));

      // Pagm

      migrationPagm.migrationFromThriftToCql();
      final List<PagmCql> listPagmThrift = supportPagmThrift.findAll();
      final List<PagmCql> listPagmCql = supportPagmCql.findAll();
      Assert.assertEquals(listPagmThrift, listPagmCql);
      Assert.assertTrue(CompareUtils.compareListsGeneric(listPagmThrift, listPagmCql));

      // DroitContratService

      migrationContratService.migrationFromThriftToCql();
      final List<ServiceContract> listContratServiceThrift = supportContratServiceThrift.findAll();
      final List<ServiceContract> listContratServiceCql = supportContratServiceCql.findAll();
      Assert.assertEquals(listContratServiceThrift.size(), listContratServiceCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listContratServiceThrift, listContratServiceCql));
    }

    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
    }
  }

  @Test
  public void migrationFromCqlTothrift() {
    // Action Unitaires

    migrationActionUnitaire.migrationFromCqlTothrift();
    final List<ActionUnitaire> listActionUnitaireThrift = supportActionUnitaireThrift.findAll();
    final List<ActionUnitaire> listActionUnitaireCql = supportActionUnitaireCql.findAll();
    Assert.assertEquals(listActionUnitaireThrift.size(), listActionUnitaireCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listActionUnitaireThrift, listActionUnitaireCql));

    // Format Control Profil

    migrationFormatControlProfil.migrationFromCqlTothrift();
    final List<FormatControlProfil> listFormatControlProfilThrift = supportFormatControlProfilThrift.findAll();
    final List<FormatControlProfil> listFormatControlProfilCql = supportFormatControlProfilCql.findAll();
    Assert.assertEquals(listFormatControlProfilThrift.size(), listFormatControlProfilCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listFormatControlProfilThrift, listFormatControlProfilCql));

    // Pagma

    migrationPagma.migrationFromCqlTothrift();
    final List<Pagma> listPagmaThrift = supportPagmaThrift.findAll();
    final List<Pagma> listPagmaCql = supportPagmaCql.findAll();
    Assert.assertEquals(listPagmaThrift.size(), listPagmaCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listPagmaThrift, listPagmaCql));

    // Pagmp

    migrationPagmp.migrationFromCqlTothrift();
    final List<Pagmp> listPagmpThrift = supportPagmpThrift.findAll();
    final List<Pagmp> listPagmpCql = supportPagmpCql.findAll();
    Assert.assertEquals(listPagmaThrift.size(), listPagmpCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listPagmpThrift, listPagmpCql));

    // Prmd

    migrationPrmd.migrationFromCqlTothrift();
    final List<Prmd> listPrmdThrift = supportPrmdThrift.findAll();
    final List<Prmd> listPrmdCql = supportPrmdCql.findAll();
    Assert.assertEquals(listPrmdThrift.size(), listPrmdCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listPrmdThrift, listPrmdCql));

    // Pagm

    migrationPagm.migrationFromCqlTothrift();
    final List<PagmCql> listPagmThrift = supportPagmThrift.findAll();
    final List<PagmCql> listPagmCql = supportPagmCql.findAll();
    Assert.assertEquals(listPrmdThrift.size(), listPrmdCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listPagmThrift, listPagmCql));

    // ContratService

    migrationContratService.migrationFromCqlTothrift();
    final List<ServiceContract> listContratServiceThrift = supportContratServiceThrift.findAll();
    final List<ServiceContract> listContratServiceCql = supportContratServiceCql.findAll();
    Assert.assertEquals(listContratServiceThrift.size(), listContratServiceCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listContratServiceThrift, listContratServiceCql));

  }

}
