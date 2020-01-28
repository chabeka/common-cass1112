/**
 * 
 */
package fr.urssaf.image.sae.droit.service.cql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.modelcql.PagmCql;
import fr.urssaf.image.sae.droit.dao.serializer.exception.ActionUnitaireReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmaReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmpReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.cql.ActionUnitaireCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.ContratServiceCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.FormatControlProfilCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmaCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmfCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmpCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PrmdCqlSupport;
import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.ContratServiceReferenceException;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeContratService;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaeDroitsEtFormat;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.model.SaePagma;
import fr.urssaf.image.sae.droit.model.SaePagmf;
import fr.urssaf.image.sae.droit.model.SaePagmp;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.SaeDroitService;
import fr.urssaf.image.sae.droit.utils.Constantes;

/**
 * Classe Test de la classe {@link SaeDroitService} en mode Cql
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)

public class SaeDroitServiceCqlDataTest {

  private static final String BEAN1 = "bean1";

  private static final String DESCRIPTION_ACTION_2 = "description action unitaire 2";

  private static final String DESCRIPTION_ACTION_1 = "description action unitaire 1";

  private static final String DESCRIPTION_PAGMP = "description pagmp";

  private static final String DESCRIPTION_PAGMF = "description pagmf";

  private static final String DESCRIPTION_PAGMP_2 = "description pagmp 2";

  private static final String CODE_PAGMP = "pagmpCode";

  private static final String CODE_PAGMA = "pagmaCode";

  private static final String CODE_PAGMF = "pagmfCode";

  private static final String CODE_PAGMF_2 = "pagmfCode2";

  private static final String DESCRIPTION_PAGM = "description pagm";

  private static final String CODE_PAGM = "pagmCode";

  private static final String CODE_PAGMP_2 = "pagmpCode2";

  private static final String CODE_PAGMA_2 = "pagmaCode2";

  private static final String DESCRIPTION_PAGM_2 = "description pagm 2";

  private static final String CODE_PAGM_2 = "pagmCode2";

  private static final Long DUREE_CONTRAT = Long.valueOf(60);

  private static final String LIBELLE_CONTRAT = "libellé contrat";

  private static final String DESCRIPTION_CONTRAT = "description contrat";

  private static final String CODE_CLIENT = "clientCode";

  private static final String CODE_ACTION_1 = "action1code";

  private static final String CODE_ACTION_2 = "action2code";

  private static final String CODE_PRMD = "prmdCode";

  private static final String CODE_PRMD_2 = "prmdCode2";

  private static final String DESCRIPTION_PRMD = "description prmd";

  private static final String LUCENE_PRMD = "lucene prmd";

  private static final String DESCRIPTION_PRMD_2 = "description prmd 2";

  private static final String LUCENE_PRMD_2 = "lucene prmd 2";

  private static final String ID_PKI = "pki";

  private final String cfName = Constantes.CF_DROIT_CONTRAT_SERVICE;

  @Autowired
  // @Qualifier("saeDroitServiceFacadeImpl")
  private SaeDroitService service;

  @Autowired
  private ContratServiceCqlSupport contratCqlSupport;

  @Autowired
  private PagmCqlSupport pagmCqlSupport;

  @Autowired
  private PagmpCqlSupport pagmpCqlSupport;

  @Autowired
  private PagmaCqlSupport pagmaCqlSupport;

  @Autowired
  private PagmfCqlSupport pagmfCqlSupport;

  @Autowired
  private PrmdCqlSupport prmdCqlSupport;

  @Autowired
  private FormatControlProfilCqlSupport formatControlProfilCqlSupport;

  @Autowired
  private ActionUnitaireCqlSupport actionCqlSupport;

  @Autowired
  private CassandraServerBean server;

  @Before
  public void setup() throws Exception {
    final HashMap<String, String> modesApiTest = new HashMap<>();
    modesApiTest.put(cfName, "DATASTAX");
    modesApiTest.put("droitactionunitaire", "DATASTAX");
    modesApiTest.put("droitformatcontrolprofil", "DATASTAX");
    modesApiTest.put("droitpagmf", "DATASTAX");
    modesApiTest.put("droitpagmp", "DATASTAX");
    modesApiTest.put("droitpagma", "DATASTAX");
    modesApiTest.put("droitpagm", "DATASTAX");
    modesApiTest.put("droitprmd", "DATASTAX");
    ModeGestionAPI.setListeCfsModes(modesApiTest);
  }

  @After
  public void after() throws Exception {
    // server.resetData();

  }
  @Test
  public void testServiceContratServiceInexistant() {
    final boolean exists = service.contratServiceExists(CODE_CLIENT);

    Assert.assertFalse("le contrat de service n'existe pas", exists);
  }

  @Test
  public void testServiceContratServiceExistant() {
    creationContrat();
    final boolean exists = service.contratServiceExists(CODE_CLIENT);
    Assert.assertTrue("le contrat de service existe", exists);
  }

  @Test(expected = ContratServiceNotFoundException.class)
  public void testContratServiceInexistant()
      throws ContratServiceNotFoundException, PagmNotFoundException, FormatControlProfilNotFoundException {

    service.loadSaeDroits("test1", Arrays.asList(new String[] { "pagm1" }));

  }

  @Test(expected = PagmNotFoundException.class)
  public void testPagmInexistant() throws ContratServiceNotFoundException,
  PagmNotFoundException, FormatControlProfilNotFoundException {
    creationContrat();

    service.loadSaeDroits(CODE_CLIENT, Arrays
                          .asList(new String[] { "pagm1" }));
  }

  @Test(expected = PagmaReferenceException.class)
  public void testPagmaInexistant() throws ContratServiceNotFoundException,
  PagmNotFoundException, FormatControlProfilNotFoundException {
    creationContrat();
    creationPagm();

    service.loadSaeDroits(CODE_CLIENT, Arrays
                          .asList(new String[] { CODE_PAGM }));
  }

  @Test(expected = PagmpReferenceException.class)
  public void testPagmpInexistant() throws ContratServiceNotFoundException,
  PagmNotFoundException, FormatControlProfilNotFoundException {
    creationContrat();
    creationPagm();
    creationPagma();

    service.loadSaeDroits(CODE_CLIENT, Arrays
                          .asList(new String[] { CODE_PAGM }));
  }

  @Test(expected = PrmdReferenceException.class)
  public void testPrmdInexistant() throws ContratServiceNotFoundException,
  PagmNotFoundException, FormatControlProfilNotFoundException {
    creationContrat();
    creationPagm();
    creationPagma();
    creationPagmp();

    service.loadSaeDroits(CODE_CLIENT, Arrays
                          .asList(new String[] { CODE_PAGM }));
  }

  @Test(expected = ActionUnitaireReferenceException.class)
  public void testActionInexistant() throws ContratServiceNotFoundException,
  PagmNotFoundException, FormatControlProfilNotFoundException {
    creationContrat();
    creationPagm();
    creationPagma();
    creationPagmp();
    creationPrmd();

    service.loadSaeDroits(CODE_CLIENT, Arrays
                          .asList(new String[] { CODE_PAGM }));
  }

  @Test
  public void testSucces() throws ContratServiceNotFoundException,
  PagmNotFoundException, FormatControlProfilNotFoundException {
    creationContrat();
    creationPagm();
    creationPagma();
    creationPagmp();
    creationPagmf();

    final Prmd prmd = creationPrmd();
    creationActionUnitaire();

    final SaeDroitsEtFormat saeDroitsEtFormat = service.loadSaeDroits(CODE_CLIENT, Arrays
                                                                      .asList(new String[] { CODE_PAGM }));
    Assert.assertNotNull(saeDroitsEtFormat);
    final SaeDroits droits = saeDroitsEtFormat.getSaeDroits();

    //      SaeDroits droits = service.loadSaeDroits(CODE_CLIENT, Arrays
    //            .asList(new String[] { CODE_PAGM }));
    Assert.assertTrue("l'action unitaire doit exister : " + CODE_ACTION_1,
                      droits.containsKey(CODE_ACTION_1));

    final List<SaePrmd> saePrmds = droits.get(CODE_ACTION_1);

    Assert.assertEquals("1 seul PRMD", 1, saePrmds.size());

    comparerPrmd(prmd, saePrmds.get(0).getPrmd());

    final List<FormatControlProfil> formats = saeDroitsEtFormat.getListFormatControlProfil();
    Assert.assertEquals("Plusieurs formatControlProfil", 1, formats.size());

  }

  /**
   * @param prmd
   * @param prmd2
   */
  private void comparerPrmd(final Prmd reference, final Prmd valeur) {
    Assert.assertEquals("code PRMD correct", reference.getCode(), valeur
                        .getCode());
    Assert.assertEquals("description PRMD correct", reference
                        .getDescription(), valeur.getDescription());
    Assert.assertEquals("lucène PRMD correct", reference.getLucene(), valeur
                        .getLucene());

  }

  @Test
  public void testSuccesPlusieursPagm()
      throws ContratServiceNotFoundException, PagmNotFoundException, FormatControlProfilNotFoundException {
    creationContrat();
    creationPagm();
    creationPagm2();
    creationPagma();
    creationPagma2();
    creationPagmp();
    creationPagmp2();
    final Prmd prmd1 = creationPrmd();
    final Prmd prmd2 = creationPrmd2();
    creationActionUnitaire();
    creationActionUnitaire2();
    creationPagmf();
    creationPagmf2();

    //      SaeDroits droits = service.loadSaeDroits(CODE_CLIENT, Arrays
    //            .asList(new String[] { CODE_PAGM, CODE_PAGM_2 }));

    final SaeDroitsEtFormat saeDroitsEtFormat = service.loadSaeDroits(CODE_CLIENT, Arrays
                                                                      .asList(new String[] { CODE_PAGM, CODE_PAGM_2 }));
    Assert.assertNotNull(saeDroitsEtFormat);
    final SaeDroits droits = saeDroitsEtFormat.getSaeDroits();

    Assert.assertEquals("2 clés présentes ", 2, droits.keySet().size());

    Assert.assertTrue("l'action unitaire doit exister : " + CODE_ACTION_1,
                      droits.containsKey(CODE_ACTION_1));

    Assert.assertTrue("l'action unitaire doit exister : " + CODE_ACTION_2,
                      droits.containsKey(CODE_ACTION_2));

    List<SaePrmd> saePrmds = droits.get(CODE_ACTION_1);

    Assert.assertEquals("2 PRMD", 2, saePrmds.size());

    for (final SaePrmd saePrmd : saePrmds) {
      if (saePrmd.getPrmd().getCode().equals(prmd1.getCode())) {
        comparerPrmd(prmd1, saePrmd.getPrmd());
      } else if (saePrmd.getPrmd().getCode().equals(prmd2.getCode())) {
        comparerPrmd(prmd2, saePrmd.getPrmd());
      } else {
        Assert.fail("cas non géré");
      }
    }

    saePrmds = droits.get(CODE_ACTION_2);
    Assert.assertEquals("1 seul PRMD", 1, saePrmds.size());
    comparerPrmd(prmd2, saePrmds.get(0).getPrmd());

    final List<FormatControlProfil> formats = saeDroitsEtFormat.getListFormatControlProfil();
    Assert.assertEquals("1 formatControlProfil", 2, formats.size());
  }

  @Test(expected = ContratServiceReferenceException.class)
  public void testGetServiceContractInexistant() {

    creationContrat();

    service.getServiceContract("code inexistant");

  }

  @Test
  public void testGetServiceContractSuccess() {

    creationContrat();

    final ServiceContract contract = service.getServiceContract(CODE_CLIENT);

    Assert.assertEquals("description du contrat doit être correcte",
                        DESCRIPTION_CONTRAT, contract.getDescription());
    Assert.assertEquals("libellé du contrat doit être correcte",
                        LIBELLE_CONTRAT, contract.getLibelle());
    Assert.assertEquals("durée du contrat doit être correcte", DUREE_CONTRAT,
                        contract.getViDuree());
    Assert.assertEquals("code client doit être correcte", CODE_CLIENT,
                        contract.getCodeClient());

  }

  private void creationActionUnitaire() {
    final ActionUnitaire actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode(CODE_ACTION_1);
    actionUnitaire.setDescription(DESCRIPTION_ACTION_1);

    actionCqlSupport.create(actionUnitaire);
  }

  private void creationActionUnitaire2() {
    final ActionUnitaire actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode(CODE_ACTION_2);
    actionUnitaire.setDescription(DESCRIPTION_ACTION_2);

    actionCqlSupport.create(actionUnitaire);
  }

  private void creationContrat() {
    final ServiceContract contract = new ServiceContract();
    contract.setCodeClient(CODE_CLIENT);
    contract.setDescription(DESCRIPTION_CONTRAT);
    contract.setLibelle(LIBELLE_CONTRAT);
    contract.setViDuree(DUREE_CONTRAT);
    contract.setIdPki(ID_PKI);
    contract.setVerifNommage(false);
    contratCqlSupport.create(contract);
  }

  private void creationPagm() {
    final PagmCql pagmCql = new PagmCql();
    pagmCql.setIdClient(CODE_CLIENT);
    pagmCql.setCode(CODE_PAGM);
    pagmCql.setDescription(DESCRIPTION_PAGM);
    pagmCql.setPagma(CODE_PAGMA);
    pagmCql.setPagmp(CODE_PAGMP);
    pagmCql.setPagmf(CODE_PAGMF);

    pagmCqlSupport.create(pagmCql);

  }

  private void creationPagm2() {
    final PagmCql pagmCql = new PagmCql();
    pagmCql.setIdClient(CODE_CLIENT);
    pagmCql.setCode(CODE_PAGM_2);
    pagmCql.setDescription(DESCRIPTION_PAGM_2);
    pagmCql.setPagma(CODE_PAGMA_2);
    pagmCql.setPagmp(CODE_PAGMP_2);
    pagmCql.setPagmf(CODE_PAGMF_2);

    pagmCqlSupport.create(pagmCql);

  }

  private void creationPagma() {
    final Pagma pagma = new Pagma();

    pagma.setActionUnitaires(Arrays.asList(new String[] { CODE_ACTION_1 }));
    pagma.setCode(CODE_PAGMA);

    pagmaCqlSupport.create(pagma);
  }

  private void creationPagma2() {
    final Pagma pagma = new Pagma();

    pagma.setActionUnitaires(Arrays.asList(new String[] { CODE_ACTION_1,
                                                          CODE_ACTION_2 }));
    pagma.setCode(CODE_PAGMA_2);

    pagmaCqlSupport.create(pagma);
  }

  private void creationPagmp() {
    final Pagmp pagmp = new Pagmp();
    pagmp.setCode(CODE_PAGMP);
    pagmp.setDescription(DESCRIPTION_PAGMP);
    pagmp.setPrmd(CODE_PRMD);

    pagmpCqlSupport.create(pagmp);
  }

  private void creationPagmp2() {
    final Pagmp pagmp = new Pagmp();
    pagmp.setCode(CODE_PAGMP_2);
    pagmp.setDescription(DESCRIPTION_PAGMP_2);
    pagmp.setPrmd(CODE_PRMD_2);

    pagmpCqlSupport.create(pagmp);
  }

  private Prmd creationPrmd() {
    final Prmd prmd = new Prmd();
    prmd.setCode(CODE_PRMD);
    prmd.setDescription(DESCRIPTION_PRMD);
    prmd.setLucene(LUCENE_PRMD);
    prmd.setBean(BEAN1);
    prmd.setMetadata(new HashMap<String, List<String>>());

    prmdCqlSupport.create(prmd);

    return prmd;
  }

  private Prmd creationPrmd2() {
    final Prmd prmd = new Prmd();
    prmd.setCode(CODE_PRMD_2);
    prmd.setDescription(DESCRIPTION_PRMD_2);
    prmd.setLucene(LUCENE_PRMD_2);
    prmd.setBean(BEAN1);
    prmd.setMetadata(new HashMap<String, List<String>>());

    prmdCqlSupport.create(prmd);

    return prmd;
  }

  private void creationPagmf() throws FormatControlProfilNotFoundException {
    creationFormatControlProfil();

    final Pagmf pagmf = new Pagmf();

    pagmf.setCodePagmf(CODE_PAGMF);
    pagmf.setDescription(DESCRIPTION_PAGMF);
    pagmf.setCodeFormatControlProfil("formatProfile");

    pagmfCqlSupport.create(pagmf);
  }

  private void creationFormatControlProfil() {
    final FormatControlProfil format = new FormatControlProfil();
    format.setDescription("description");
    format.setFormatCode("formatProfile");
    final FormatProfil controlProfil = creationFormatProfil();
    format.setControlProfil(controlProfil);

    formatControlProfilCqlSupport.create(format);
  }

  private FormatProfil creationFormatProfil() {
    final FormatProfil formatProfil = new FormatProfil();
    formatProfil.setFileFormat("fmt/354");
    formatProfil.setFormatIdentification(true);
    formatProfil.setFormatValidation(true);
    formatProfil.setFormatValidationMode("STRICT");
    return formatProfil;
  }

  private void creationPagmf2() throws FormatControlProfilNotFoundException {
    creationFormatControlProfil2();

    final Pagmf pagmf = new Pagmf();

    pagmf.setCodePagmf(CODE_PAGMF_2);
    pagmf.setDescription(DESCRIPTION_PAGMF);
    pagmf.setCodeFormatControlProfil("formatProfile");

    pagmfCqlSupport.create(pagmf);
  }

  private void creationFormatControlProfil2() {
    final FormatControlProfil format = new FormatControlProfil();
    format.setDescription("description");
    format.setFormatCode("formatProfile");
    final FormatProfil controlProfil = creationFormatProfil2();
    format.setControlProfil(controlProfil);

    formatControlProfilCqlSupport.create(format);
  }

  private FormatProfil creationFormatProfil2() {
    final FormatProfil formatProfil = new FormatProfil();
    formatProfil.setFileFormat("fmt/354");
    formatProfil.setFormatIdentification(true);
    formatProfil.setFormatValidation(true);
    formatProfil.setFormatValidationMode("MONITOR");
    return formatProfil;
  }

  // identique à celui de la classe support ContratServiceDataSupportTest
  @Test
  public void findAllCsTest() {
    ServiceContract contract = new ServiceContract();
    contract.setCodeClient("codeClient1");
    contract.setDescription("description1");
    contract.setLibelle("libelle1");
    contract.setViDuree(Long.valueOf(61));
    contract.setIdPki("pki 1");
    contract.setVerifNommage(false);

    contratCqlSupport.create(contract);

    contract = new ServiceContract();
    contract.setCodeClient("codeClient2");
    contract.setDescription("description2");
    contract.setLibelle("libelle2");
    contract.setViDuree(Long.valueOf(62));
    contract.setIdPki("pki 1");
    contract.setVerifNommage(false);

    contratCqlSupport.create(contract);

    contract = new ServiceContract();
    contract.setCodeClient("codeClient3");
    contract.setDescription("description3");
    contract.setLibelle("libelle3");
    contract.setViDuree(Long.valueOf(63));
    contract.setIdPki("pki 1");
    contract.setVerifNommage(false);

    contratCqlSupport.create(contract);

    final List<ServiceContract> list = contratCqlSupport.findAll(5);

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
  public void getFullCsTest() throws FormatControlProfilNotFoundException {
    creationContrat();
    creationPagm();
    creationPagma();
    creationPagmp();
    creationPagmf();
    creationPrmd();
    final SaeContratService fullCs = service.getFullContratService(CODE_CLIENT);

    Assert.assertEquals(fullCs.getCodeClient(),
                        CODE_CLIENT);
    Assert.assertEquals(fullCs.getDescription(),
                        DESCRIPTION_CONTRAT);
    Assert.assertEquals(fullCs.getLibelle(),
                        LIBELLE_CONTRAT);
    Assert.assertEquals(fullCs.getViDuree(),
                        DUREE_CONTRAT);
    Assert.assertNotNull(fullCs.getSaePagms());
    for (final SaePagm pagm : fullCs.getSaePagms()) {
      Assert.assertEquals(pagm.getCode(), CODE_PAGM);
      Assert.assertEquals(pagm.getDescription(), DESCRIPTION_PAGM);

      final SaePagma saePagma = new SaePagma();
      saePagma.setActionUnitaires(Arrays
                                  .asList(new String[] { CODE_ACTION_1 }));
      saePagma.setCode(CODE_PAGMA);
      pagm.getPagma().equals(saePagma);
      Assert.assertEquals(pagm.getPagma(), saePagma);

      final SaePagmp saePagmp = new SaePagmp();
      saePagmp.setCode(CODE_PAGMP);
      saePagmp.setDescription(DESCRIPTION_PAGMP);

      saePagmp.setPrmd(CODE_PRMD);
      Assert.assertEquals(pagm.getPagmp(), saePagmp);

      final SaePagmf saePagmf = new SaePagmf();
      saePagmf.setCodePagmf(CODE_PAGMF);
      saePagmf.setDescription(DESCRIPTION_PAGMF);
      saePagmf.setFormatProfile("formatProfile");
      Assert.assertEquals(pagm.getPagmf(), saePagmf);

    }

  }


  @Test
  public void egaliteSaePagm() {

    // Test égalité OK
    final SaePagm saePagm1 = new SaePagm();
    saePagm1.setCode("codePagm");
    saePagm1.setDescription("description1");
    final SaePagma pagma = new SaePagma();
    final List<String> actionUnitaires = new ArrayList<>();
    actionUnitaires.add("action1");
    actionUnitaires.add("action2");
    pagma.setActionUnitaires(actionUnitaires);
    pagma.setCode("codePagma");
    saePagm1.setPagma(pagma);
    final SaePagmf pagmf = new SaePagmf();
    pagmf.setCodePagmf("codePagmf");
    pagmf.setDescription("description");
    pagmf.setFormatProfile("formatProfile");
    saePagm1.setPagmf(pagmf);
    final SaePagmp pagmp = new SaePagmp();
    pagmp.setCode("codePagmp");
    pagmp.setDescription("description");
    pagmp.setPrmd("prmd");
    saePagm1.setPagmp(pagmp);
    final Map<String,String> parametres = new HashMap<>();
    parametres.put("key", "value");
    saePagm1.setParametres(parametres);

    final SaePagm saePagm2 = new SaePagm();
    saePagm2.setCode("codePagm");
    saePagm2.setDescription("description1");
    final SaePagma pagma2 = new SaePagma();
    final List<String> actionUnitaires2 = new ArrayList<>();
    actionUnitaires2.add("action1");
    actionUnitaires2.add("action2");
    pagma2.setActionUnitaires(actionUnitaires2);
    pagma2.setCode("codePagma");
    saePagm2.setPagma(pagma2);
    final SaePagmf pagmf2 = new SaePagmf();
    pagmf2.setCodePagmf("codePagmf");
    pagmf2.setDescription("description");
    pagmf2.setFormatProfile("formatProfile");
    saePagm2.setPagmf(pagmf2);
    final SaePagmp pagmp2 = new SaePagmp();
    pagmp2.setCode("codePagmp");
    pagmp2.setDescription("description");
    pagmp2.setPrmd("prmd");
    saePagm2.setPagmp(pagmp2);
    final Map<String,String> parametres2 = new HashMap<>();
    parametres2.put("key", "value");
    saePagm2.setParametres(parametres2);

    Assert.assertEquals(saePagm1, saePagm2);
    Assert.assertTrue("les 2 saePagm doivent être identiques", saePagm1.equals(saePagm2));

    // Test Pagmf null
    saePagm1.setPagmf(null);
    Assert.assertFalse("les 2 saePagm doivent être différents", saePagm1.equals(saePagm2));
    saePagm2.setPagmf(null);
    Assert.assertTrue("les 2 saePagm doivent être identiques", saePagm1.equals(saePagm2));

    // Codes Différents
    saePagm1.setPagmf(pagmf);
    saePagm2.setPagmf(pagmf2);
    saePagm1.setCode("codeDiff");
    Assert.assertFalse("les 2 saePagm doivent être différents", saePagm1.equals(saePagm2));

    // Descriptions différentes
    saePagm1.setCode("codePagm");
    saePagm1.setDescription("descriptionDiff");
    Assert.assertFalse("les 2 saePagm doivent être différents", saePagm1.equals(saePagm2));

    // Pagma différents
    saePagm1.setDescription("description");
    pagma.setCode("codeDiff");
    saePagm1.setPagma(pagma);
    Assert.assertFalse("les 2 saePagm doivent être différents", saePagm1.equals(saePagm2));

  }
}
