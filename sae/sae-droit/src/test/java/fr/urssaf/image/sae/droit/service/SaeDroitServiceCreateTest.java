/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmaReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmpReferenceException;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.PagmReferenceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SaeDroitServiceCreateTest {

   @Autowired
   private CassandraServerBean cassandraServer;

   @Autowired
   private SaeDroitService service;

   @Autowired
   private ContratServiceSupport contratSupport;

   @Autowired
   private PagmSupport pagmSupport;

   @Autowired
   private PagmaSupport pagmaSupport;

   @Autowired
   private PagmpSupport pagmpSupport;

   @Autowired
   private JobClockSupport clockSupport;

   @After
   public void end() throws Exception {
      cassandraServer.resetData();
   }

   @Test(expected = DroitRuntimeException.class)
   public void testCreateContratDejaExistant() {

      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));
      serviceContract.setIdPki("pki 1");
      serviceContract.setVerifNommage(false);
      contratSupport.create(serviceContract, clockSupport.currentCLock());

      List<Pagm> pagms = new ArrayList<Pagm>();
      Pagm pagm = new Pagm();
      pagm.setCode("codePagm");
      pagm.setDescription("description pagm");
      pagm.setPagma("pagma");
      pagm.setPagmp("pagmp");
      pagm.setParametres(new HashMap<String, String>());
      pagms.add(pagm);

      service.createContratService(serviceContract, pagms);

   }

   @Test(expected = DroitRuntimeException.class)
   public void testCreatePagmDejaExistant() {
      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setIdPki("pki 1");
      serviceContract.setVerifNommage(false);
      serviceContract.setViDuree(Long.valueOf(60));

      List<Pagm> pagms = new ArrayList<Pagm>();
      Pagm pagm = new Pagm();
      pagm.setCode("codePagm");
      pagm.setDescription("description pagm");
      pagm.setPagma("pagma");
      pagm.setPagmp("pagmp");
      pagm.setParametres(new HashMap<String, String>());
      pagms.add(pagm);

      pagmSupport.create("codeClient", pagm, clockSupport.currentCLock());

      service.createContratService(serviceContract, pagms);
   }

   @Test(expected = PagmaReferenceException.class)
   public void testCreatePagmaInexistant() {
      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));
      serviceContract.setIdPki("pki 1");
      serviceContract.setVerifNommage(false);

      List<Pagm> pagms = new ArrayList<Pagm>();
      Pagm pagm = new Pagm();
      pagm.setCode("codePagm");
      pagm.setDescription("description pagm");
      pagm.setPagma("pagma");
      pagm.setPagmp("pagmp");
      pagm.setParametres(new HashMap<String, String>());
      pagms.add(pagm);

      service.createContratService(serviceContract, pagms);
   }

   @Test(expected = PagmpReferenceException.class)
   public void testCreatePagmpInexistant() {
      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));
      serviceContract.setIdPki("pki 1");
      serviceContract.setVerifNommage(false);

      List<Pagm> pagms = new ArrayList<Pagm>();
      Pagm pagm = new Pagm();
      pagm.setCode("codePagm");
      pagm.setDescription("description pagm");
      pagm.setPagma("pagma");
      pagm.setPagmp("pagmp");
      pagm.setParametres(new HashMap<String, String>());
      pagms.add(pagm);

      Pagma pagma = new Pagma();
      pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));
      pagma.setCode("pagma");
      pagmaSupport.create(pagma, clockSupport.currentCLock());

      service.createContratService(serviceContract, pagms);
   }

   @Test
   public void testCreateSucces() {
      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));
      serviceContract.setIdPki("pki 1");
      serviceContract.setVerifNommage(false);

      List<Pagm> pagms = new ArrayList<Pagm>();
      Pagm pagm = new Pagm();
      pagm.setCode("codePagm");
      pagm.setDescription("description pagm");
      pagm.setPagma("pagma");
      pagm.setPagmp("pagmp");
      pagm.setParametres(new HashMap<String, String>());
      pagms.add(pagm);

      Pagma pagma = new Pagma();
      pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));
      pagma.setCode("pagma");
      pagmaSupport.create(pagma, clockSupport.currentCLock());

      Pagmp pagmp = new Pagmp();
      pagmp.setCode("pagmp");
      pagmp.setDescription("description pagmp");
      pagmp.setPrmd("codePrmd");
      pagmpSupport.create(pagmp, clockSupport.currentCLock());

      service.createContratService(serviceContract, pagms);

      ServiceContract storedContract = contratSupport.find("codeClient");

      Assert.assertEquals(
            "les deux contrats de service doivent être identiques",
            serviceContract, storedContract);

      List<Pagm> storedPagm = pagmSupport.find("codeClient");
      Assert.assertEquals(
            "les deux listes de pagms doivent avoir la meme longueur", pagms
                  .size(), storedPagm.size());

      Assert.assertTrue("les pagm stockés doivent être identiques", pagms
            .containsAll(storedPagm));

   }

   @Test
   public void testAddPagmSucces() {
      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));
      serviceContract.setIdPki("pki 1");
      serviceContract.setVerifNommage(false);

      List<Pagm> pagms = new ArrayList<Pagm>();
      Pagm pagm = new Pagm();
      pagm.setCode("codePagm");
      pagm.setDescription("description pagm");
      pagm.setPagma("pagma");
      pagm.setPagmp("pagmp");
      pagm.setParametres(new HashMap<String, String>());
      pagms.add(pagm);

      Pagma pagma = new Pagma();
      pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));
      pagma.setCode("pagma");
      pagmaSupport.create(pagma, clockSupport.currentCLock());

      Pagmp pagmp = new Pagmp();
      pagmp.setCode("pagmp");
      pagmp.setDescription("description pagmp");
      pagmp.setPrmd("codePrmd");
      pagmpSupport.create(pagmp, clockSupport.currentCLock());

      service.createContratService(serviceContract, pagms);

      pagm = new Pagm();
      pagm.setCode("codePagm2");
      pagm.setDescription("description pagm");
      pagm.setPagma("pagma");
      pagm.setPagmp("pagmp");
      pagm.setParametres(new HashMap<String, String>());

      service.addPagmContratService(serviceContract.getCodeClient(), pagm);

      List<Pagm> storedPagm = pagmSupport.find("codeClient");

      Assert.assertEquals("il doit y avoir deux pagm dans la liste", 2,
            storedPagm.size());

      boolean found = false;
      int i = 0;
      while (i < storedPagm.size() && !found) {
         if ("codePagm2".equals(storedPagm.get(i).getCode())) {
            found = true;
         }
         i++;
      }

      Assert.assertTrue(
            "le pagm ajouté doit être contenu dans la liste retournée", found);

   }

   @Test(expected=PagmReferenceException.class)
   public void testAddPagmFailPagmExistant() {
      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));
      serviceContract.setIdPki("pki 1");
      serviceContract.setVerifNommage(false);

      List<Pagm> pagms = new ArrayList<Pagm>();
      Pagm pagm = new Pagm();
      pagm.setCode("codePagm");
      pagm.setDescription("description pagm");
      pagm.setPagma("pagma");
      pagm.setPagmp("pagmp");
      pagm.setParametres(new HashMap<String, String>());
      pagms.add(pagm);

      Pagma pagma = new Pagma();
      pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));
      pagma.setCode("pagma");
      pagmaSupport.create(pagma, clockSupport.currentCLock());

      Pagmp pagmp = new Pagmp();
      pagmp.setCode("pagmp");
      pagmp.setDescription("description pagmp");
      pagmp.setPrmd("codePrmd");
      pagmpSupport.create(pagmp, clockSupport.currentCLock());

      service.createContratService(serviceContract, pagms);

      service.addPagmContratService(serviceContract.getCodeClient(), pagm);

   }
}
