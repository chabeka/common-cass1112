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

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
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
import fr.urssaf.image.sae.droit.exception.ContratServiceReferenceException;
import fr.urssaf.image.sae.droit.exception.LockTimeoutException;
import fr.urssaf.image.sae.droit.exception.PagmReferenceException;
import fr.urssaf.image.sae.droit.utils.ZookeeperUtils;

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

   @Autowired
   private CuratorFramework curatorClient;

   @After
   public void end() throws Exception {
      cassandraServer.resetData();
   }

   @Test
   public void testCreateServiceDejaLocke() {
      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
            "/DroitContratService/codeClient");
      try {
         ZookeeperUtils.acquire(mutex, "/DroitContratService/codeClient");
      } catch (LockTimeoutException e) {
         Assert.fail("pas d'exception de lock attendue");
      }

      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));

      List<Pagm> pagms = new ArrayList<Pagm>();
      Pagm pagm = new Pagm();
      pagm.setCode("codePagm");
      pagm.setDescription("description pagm");
      pagm.setPagma("pagma");
      pagm.setPagmp("pagmp");
      pagm.setParametres(new HashMap<String, String>());
      pagms.add(pagm);

      try {
         service.createContratService(serviceContract, pagms);
         Assert.fail("erreur LockTimeoutException attendue");
      } catch (LockTimeoutException e) {

      } finally {
         mutex.release();
      }
   }

   @Test(expected = ContratServiceReferenceException.class)
   public void testCreateContratDejaExistant() throws LockTimeoutException {

      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));

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

   @Test(expected = PagmReferenceException.class)
   public void testCreatePagmDejaExistant() throws LockTimeoutException {
      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
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
   public void testCreatePagmaInexistant() throws LockTimeoutException {
      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));

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
   public void testCreatePagmpInexistant() throws LockTimeoutException {
      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));

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
   public void testCreateSucces() throws LockTimeoutException {
      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));

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
}
