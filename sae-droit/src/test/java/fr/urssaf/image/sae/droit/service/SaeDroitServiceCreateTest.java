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

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmfSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.PagmReferenceException;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.model.SaePagma;
import fr.urssaf.image.sae.droit.model.SaePagmf;
import fr.urssaf.image.sae.droit.model.SaePagmp;

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
   private PagmfSupport pagmfSupport;

   @Autowired
   private FormatControlProfilSupport formatControlProfilSupport;

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

      List<SaePagm> listeSaePagm = new ArrayList<SaePagm>();
      SaePagma pagma = new SaePagma();
      SaePagmp pagmp = new SaePagmp();
      pagma.setActionUnitaires(Arrays
            .asList(new String[] { "archivage_unitaire" }));
      pagma.setCode("codePagma");
      pagmp.setCode("pagmpCode");
      pagmp.setDescription("description pagmp");
      pagmp.setPrmd("prmd");
      SaePagm saePagm = new SaePagm();
      saePagm.setCode("codePagm");
      saePagm.setDescription("description pagm");
      saePagm.setPagma(pagma);
      saePagm.setPagmp(pagmp);

      listeSaePagm.add(saePagm);

      service.createContratService(serviceContract, listeSaePagm);

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

      List<SaePagm> listeSaePagm = new ArrayList<SaePagm>();

      SaePagma pagma = new SaePagma();
      SaePagmp pagmp = new SaePagmp();
      pagma.setActionUnitaires(Arrays
            .asList(new String[] { "archivage_unitaire" }));
      pagma.setCode("codePagma");
      pagmp.setCode("pagmpCode");
      pagmp.setDescription("description pagmp");
      pagmp.setPrmd("prmd");

      SaePagm saePagm = new SaePagm();
      saePagm.setCode("codePagm");
      saePagm.setDescription("description pagm");
      saePagm.setPagma(pagma);
      saePagm.setPagmp(pagmp);
      listeSaePagm.add(saePagm);

      service.createContratService(serviceContract, listeSaePagm);
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

      SaePagma pagma = new SaePagma();
      pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));
      pagma.setCode("pagma");

      SaePagmp pagmp = new SaePagmp();
      pagmp.setCode("pagmp");
      pagmp.setDescription("description pagmp");
      pagmp.setPrmd("codePrmd");

      SaePagmf pagmf = new SaePagmf();
      pagmf.setCodePagmf("pagmf");
      pagmf.setDescription("description pagmf");
      pagmf.setFormatProfile("formatProfile");

      FormatProfil formatProfil = new FormatProfil();
      formatProfil.setFileFormat("fmt/354");
      formatProfil.setFormatIdentification(true);
      formatProfil.setFormatValidation(true);
      formatProfil.setFormatValidationMode("STRICT");

      FormatControlProfil formatControlProfil = new FormatControlProfil();
      formatControlProfil.setFormatCode("formatProfile");
      formatControlProfil.setDescription("description");
      formatControlProfil.setControlProfil(formatProfil);

      formatControlProfilSupport.create(formatControlProfil, clockSupport
            .currentCLock());

      List<SaePagm> listeSaePagm = new ArrayList<SaePagm>();
      SaePagm saePagm = new SaePagm();
      saePagm.setCode("codePagm");
      saePagm.setDescription("description pagm");
      saePagm.setPagma(pagma);
      saePagm.setPagmp(pagmp);
      saePagm.setPagmf(pagmf);
      listeSaePagm.add(saePagm);

      service.createContratService(serviceContract, listeSaePagm);

      ServiceContract storedContract = contratSupport.find("codeClient");

      Assert.assertEquals(
            "les deux contrats de service doivent être identiques",
            serviceContract, storedContract);

      List<SaePagm> storedSaePagm = service.getListeSaePagm("codeClient");
      Assert.assertEquals(
            "les deux listes de pagms doivent avoir la meme longueur",
            listeSaePagm.size(), storedSaePagm.size());

      Assert.assertEquals("les pagm stockés doivent être identiques",
            listeSaePagm.get(0).getCode(), storedSaePagm.get(0).getCode());
      Assert.assertEquals("les pagm stockés doivent être identiques",
            listeSaePagm.get(0).getDescription(), storedSaePagm.get(0)
                  .getDescription());
      Assert.assertEquals("les pagm stockés doivent être identiques",
            listeSaePagm.get(0).getPagma(), storedSaePagm.get(0).getPagma());
      Assert.assertEquals("les pagm stockés doivent être identiques",
            listeSaePagm.get(0).getPagmf().getCodePagmf(), storedSaePagm.get(0)
                  .getPagmf().getCodePagmf());
      Assert.assertEquals("les pagm stockés doivent être identiques",
            listeSaePagm.get(0).getPagmp(), storedSaePagm.get(0).getPagmp());

   }

   @Test(expected = PagmReferenceException.class)
   public void testAddPagmFailPagmExistant() {
      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));
      serviceContract.setIdPki("pki 1");
      serviceContract.setVerifNommage(false);

      Pagma pagma = new Pagma();
      pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));
      pagma.setCode("pagma");
      pagmaSupport.create(pagma, clockSupport.currentCLock());

      Pagmp pagmp = new Pagmp();
      pagmp.setCode("pagmp");
      pagmp.setDescription("description pagmp");
      pagmp.setPrmd("codePrmd");
      pagmpSupport.create(pagmp, clockSupport.currentCLock());

      List<SaePagm> listeSaePagm = new ArrayList<SaePagm>();
      SaePagm saePagm = new SaePagm();
      saePagm.setCode("codePagm");
      saePagm.setDescription("description pagm");
      SaePagma saePagma = new SaePagma();
      saePagma.setCode(pagma.getCode());
      saePagma.setActionUnitaires(pagma.getActionUnitaires());
      SaePagmp saePagmp = new SaePagmp();
      saePagmp.setCode(pagmp.getCode());
      saePagmp.setDescription(pagmp.getDescription());
      saePagmp.setPrmd(pagmp.getPrmd());
      saePagm.setPagma(saePagma);
      saePagm.setPagmp(saePagmp);
      listeSaePagm.add(saePagm);

      service.createContratService(serviceContract, listeSaePagm);

      service.ajouterPagmContratService(serviceContract.getCodeClient(),
            saePagm);

   }

   @Test
   public void testAjouterPagmSucces() {

      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));
      serviceContract.setVerifNommage(false);
      serviceContract.setListPki(Arrays.asList(new String[] { "pki 1" }));

      contratSupport.create(serviceContract, clockSupport.currentCLock());

      SaePagma pagma = new SaePagma();
      SaePagmp pagmp = new SaePagmp();
      pagma.setActionUnitaires(Arrays
            .asList(new String[] { "archivage_unitaire" }));
      pagma.setCode("codePagma");
      pagmp.setCode("codePagmp");
      pagmp.setDescription("description pagmp");
      pagmp.setPrmd("codePrmd");

      SaePagm saePagm = new SaePagm();
      saePagm.setCode("codePagm");
      saePagm.setDescription("description pagm");
      saePagm.setPagma(pagma);
      saePagm.setPagmp(pagmp);

      service.ajouterPagmContratService("codeClient", saePagm);

      List<Pagm> storedPagm = pagmSupport.find("codeClient");

      Assert.assertEquals("il doit y avoir 1 pagm dans la liste", 1, storedPagm
            .size());

      boolean found = false;
      int i = 0;
      while (i < storedPagm.size() && !found) {
         if ("codePagm".equals(storedPagm.get(i).getCode())) {
            found = true;
         }
         i++;
      }

      Assert.assertTrue(
            "le pagm ajouté doit être contenu dans la liste retournée", found);

   }

   @Test
   public void testModifierPagmSucces() {

      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));
      serviceContract.setVerifNommage(false);
      serviceContract.setListPki(Arrays.asList(new String[] { "pki 1" }));

      contratSupport.create(serviceContract, clockSupport.currentCLock());

      SaePagma pagma = new SaePagma();
      SaePagmp pagmp = new SaePagmp();
      SaePagmf pagmf = new SaePagmf();
      pagma.setActionUnitaires(Arrays
            .asList(new String[] { "archivage_unitaire" }));
      pagma.setCode("codePagma");
      pagmp.setCode("codePagmp");
      pagmp.setDescription("description pagmp");
      pagmp.setPrmd("codePrmd");
      pagmf.setCodePagmf("codePagmf");
      pagmf.setDescription("description pagmf");
      FormatControlProfil fcp = new FormatControlProfil();
      fcp.setFormatCode("codeFormatProfile");
      fcp.setDescription("description fcp");
      FormatProfil controlProfil = new FormatProfil();
      controlProfil.setFileFormat("formatFichier");
      controlProfil.setFormatIdentification(false);
      controlProfil.setFormatValidation(true);
      controlProfil.setFormatValidationMode("STRICT");
      fcp.setControlProfil(controlProfil);

      formatControlProfilSupport.create(fcp, clockSupport.currentCLock());
      pagmf.setFormatProfile("codeFormatProfile");

      SaePagm saePagm = new SaePagm();
      saePagm.setCode("codePagm");
      saePagm.setDescription("description pagm");
      saePagm.setPagma(pagma);
      saePagm.setPagmp(pagmp);
      saePagm.setPagmf(pagmf);

      service.ajouterPagmContratService("codeClient", saePagm);

      List<SaePagm> listeSaePagm = service.getListeSaePagm("codeClient");

      Assert.assertEquals("il doit y avoir 1 pagm dans la liste", 1,
            listeSaePagm.size());

      Assert.assertEquals("Le code du PAGM doit être codePagm", "codePagm",
            listeSaePagm.get(0).getCode());
      Assert.assertEquals("Le code du PAGMa doit être codePagma", "codePagma",
            listeSaePagm.get(0).getPagma().getCode());
      Assert.assertEquals(
            "La description du PAGMp doit être - description pagmp -",
            "description pagmp", listeSaePagm.get(0).getPagmp()
                  .getDescription());
      Assert.assertEquals(
            "Le code du profil de format du PAGMf doit être codeFormatProfile",
            "codeFormatProfile", listeSaePagm.get(0).getPagmf()
                  .getFormatProfile());

      // Modification de saePagm
      pagma.setCode("codePagmaModifie");
      pagmp.setDescription("description pagmp modifiée");
      pagmf.setDescription("description pagmf modifiée");
      saePagm.setDescription("description pagm modifiée");
      saePagm.setPagma(pagma);
      saePagm.setPagmp(pagmp);
      saePagm.setPagmf(pagmf);

      service.modifierPagmContratService("codeClient", saePagm);

      listeSaePagm = service.getListeSaePagm("codeClient");

      Assert.assertEquals("il doit y avoir 1 pagm dans la liste", 1,
            listeSaePagm.size());

      Assert.assertEquals("Le code du PAGM doit être codePagm", "codePagm",
            listeSaePagm.get(0).getCode());
      Assert.assertEquals(
            "La description du PAGM doit être - description pagm modifiée -",
            "description pagm modifiée", listeSaePagm.get(0).getDescription());
      Assert.assertEquals("Le code du PAGMa doit être codePagma",
            "codePagmaModifie", listeSaePagm.get(0).getPagma().getCode());
      Assert.assertEquals(
            "La description du PAGMp doit être - description pagmp modifiée -",
            "description pagmp modifiée", listeSaePagm.get(0).getPagmp()
                  .getDescription());
      Assert.assertEquals(
            "La description du PAGMf doit être - description pagmf modifiée -",
            "description pagmf modifiée", listeSaePagm.get(0).getPagmf()
                  .getDescription());
   }

   @Test
   public void testSupprimerPagmSucces() {

      ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("codeClient");
      serviceContract.setDescription("description");
      serviceContract.setLibelle("libellé");
      serviceContract.setViDuree(Long.valueOf(60));
      serviceContract.setVerifNommage(false);
      serviceContract.setListPki(Arrays.asList(new String[] { "pki 1" }));

      contratSupport.create(serviceContract, clockSupport.currentCLock());

      SaePagma pagma = new SaePagma();
      SaePagmp pagmp = new SaePagmp();
      SaePagmf pagmf = new SaePagmf();
      pagma.setActionUnitaires(Arrays
            .asList(new String[] { "archivage_unitaire" }));
      pagma.setCode("codePagma");
      pagmp.setCode("codePagmp");
      pagmp.setDescription("description pagmp");
      pagmp.setPrmd("codePrmd");

      pagmf.setCodePagmf("codePagmf");
      pagmf.setDescription("description pagmf");
      FormatControlProfil fcp = new FormatControlProfil();
      fcp.setFormatCode("codeFormatProfile");
      fcp.setDescription("description fcp");
      FormatProfil controlProfil = new FormatProfil();
      controlProfil.setFileFormat("formatFichier");
      controlProfil.setFormatIdentification(false);
      controlProfil.setFormatValidation(true);
      controlProfil.setFormatValidationMode("STRICT");
      fcp.setControlProfil(controlProfil);
      formatControlProfilSupport.create(fcp, clockSupport.currentCLock());
      pagmf.setFormatProfile("codeFormatProfile");

      SaePagm saePagm = new SaePagm();
      saePagm.setCode("codePagm");
      saePagm.setDescription("description pagm");
      saePagm.setPagma(pagma);
      saePagm.setPagmp(pagmp);
      saePagm.setPagmf(pagmf);

      service.ajouterPagmContratService("codeClient", saePagm);

      List<SaePagm> listeSaePagm = service.getListeSaePagm("codeClient");

      Assert.assertEquals("il doit y avoir 1 pagm dans la liste", 1,
            listeSaePagm.size());

      Assert.assertEquals("Le code du PAGM doit être codePagm", "codePagm",
            listeSaePagm.get(0).getCode());
      Assert.assertEquals("Le code du PAGMa doit être codePagma", "codePagma",
            listeSaePagm.get(0).getPagma().getCode());
      Assert.assertEquals(
            "La description du PAGMp doit être - description pagmp -",
            "description pagmp", listeSaePagm.get(0).getPagmp()
                  .getDescription());
      Assert.assertEquals(
            "Le code du profil de format du PAGMf doit être codeFormatProfile",
            "codeFormatProfile", listeSaePagm.get(0).getPagmf()
                  .getFormatProfile());

      service.supprimerPagmContratService("codeClient", "codePagm");

      try {
         listeSaePagm = service.getListeSaePagm("codeClient");
      } catch (InvalidCacheLoadException e) {
         Assert
               .assertEquals(
                     "Message attentdu : CacheLoader returned null for key codePagma.",
                     "CacheLoader returned null for key codePagma.", e
                           .getMessage());
      }

   }
}
