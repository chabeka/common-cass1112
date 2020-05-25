/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droits;

import java.util.ArrayList;
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
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
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

  @Autowired
  private ModeApiCqlSupport modeApiCqlSupport;


  final private Javers javers = JaversBuilder
      .javers()
      .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
      .build();

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationContratServiceTest.class);



  String[] listCode = {"CS_SAEL", "CS_NAIA", "CS_MOSAIC", "CS_V2", "CS_RECHERCHE_DOCUMENTAIRE", "CS_CIME", "CS_ANCIEN_SYSTEME"};

  String[] listDescription = {"Contrat de service pour le client SAEL", "Contrat de service pour le client NAIA",
                              "Contrat de service pour MOSAIC", "Contrat de service pour le client V2",
                              "Contrat de service RECHERCHE_DOCUMENTAIRE", "Contrat de service pour le client CIME",
  "accès ancien contrat de service"};

  String[] listLibelle = {"CS_SAEL", "CS_NAIA", "CS_MOSAIC", "CS_V2", "CS_RECHERCHE_DOCUMENTAIRE",
                          "CS_CIME", "accès ancien contrat de service"};


  String[][] listPki = {
                        {"CN=IGC/A", "CN=ACOSS_Reseau_des_URSSAF"},
                        {"CN=IGC/A", "CN=ACOSS_Reseau_des_URSSAF"},
                        {"CN=ACOSS_Reseau_des_URSSAF"},
                        {"CN=IGC/A", "CN=ACOSS_Reseau_des_URSSAF"},
                        {"CN=IGC/A", "CN=ACOSS_Reseau_des_URSSAF"},
                        {"CN=IGC/A", "CN=ACOSS_Reseau_des_URSSAF"},
                        {}

  };

  String[][] listCert = {
                         {"CN=SAEL"},
                         {"CN=NAIA"},
                         {"CN=MOSAIC"},
                         {"CN=SATURNE"},
                         {"CN=RECHERCHE-DOCUMENTAIRE"},
                         {"CN=CIME"},
                         {}

  };

  boolean[] listVerifNommage = {true, true, true, true, true, false, true};

  long[] listDuree = {7200, 7200, 7200, 7200, 7200, 50000, 7200};



  @After
  public void after() throws Exception {

    server.resetData();
    modeApiCqlSupport.initTables(MODE_API.HECTOR);
  }

  /**
   * Migration des données de DroitContratService vers droitcontratservicecql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {
      populateTableThrift();

      migrationContratService.migrationFromThriftToCql(javers);
      final List<ServiceContract> listThrift = supportThrift.findAll();
      // On modifi la liste thrift pour ajouter le listPki au contrat de service CS_ANCIEN_SYSTEME
      for (int i = 0; i < listThrift.size(); i++) {
        if (listThrift.get(i).getCodeClient().equals("CS_ANCIEN_SYSTEME")) {
          final List<String> listPki = new ArrayList<>();
          listPki.add("CN=IGC/A");
          listThrift.get(i).setListPki(listPki);
        }
      }

      final List<ServiceContract> listCql = supportCql.findAll();

      Assert.assertEquals(listThrift.size(), listCode.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      final Diff diff = migrationContratService.compareContratService(listThrift, listCql, javers);
      MigrationContratServiceTest.LOGGER.info(diff.toString());
      Assert.assertTrue(diff.getChanges().isEmpty());
    }
    catch (final Exception ex) {
      MigrationContratServiceTest.LOGGER.debug("exception=" + ex);
    }
  }

  /**
   * Migration des données de droitcontratservicecql vers DroitContratService
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationContratService.migrationFromCqlTothrift(javers);

    final List<ServiceContract> listThrift = supportThrift.findAll();
    final List<ServiceContract> listCql = supportCql.findAll();

    Assert.assertEquals(listCql.size(), listCode.length);
    Assert.assertEquals(listThrift.size(), listCql.size());
    final Diff diff = migrationContratService.compareContratService(listThrift, listCql, javers);
    MigrationContratServiceTest.LOGGER.info(diff.toString());
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
  public void diffAddTest() throws Exception {

    populateTableThrift();
    migrationContratService.migrationFromThriftToCql(javers);

    final List<ServiceContract> listThrift = supportThrift.findAll();
    final ServiceContract contratService = new ServiceContract();
    contratService.setCodeClient("CODECLIENTADD");
    contratService.setDescription("DESCADD");
    supportCql.create(contratService);
    final List<ServiceContract> listCql = supportCql.findAll();
    final Diff diff = migrationContratService.compareContratService(listThrift, listCql, javers);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("NewObject{ new object: fr.urssaf.image.sae.droit.dao.model.ServiceContract/CODECLIENTADD }"));

  }

  @Test
  public void diffDescTest() throws Exception {

    populateTableThrift();
    migrationContratService.migrationFromThriftToCql(javers);

    final List<ServiceContract> listThrift = supportThrift.findAll();
    final List<ServiceContract> listCql = supportCql.findAll();
    listCql.get(0).setDescription("DESCDIFF");

    final Diff diff = migrationContratService.compareContratService(listThrift, listCql, javers);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertEquals(changes,"ValueChange{ 'description' value changed from 'Contrat de service pour le client V2' to 'DESCDIFF' }");
  }
}
