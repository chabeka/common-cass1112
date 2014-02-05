/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.serializer.exception.ActionUnitaireReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmaReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmpReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmfSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.ContratServiceReferenceException;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmfNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeContratService;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaeDroitsEtFormat;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.model.SaePagma;
import fr.urssaf.image.sae.droit.model.SaePagmf;
import fr.urssaf.image.sae.droit.model.SaePagmp;
import fr.urssaf.image.sae.droit.model.SaePrmd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SaeDroitServiceDataTest {

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

   @Autowired
   private SaeDroitService service;

   @Autowired
   private CassandraServerBean cassandraServer;

   @Autowired
   private ContratServiceSupport contratSupport;

   @Autowired
   private PagmSupport pagmSupport;

   @Autowired
   private PagmpSupport pagmpSupport;

   @Autowired
   private PagmaSupport pagmaSupport;
   
   @Autowired
   private PagmfSupport pagmfSupport;

   @Autowired
   private PrmdSupport prmdSupport;
   
   @Autowired
   private FormatControlProfilSupport formatControlProfilSupport;

   @Autowired
   private ActionUnitaireSupport actionSupport;

   @After
   public void end() throws Exception {
      cassandraServer.resetData();
   }

   @Test
   public void testServiceContratServiceInexistant() {
      boolean exists = service.contratServiceExists(CODE_CLIENT);

      Assert.assertFalse("le contrat de service n'existe pas", exists);
   }

   @Test
   public void testServiceContratServiceExistant() {
      creationContrat();
      boolean exists = service.contratServiceExists(CODE_CLIENT);
      Assert.assertTrue("le contrat de service existe", exists);
   }

   @Test(expected = ContratServiceNotFoundException.class)
   public void testContratServiceInexistant()
         throws ContratServiceNotFoundException, PagmfNotFoundException, FormatControlProfilNotFoundException {

      service.loadSaeDroits("test1", Arrays.asList(new String[] { "pagm1" }));

   }

   @Test(expected = PagmfNotFoundException.class)
   public void testPagmInexistant() throws ContratServiceNotFoundException,
         PagmfNotFoundException, FormatControlProfilNotFoundException {
      creationContrat();

      service.loadSaeDroits(CODE_CLIENT, Arrays
            .asList(new String[] { "pagm1" }));
   }

   @Test(expected = PagmaReferenceException.class)
   public void testPagmaInexistant() throws ContratServiceNotFoundException,
         PagmfNotFoundException, FormatControlProfilNotFoundException {
      creationContrat();
      creationPagm();

      service.loadSaeDroits(CODE_CLIENT, Arrays
            .asList(new String[] { CODE_PAGM }));
   }

   @Test(expected = PagmpReferenceException.class)
   public void testPagmpInexistant() throws ContratServiceNotFoundException,
         PagmfNotFoundException, FormatControlProfilNotFoundException {
      creationContrat();
      creationPagm();
      creationPagma();

      service.loadSaeDroits(CODE_CLIENT, Arrays
            .asList(new String[] { CODE_PAGM }));
   }

   @Test(expected = PrmdReferenceException.class)
   public void testPrmdInexistant() throws ContratServiceNotFoundException,
         PagmfNotFoundException, FormatControlProfilNotFoundException {
      creationContrat();
      creationPagm();
      creationPagma();
      creationPagmp();

      service.loadSaeDroits(CODE_CLIENT, Arrays
            .asList(new String[] { CODE_PAGM }));
   }

   @Test(expected = ActionUnitaireReferenceException.class)
   public void testActionInexistant() throws ContratServiceNotFoundException,
         PagmfNotFoundException, FormatControlProfilNotFoundException {
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
         PagmfNotFoundException, FormatControlProfilNotFoundException {
      creationContrat();
      creationPagm();
      creationPagma();
      creationPagmp();
      creationPagmf();
      
      Prmd prmd = creationPrmd();
      creationActionUnitaire();

      SaeDroitsEtFormat saeDroitsEtFormat = service.loadSaeDroits(CODE_CLIENT, Arrays
            .asList(new String[] { CODE_PAGM }));
      Assert.assertNotNull(saeDroitsEtFormat);
      SaeDroits droits = saeDroitsEtFormat.getSaeDroits();
      
//      SaeDroits droits = service.loadSaeDroits(CODE_CLIENT, Arrays
//            .asList(new String[] { CODE_PAGM }));
      Assert.assertTrue("l'action unitaire doit exister : " + CODE_ACTION_1,
            droits.containsKey(CODE_ACTION_1));

      List<SaePrmd> saePrmds = droits.get(CODE_ACTION_1);

      Assert.assertEquals("1 seul PRMD", 1, saePrmds.size());

      comparerPrmd(prmd, saePrmds.get(0).getPrmd());
      
      List<FormatControlProfil> formats = saeDroitsEtFormat.getListFormatControlProfil();
      Assert.assertEquals("Plusieurs formatControlProfil", 1, formats.size());

   }

   /**
    * @param prmd
    * @param prmd2
    */
   private void comparerPrmd(Prmd reference, Prmd valeur) {
      Assert.assertEquals("code PRMD correct", reference.getCode(), valeur
            .getCode());
      Assert.assertEquals("description PRMD correct", reference
            .getDescription(), valeur.getDescription());
      Assert.assertEquals("lucène PRMD correct", reference.getLucene(), valeur
            .getLucene());

   }

   @Test
   public void testSuccesPlusieursPagm()
         throws ContratServiceNotFoundException, PagmfNotFoundException, FormatControlProfilNotFoundException {
      creationContrat();
      creationPagm();
      creationPagm2();
      creationPagma();
      creationPagma2();
      creationPagmp();
      creationPagmp2();
      Prmd prmd1 = creationPrmd();
      Prmd prmd2 = creationPrmd2();
      creationActionUnitaire();
      creationActionUnitaire2();
      creationPagmf();
      creationPagmf2();

//      SaeDroits droits = service.loadSaeDroits(CODE_CLIENT, Arrays
//            .asList(new String[] { CODE_PAGM, CODE_PAGM_2 }));
      
      SaeDroitsEtFormat saeDroitsEtFormat = service.loadSaeDroits(CODE_CLIENT, Arrays
            .asList(new String[] { CODE_PAGM, CODE_PAGM_2 }));
      Assert.assertNotNull(saeDroitsEtFormat);
      SaeDroits droits = saeDroitsEtFormat.getSaeDroits();

      Assert.assertEquals("2 clés présentes ", 2, droits.keySet().size());

      Assert.assertTrue("l'action unitaire doit exister : " + CODE_ACTION_1,
            droits.containsKey(CODE_ACTION_1));

      Assert.assertTrue("l'action unitaire doit exister : " + CODE_ACTION_2,
            droits.containsKey(CODE_ACTION_2));

      List<SaePrmd> saePrmds = droits.get(CODE_ACTION_1);

      Assert.assertEquals("2 PRMD", 2, saePrmds.size());

      for (SaePrmd saePrmd : saePrmds) {
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
      
      List<FormatControlProfil> formats = saeDroitsEtFormat.getListFormatControlProfil();
      Assert.assertEquals("1 formatControlProfil", 2, formats.size());
   }

   @Test(expected = ContratServiceReferenceException.class)
   public void testGetServiceContractInexistant() {

      creationContrat();

      this.service.getServiceContract("code inexistant");

   }

   @Test
   public void testGetServiceContractSuccess() {

      creationContrat();

      ServiceContract contract = this.service.getServiceContract(CODE_CLIENT);

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
      ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode(CODE_ACTION_1);
      actionUnitaire.setDescription(DESCRIPTION_ACTION_1);

      actionSupport.create(actionUnitaire, new Date().getTime());
   }

   private void creationActionUnitaire2() {
      ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode(CODE_ACTION_2);
      actionUnitaire.setDescription(DESCRIPTION_ACTION_2);

      actionSupport.create(actionUnitaire, new Date().getTime());
   }

   private void creationContrat() {
      ServiceContract contract = new ServiceContract();
      contract.setCodeClient(CODE_CLIENT);
      contract.setDescription(DESCRIPTION_CONTRAT);
      contract.setLibelle(LIBELLE_CONTRAT);
      contract.setViDuree(DUREE_CONTRAT);
      contract.setIdPki(ID_PKI);
      contract.setVerifNommage(false);
      contratSupport.create(contract, new Date().getTime());
   }

   private void creationPagm() {
      Pagm pagm = new Pagm();
      pagm.setCode(CODE_PAGM);
      pagm.setDescription(DESCRIPTION_PAGM);
      pagm.setPagma(CODE_PAGMA);
      pagm.setPagmp(CODE_PAGMP);
      pagm.setPagmf(CODE_PAGMF);

      pagmSupport.create(CODE_CLIENT, pagm, new Date().getTime());

   }

   private void creationPagm2() {
      Pagm pagm = new Pagm();
      pagm.setCode(CODE_PAGM_2);
      pagm.setDescription(DESCRIPTION_PAGM_2);
      pagm.setPagma(CODE_PAGMA_2);
      pagm.setPagmp(CODE_PAGMP_2);
      pagm.setPagmf(CODE_PAGMF_2);

      pagmSupport.create(CODE_CLIENT, pagm, new Date().getTime());

   }

   private void creationPagma() {
      Pagma pagma = new Pagma();

      pagma.setActionUnitaires(Arrays.asList(new String[] { CODE_ACTION_1 }));
      pagma.setCode(CODE_PAGMA);

      pagmaSupport.create(pagma, new Date().getTime());
   }

   private void creationPagma2() {
      Pagma pagma = new Pagma();

      pagma.setActionUnitaires(Arrays.asList(new String[] { CODE_ACTION_1,
            CODE_ACTION_2 }));
      pagma.setCode(CODE_PAGMA_2);

      pagmaSupport.create(pagma, new Date().getTime());
   }

   private void creationPagmp() {
      Pagmp pagmp = new Pagmp();
      pagmp.setCode(CODE_PAGMP);
      pagmp.setDescription(DESCRIPTION_PAGMP);
      pagmp.setPrmd(CODE_PRMD);

      pagmpSupport.create(pagmp, new Date().getTime());
   }

   private void creationPagmp2() {
      Pagmp pagmp = new Pagmp();
      pagmp.setCode(CODE_PAGMP_2);
      pagmp.setDescription(DESCRIPTION_PAGMP_2);
      pagmp.setPrmd(CODE_PRMD_2);

      pagmpSupport.create(pagmp, new Date().getTime());
   }

   private Prmd creationPrmd() {
      Prmd prmd = new Prmd();
      prmd.setCode(CODE_PRMD);
      prmd.setDescription(DESCRIPTION_PRMD);
      prmd.setLucene(LUCENE_PRMD);
      prmd.setBean(BEAN1);
      prmd.setMetadata(new HashMap<String, List<String>>());

      prmdSupport.create(prmd, new Date().getTime());

      return prmd;
   }

   private Prmd creationPrmd2() {
      Prmd prmd = new Prmd();
      prmd.setCode(CODE_PRMD_2);
      prmd.setDescription(DESCRIPTION_PRMD_2);
      prmd.setLucene(LUCENE_PRMD_2);
      prmd.setBean(BEAN1);
      prmd.setMetadata(new HashMap<String, List<String>>());

      prmdSupport.create(prmd, new Date().getTime());

      return prmd;
   }
   
   private void creationPagmf() throws FormatControlProfilNotFoundException {
      creationFormatControlProfil();
      
      Pagmf pagmf = new Pagmf();

      pagmf.setCodePagmf(CODE_PAGMF);
      pagmf.setDescription(DESCRIPTION_PAGMF);
      pagmf.setCodeFormatControlProfil("formatProfile");
      
      pagmfSupport.create(pagmf, new Date().getTime());
   }
   
   private void creationFormatControlProfil() {
      FormatControlProfil format = new FormatControlProfil();
      format.setDescription("description");
      format.setFormatCode("formatProfile");
      FormatProfil controlProfil = creationFormatProfil();
      format.setControlProfil(controlProfil);
      
      formatControlProfilSupport.create(format, new Date().getTime());
   }
   
   private FormatProfil creationFormatProfil() {
      FormatProfil formatProfil = new FormatProfil();
      formatProfil.setFileFormat("fmt/354");
      formatProfil.setFormatIdentification(true);
      formatProfil.setFormatValidation(true);
      formatProfil.setFormatValidationMode("STRICT");
      return formatProfil;
   }
   
   private void creationPagmf2() throws FormatControlProfilNotFoundException {
      creationFormatControlProfil2();
      
      Pagmf pagmf = new Pagmf();

      pagmf.setCodePagmf(CODE_PAGMF_2);
      pagmf.setDescription(DESCRIPTION_PAGMF);
      pagmf.setCodeFormatControlProfil("formatProfile");
      
      pagmfSupport.create(pagmf, new Date().getTime());
   }
   
   private void creationFormatControlProfil2() {
      FormatControlProfil format = new FormatControlProfil();
      format.setDescription("description");
      format.setFormatCode("formatProfile");
      FormatProfil controlProfil = creationFormatProfil2();
      format.setControlProfil(controlProfil);
      
      formatControlProfilSupport.create(format, new Date().getTime());
   }
   
   private FormatProfil creationFormatProfil2() {
      FormatProfil formatProfil = new FormatProfil();
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

      contratSupport.create(contract, new Date().getTime());

      contract = new ServiceContract();
      contract.setCodeClient("codeClient2");
      contract.setDescription("description2");
      contract.setLibelle("libelle2");
      contract.setViDuree(Long.valueOf(62));
      contract.setIdPki("pki 1");
      contract.setVerifNommage(false);

      contratSupport.create(contract, new Date().getTime());

      contract = new ServiceContract();
      contract.setCodeClient("codeClient3");
      contract.setDescription("description3");
      contract.setLibelle("libelle3");
      contract.setViDuree(Long.valueOf(63));
      contract.setIdPki("pki 1");
      contract.setVerifNommage(false);

      contratSupport.create(contract, new Date().getTime());

      List<ServiceContract> list = contratSupport.findAll(5);

      Assert.assertEquals("vérification du nombre d'enregistrements", 3, list
            .size());

      for (int i = 1; i < 4; i++) {
         String codeClient = "codeClient" + i;
         String description = "description" + i;
         String libelle = "libelle" + i;
         Long duree = Long.valueOf(60 + i);

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
      SaeContratService fullCs = service.getFullContratService(CODE_CLIENT);

      Assert.assertEquals(fullCs.getCodeClient(),
            CODE_CLIENT);
      Assert.assertEquals(fullCs.getDescription(),
            DESCRIPTION_CONTRAT);
      Assert.assertEquals(fullCs.getLibelle(),
            LIBELLE_CONTRAT);
      Assert.assertEquals(fullCs.getViDuree(),
            DUREE_CONTRAT);
      Assert.assertNotNull(fullCs.getSaePagms());
      for (SaePagm pagm : fullCs.getSaePagms()) {
         Assert.assertEquals(pagm.getCode(), CODE_PAGM);
         Assert.assertEquals(pagm.getDescription(), DESCRIPTION_PAGM);

         SaePagma saePagma = new SaePagma();
         saePagma.setActionUnitaires(Arrays
               .asList(new String[] { CODE_ACTION_1 }));
         saePagma.setCode(CODE_PAGMA);
         pagm.getPagma().equals(saePagma);
         Assert.assertEquals(pagm.getPagma(), saePagma);

         SaePagmp saePagmp = new SaePagmp();
         saePagmp.setCode(CODE_PAGMP);
         saePagmp.setDescription(DESCRIPTION_PAGMP);

         saePagmp.setPrmd(CODE_PRMD);
         Assert.assertEquals(pagm.getPagmp(), saePagmp);
         
         SaePagmf saePagmf = new SaePagmf();
         saePagmf.setCodePagmf(CODE_PAGMF);
         saePagmf.setDescription(DESCRIPTION_PAGMF);
         saePagmf.setFormatProfile("formatProfile");
         Assert.assertEquals(pagm.getPagmf(), saePagmf);
         
      }

   }

}
