/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support.cql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ContratServiceCqlDatasSupportTest {

  private static final String DESCRIPTION1 = "description1";

  private static final String CODE_CLIENT1 = "codeClient1";

  private static final String LIBELLE1 = "libelle1";

  private static final Long VI_DUREE = Long.valueOf(61);

  private static final String ID_PKI = "id_pki";

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ContratServiceCqlDatasSupportTest.class);

  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  private ContratServiceCqlSupport support;

  @Test
  public void init() {
    try {
      if (cassandraServer.isCassandraStarted()) {
        cassandraServer.resetData(true, MODE_API.DATASTAX);
      }
      Assert.assertTrue(true);

    }
    catch (final Exception e) {
      LOGGER.error("Une erreur s'est produite lors de l'init de cassandra", e.getMessage());
    }
  }
  @After
  public void end() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
  }

  @Test
  public void testCreateFind() {

    final ServiceContract contract = new ServiceContract();

    contract.setCodeClient(CODE_CLIENT1);
    contract.setDescription(DESCRIPTION1);
    contract.setLibelle(LIBELLE1);
    contract.setViDuree(VI_DUREE);
    contract.setIdPki(ID_PKI);
    final List<String> listPki = new ArrayList<>();// AJOUT EC
    listPki.add(ID_PKI);
    contract.setListPki(listPki);
    contract.setVerifNommage(false);

    support.create(contract);

    final ServiceContract res = support.find(CODE_CLIENT1);

    Assert.assertNotNull("le contrat de service ne doit pas être null", res);
    Assert.assertEquals("l'identifiant (libelle) doit être correct",
                        LIBELLE1, res.getLibelle());
    Assert.assertEquals("le code client doit être correct", CODE_CLIENT1, res
                        .getCodeClient());
    Assert.assertEquals("la description doit être correcte", DESCRIPTION1,
                        res.getDescription());
    Assert.assertEquals("la durée doit être correcte", VI_DUREE, res
                        .getViDuree());
    Assert.assertTrue("La liste des pki doit contenir 1 et 1 seul élément",
                      CollectionUtils.isNotEmpty(res.getListPki())
                      && res.getListPki().size() == 1);
    Assert.assertEquals("l'identifiant de la pki doit être correct", ID_PKI,
                        res.getListPki().get(0));
    Assert.assertFalse("la vérification doit être désactivée", res
                       .isVerifNommage());

  }

  @Test
  public void testCreateDelete() {
    final ServiceContract contract = new ServiceContract();
    contract.setCodeClient(CODE_CLIENT1);
    contract.setDescription(DESCRIPTION1);
    contract.setLibelle(LIBELLE1);
    contract.setViDuree(VI_DUREE);
    contract.setIdPki(ID_PKI);
    contract.setVerifNommage(false);

    support.create(contract);

    support.delete(LIBELLE1);

    final ServiceContract res = support.find(LIBELLE1);
    Assert.assertNull(
                      "aucune référence de l'action unitaire ne doit être trouvée", res);
  }

  @Test
  public void testCreateFindAll() {

    ServiceContract contract = new ServiceContract();
    contract.setCodeClient(CODE_CLIENT1);
    contract.setDescription(DESCRIPTION1);
    contract.setLibelle(LIBELLE1);
    contract.setViDuree(VI_DUREE);
    contract.setIdPki("pki 1");
    contract.setVerifNommage(false);

    support.create(contract);

    contract = new ServiceContract();
    contract.setCodeClient("codeClient2");
    contract.setDescription("description2");
    contract.setLibelle("libelle2");
    contract.setViDuree(Long.valueOf(62));
    contract.setIdPki("pki 1");
    contract.setVerifNommage(false);

    support.create(contract);

    contract = new ServiceContract();
    contract.setCodeClient("codeClient3");
    contract.setDescription("description3");
    contract.setLibelle("libelle3");
    contract.setViDuree(Long.valueOf(63));
    contract.setIdPki("pki 1");
    contract.setVerifNommage(false);

    support.create(contract);

    final List<ServiceContract> list = support.findAll(10);

    Assert.assertEquals("vérification du nombre d'enregistrements", 3, list
                        .size());

    for (int i = 1; i < 4; i++) {
      final String codeClient = "codeClient" + i;
      final String description = "description" + i;
      final String libelle = "libelle" + i;
      final Long duree = Long.valueOf(60 + i);

      boolean found = false;
      int index = 0;
      while (!found && index < list.size()) {
        if (codeClient.equals(list.get(index).getCodeClient())) {
          Assert.assertEquals("la description doit etre valide",
                              description, list.get(index).getDescription());
          Assert.assertEquals("le libellé doit etre valide", libelle, list
                              .get(index).getLibelle());
          Assert.assertEquals("la durée doit etre valide", duree, list
                              .get(index).getViDuree());
          found = true;
        }
        index++;
      }

      Assert.assertTrue("le code " + libelle + " doit etre trouvé", found);
    }
  }

  @Test
  public void testUpdate() {

    final ServiceContract contract = new ServiceContract();
    contract.setCodeClient(CODE_CLIENT1);
    contract.setDescription(DESCRIPTION1);
    contract.setLibelle(LIBELLE1);
    contract.setViDuree(VI_DUREE);
    contract.setIdPki("pki 1");
    contract.setVerifNommage(false);
    support.create(contract);

    final ServiceContract contractBis = new ServiceContract();
    contractBis.setCodeClient(CODE_CLIENT1);
    contractBis.setDescription(DESCRIPTION1);
    contractBis.setLibelle(LIBELLE1 + "UPDATED");
    contractBis.setViDuree(VI_DUREE);
    contractBis.setIdPki("pki 1");
    contractBis.setVerifNommage(false);
    support.create(contractBis);

    final ServiceContract contractBd = support.find(CODE_CLIENT1);
    Assert.assertEquals(contractBd, contractBis);
  }

  @Test
  public void testNullCodeValue() {
    try {
      final ServiceContract contract = new ServiceContract();
      contract.setCodeClient(null);
      support.create(contract);
      Assert.assertTrue(false);
    }
    catch (final Exception e) {
      Assert.assertEquals(e.getMessage(), "le code client ne peut etre null");
    }

  }

  @Test
  public void testNullEntity() {
    try {
      support.create(null);
      Assert.assertTrue(false);
    }
    catch (final Exception e) {
      Assert.assertEquals(e.getMessage(), "l'objet contratService ne peut etre null");
    }

  }
}
