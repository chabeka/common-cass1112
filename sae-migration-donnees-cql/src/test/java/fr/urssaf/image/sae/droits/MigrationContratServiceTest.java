/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droits;

import java.util.Arrays;
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
import fr.urssaf.image.sae.droit.MigrationContratService;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.ContratServiceCqlSupport;



/**
 * (AC75095351) Classe de test migration des contrats de service
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationContratServiceTest {

  private static final Date DATE = new Date();

  @Autowired
  private ContratServiceCqlSupport supportCql;

  @Autowired
  private ContratServiceSupport supportThrift;

  @Autowired
  MigrationContratService migrationContratService;

  @Autowired
  private CassandraServerBean server;




  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationContratServiceTest.class);



  String[] listCode = {"CS_SAEL", "CS_NAIA", "CS_MOSAIC", "CS_V2", "CS_RECHERCHE_DOCUMENTAIRE", "CS_CIME"};

  String[] listDescription = {"Contrat de service pour le client SAEL", "Contrat de service pour le client NAIA", "Contrat de service pour MOSAIC",
                              "Contrat de service pour le client V2", "Contrat de service RECHERCHE_DOCUMENTAIRE", "Contrat de service pour le client CIME"};

  String[] listLibelle = {"CS_SAEL", "CS_NAIA", "CS_MOSAIC", "CS_V2", "CS_RECHERCHE_DOCUMENTAIRE", "CS_CIME"};


  String[][] listPki = {
                        {"CN=IGC/A", "CN=ACOSS_Reseau_des_URSSAF"},
                        {"CN=IGC/A", "CN=ACOSS_Reseau_des_URSSAF"},
                        {"CN=ACOSS_Reseau_des_URSSAF"},
                        {"CN=IGC/A", "CN=ACOSS_Reseau_des_URSSAF"},
                        {"CN=IGC/A", "CN=ACOSS_Reseau_des_URSSAF"},
                        {"CN=IGC/A", "CN=ACOSS_Reseau_des_URSSAF"}

  };

  String[][] listCert = {
                         {"CN=SAEL"},
                         {"CN=NAIA"},
                         {"CN=MOSAIC"},
                         {"CN=SATURNE"},
                         {"CN=RECHERCHE-DOCUMENTAIRE"},
                         {"CN=CIME"}

  };

  boolean[] listVerifNommage = {true, true, true, true, true, false};

  long[] listDuree = {7200, 7200, 7200, 7200, 7200, 50000};

  @After
  public void after() throws Exception {

    server.resetData();

  }

  /**
   * Migration des données de DroitContratService vers droitcontratservicecql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {
      server.resetData();
      populateTableThrift();

      migrationContratService.migrationFromThriftToCql();
      final List<ServiceContract> listThrift = supportThrift.findAll();
      final List<ServiceContract> listCql = supportCql.findAll();

      Assert.assertEquals(listThrift.size(), listCode.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      final Diff diff = migrationContratService.compareContratService(listThrift, listCql);
      LOGGER.info(diff.toString());
      Assert.assertTrue(diff.getChanges().isEmpty());
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
    }
  }

  /**
   * Migration des données de droitcontratservicecql vers DroitContratService
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationContratService.migrationFromCqlTothrift();

    final List<ServiceContract> listThrift = supportThrift.findAll();
    final List<ServiceContract> listCql = supportCql.findAll();

    Assert.assertEquals(listCql.size(), listCode.length);
    Assert.assertEquals(listThrift.size(), listCql.size());
    final Diff diff = migrationContratService.compareContratService(listThrift, listCql);
    LOGGER.info(diff.toString());
    Assert.assertTrue(diff.getChanges().isEmpty());
  }


  /**
   * On crée les enregistremenst dans la table droitcontratservicecql
   */

  private void populateTableCql() {
    int i = 0;
    for (final String code : listCode) {
      final ServiceContract contratService = setterContratService(i, code);
      supportCql.create(contratService);
      i++;
    }
  }

  /**
   * On crée les enregistremenst dans la table DroitContratService
   */
  private void populateTableThrift() {
    int i = 0;
    for (final String code : listCode) {
      final ServiceContract contratService = setterContratService(i, code);
      supportThrift.create(contratService, new Date().getTime());
      i++;
    }
  }

  /**
   * Création d'un objet de type ServiceContract enfonction des listes de test
   * 
   * @param i
   * @param code
   * @return
   */
  private ServiceContract setterContratService(final int i, final String code) {
    final ServiceContract contratService = new ServiceContract();
    contratService.setCodeClient(code);
    contratService.setDescription(listDescription[i]);
    contratService.setLibelle(listLibelle[i]);
    contratService.setListPki(Arrays.asList(listPki[i]));
    contratService.setListCertifsClient(Arrays.asList(listCert[i]));
    contratService.setVerifNommage(listVerifNommage[i]);
    contratService.setViDuree(listDuree[i]);
    return contratService;
  }

  @Test
  public void diffAddTest() {

    populateTableThrift();
    migrationContratService.migrationFromThriftToCql();

    final List<ServiceContract> listThrift = supportThrift.findAll();

    final ServiceContract contratService = new ServiceContract();
    contratService.setCodeClient("CODECLIENTADD");
    contratService.setDescription("DESCADD");
    supportCql.create(contratService);
    final List<ServiceContract> listCql = supportCql.findAll();
    final Diff diff = migrationContratService.compareContratService(listThrift, listCql);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("NewObject{ new object: ServiceContract/CODECLIENTADD }"));

  }

  @Test
  public void diffDescTest() {

    populateTableThrift();
    migrationContratService.migrationFromThriftToCql();

    final List<ServiceContract> listThrift = supportThrift.findAll();
    final List<ServiceContract> listCql = supportCql.findAll();
    listCql.get(0).setDescription("DESCDIFF");
    final Diff diff = migrationContratService.compareContratService(listThrift, listCql);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("ValueChange{ 'description' value changed from 'Contrat de service pour le client SAEL' to 'DESCDIFF' }"));

  }
}
