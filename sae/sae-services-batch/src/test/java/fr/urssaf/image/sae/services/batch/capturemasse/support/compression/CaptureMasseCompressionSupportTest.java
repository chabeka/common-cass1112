package fr.urssaf.image.sae.services.batch.capturemasse.support.compression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.model.SaePagma;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.SaeDroitService;
import fr.urssaf.image.sae.ecde.exception.EcdeRuntimeException;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.exception.CompressionException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.model.CompressedDocument;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-batch-test.xml", 
      "/applicationContext-sae-services-capturemasse-test-mock-droits.xml" })
public class CaptureMasseCompressionSupportTest {
   
   private static final Logger LOG = LoggerFactory
         .getLogger(CaptureMasseCompressionSupportTest.class);

   @Autowired
   private CaptureMasseCompressionSupport captureMasseCompressionSupport;
   
   @Autowired
   private SaeDroitService saeDroitService;
   
   @Autowired
   private EcdeTestTools ecdeTestTools;
   
   private EcdeTestSommaire ecdeTestSommaire;
   
   @Before
   public void init() {
      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");
      
      viExtrait.setPagms(new ArrayList<String>());
      viExtrait.getPagms().add("PAGM_1");
      viExtrait.getPagms().add("PAGM_2");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "archivage_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_masse", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
      
      try {
         ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
      } catch (EcdeRuntimeException e) {
         // tante une nouvelle fois
         ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
      }
   }

   @After
   public void end() throws Exception {
      AuthenticationContext.setAuthenticationToken(null);
      
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien à faire
      }
   }
   
   @Test
   public void isDocumentToBeCompress_droits() {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      UntypedDocument document = new UntypedDocument();
      
      List<SaePagm> pagms = new ArrayList<SaePagm>();
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      
      // Cas 1 : le contrat de service n'existe pas (donc pas de pagms pour ce contrat de service)
      boolean compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      SaePagm saePagm = new SaePagm();
      saePagm.setCode("PAGM_Z");
      saePagm.setPagma(new SaePagma());
      saePagm.getPagma().setActionUnitaires(new ArrayList<String>());
      saePagm.getPagma().getActionUnitaires().add("archivage_masse");
      pagms.add(saePagm);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 2 : le contrat de service existe et est le bon mais le pagm n'existe pas
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setCode("PAGM_1");
      saePagm.getPagma().getActionUnitaires().clear();
      saePagm.getPagma().getActionUnitaires().add("consultation");
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 3 : le contrat de service existe et est le bon mais n'est pas un contrat de service d'archivage de masse
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.getPagma().getActionUnitaires().clear();
      saePagm.getPagma().getActionUnitaires().add("archivage_masse");
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 4 : le contrat de service existe et est le bon et le pagm est un pagm d'archivage de masse
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
   }
   
   private void copieDocument(File ecdeDirectory, String nomFichier) {
      // copie le fichier nomFichier
      // dans le repertoire de l'ecde
      File repertoireDocuments = new File(ecdeDirectory, "documents");
      if (!repertoireDocuments.exists()) {
         repertoireDocuments.mkdirs();
      }
      File fileDoc = new File(repertoireDocuments, nomFichier);
      ClassPathResource resDoc = new ClassPathResource("doc/" + nomFichier);
      FileOutputStream fos = null;
      try {
         fos = new FileOutputStream(fileDoc);
         IOUtils.copy(resDoc.getInputStream(), fos);
         resDoc.getInputStream().close();
      } catch (FileNotFoundException e) {
         LOG.error(e.getMessage());
      } catch (IOException e) {
         LOG.error(e.getMessage());
      } finally {
         if (fos != null) {
            try {
               fos.close();
            } catch (IOException ex) {
               // ignore ... any significant errors should already have been
               // reported via an IOException from the final flush.
            }
         }
      }
   }
   
   @Test
   public void isDocumentToBeCompress_multi_pagm_flag_compression() {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      copieDocument(ecdeDirectory, "doc-5mo.pdf");
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc-5mo.pdf");
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Linux");
      
      List<SaePagm> pagms = new ArrayList<SaePagm>();
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      
      SaePagm saePagm = new SaePagm();
      saePagm.setCode("PAGM_1");
      saePagm.setPagma(new SaePagma());
      saePagm.getPagma().setActionUnitaires(new ArrayList<String>());
      saePagm.getPagma().getActionUnitaires().add("archivage_masse");
      pagms.add(saePagm);
      
      SaePagm saePagm2 = new SaePagm();
      saePagm2.setCode("PAGM_2");
      saePagm2.setPagma(new SaePagma());
      saePagm2.getPagma().setActionUnitaires(new ArrayList<String>());
      saePagm2.getPagma().getActionUnitaires().add("archivage_masse");
      pagms.add(saePagm2);
      
      // Cas 1 : les deux pagms n'autorise pas la compression (null tous les deux)
      boolean compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setCompressionPdfActive(Boolean.FALSE);
      saePagm2.setCompressionPdfActive(null);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 2 : les deux pagms n'autorise pas la compression (un a null, l'autre à false)
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setCompressionPdfActive(null);
      saePagm2.setCompressionPdfActive(Boolean.FALSE);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 3 : les deux pagms n'autorise pas la compression (un a null, l'autre à false)
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setCompressionPdfActive(Boolean.FALSE);
      saePagm2.setCompressionPdfActive(Boolean.FALSE);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 4 : les deux pagms n'autorise pas la compression (false les deux)
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setCompressionPdfActive(Boolean.TRUE);
      saePagm2.setCompressionPdfActive(Boolean.FALSE);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 5 : les deux pagms ont une valeur differente
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setCompressionPdfActive(Boolean.FALSE);
      saePagm2.setCompressionPdfActive(Boolean.TRUE);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 6 : les deux pagms ont une valeur differente
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setCompressionPdfActive(Boolean.TRUE);
      saePagm2.setCompressionPdfActive(Boolean.TRUE);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 7 : les deux pagms ont une valeur a true
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être active", compressionActive);
      EasyMock.reset(saeDroitService);
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
   
   @Test
   public void isDocumentToBeCompress_multi_pagm_seuil_compression() {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      copieDocument(ecdeDirectory, "doc-5mo.pdf");
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc-5mo.pdf");
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Linux");
      
      List<SaePagm> pagms = new ArrayList<SaePagm>();
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      
      SaePagm saePagm = new SaePagm();
      saePagm.setCode("PAGM_1");
      saePagm.setPagma(new SaePagma());
      saePagm.getPagma().setActionUnitaires(new ArrayList<String>());
      saePagm.getPagma().getActionUnitaires().add("archivage_masse");
      saePagm.setCompressionPdfActive(Boolean.TRUE);
      pagms.add(saePagm);
      
      SaePagm saePagm2 = new SaePagm();
      saePagm2.setCode("PAGM_2");
      saePagm2.setPagma(new SaePagma());
      saePagm2.getPagma().setActionUnitaires(new ArrayList<String>());
      saePagm2.getPagma().getActionUnitaires().add("archivage_masse");
      saePagm2.setCompressionPdfActive(Boolean.TRUE);
      pagms.add(saePagm2);
      
      // Cas 1 : les deux pagms n'ont pas de seuil de compression (null tous les deux)
      boolean compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être active", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setSeuilCompressionPdf(Constantes.SEUIL_COMPRESSION_DEFAUT);
      saePagm2.setSeuilCompressionPdf(null);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 2 : les deux pagms n'ont pas de seuil de compression (un a null, l'autre à 2 Mo)
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être active", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setSeuilCompressionPdf(null);
      saePagm2.setSeuilCompressionPdf(Constantes.SEUIL_COMPRESSION_DEFAUT);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 3 : les deux pagms n'ont pas de seuil de compression (un a null, l'autre à 2 Mo)
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être active", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setSeuilCompressionPdf(Constantes.SEUIL_COMPRESSION_DEFAUT);
      saePagm2.setSeuilCompressionPdf(Constantes.SEUIL_COMPRESSION_DEFAUT);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 4 : les deux pagms sont identique (seuil par defaut)
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être active", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setSeuilCompressionPdf(1024);
      saePagm2.setSeuilCompressionPdf(Constantes.SEUIL_COMPRESSION_DEFAUT);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 5 : les deux pagms ont une valeur differente
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setSeuilCompressionPdf(Constantes.SEUIL_COMPRESSION_DEFAUT);
      saePagm2.setSeuilCompressionPdf(1024);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 6 : les deux pagms ont une valeur differente
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setSeuilCompressionPdf(1024);
      saePagm2.setSeuilCompressionPdf(1024);
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 7 : les deux pagms ont une valeur a 1024
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
   
   @Test
   public void isDocumentToBeCompress_os() {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      copieDocument(ecdeDirectory, "doc-5mo.pdf");
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc-5mo.pdf");
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Linux");
      
      List<SaePagm> pagms = new ArrayList<SaePagm>();
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      
      SaePagm saePagm = new SaePagm();
      saePagm.setCode("PAGM_1");
      saePagm.setPagma(new SaePagma());
      saePagm.getPagma().setActionUnitaires(new ArrayList<String>());
      saePagm.getPagma().getActionUnitaires().add("archivage_masse");
      saePagm.setCompressionPdfActive(Boolean.TRUE);
      pagms.add(saePagm);
      
      // Cas 1 : sous linux, la compression est toujours active
      boolean compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être active", compressionActive);
      EasyMock.reset(saeDroitService);
      
      System.setProperty("os.name", "Unix");
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 2 : sous Unix, la compression est toujours active
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être active", compressionActive);
      EasyMock.reset(saeDroitService);
      
      System.setProperty("os.name", "Aix");
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 3 : sous Aix, la compression est toujours active
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être active", compressionActive);
      EasyMock.reset(saeDroitService);
      
      System.setProperty("os.name", "Windows");
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 4 : sous windows, la compression n'est pas active
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
   
   @Test
   public void isDocumentToBeCompress_seuil() {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      copieDocument(ecdeDirectory, "doc-5mo.pdf");
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc-5mo.pdf");
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Linux");
      
      List<SaePagm> pagms = new ArrayList<SaePagm>();
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      
      SaePagm saePagm = new SaePagm();
      saePagm.setCode("PAGM_1");
      saePagm.setPagma(new SaePagma());
      saePagm.getPagma().setActionUnitaires(new ArrayList<String>());
      saePagm.getPagma().getActionUnitaires().add("archivage_masse");
      saePagm.setCompressionPdfActive(Boolean.TRUE);
      pagms.add(saePagm);
      
      // Cas 1 : test du seuil par defaut (null correspond à 2 Mo)
      boolean compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être active", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setSeuilCompressionPdf(Constantes.SEUIL_COMPRESSION_DEFAUT);
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 2 : test du seuil par defaut à 2 Mo
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être active", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setSeuilCompressionPdf(5 * 1024 * 1024);
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 3 : test du seuil a 5 Mo
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être active", compressionActive);
      EasyMock.reset(saeDroitService);
      
      saePagm.setSeuilCompressionPdf(6 * 1024 * 1024);
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 4 : test du seuil a 6 Mo
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
   
   @Test
   public void isDocumentToBeCompress_fullimage() {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      copieDocument(ecdeDirectory, "doc-5mo.pdf");
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc-5mo.pdf");
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Linux");
      
      List<SaePagm> pagms = new ArrayList<SaePagm>();
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      
      SaePagm saePagm = new SaePagm();
      saePagm.setCode("PAGM_1");
      saePagm.setPagma(new SaePagma());
      saePagm.getPagma().setActionUnitaires(new ArrayList<String>());
      saePagm.getPagma().getActionUnitaires().add("archivage_masse");
      saePagm.setCompressionPdfActive(Boolean.TRUE);
      saePagm.setSeuilCompressionPdf(Integer.valueOf(-1));
      pagms.add(saePagm);
      
      // Cas 1 : le fichier est full image
      boolean compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertTrue("La compression aurait du être active", compressionActive);
      EasyMock.reset(saeDroitService);
      
      copieDocument(ecdeDirectory, "attestation_consultation.pdf");
      document.setFilePath("attestation_consultation.pdf");
      
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      // Cas 2 : le fichier ne comporte pas d'images full screen
      compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
   
   @Test
   public void isDocumentToBeCompress_fichierTif() {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      copieDocument(ecdeDirectory, "fichier.tif");
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("fichier.tif");
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Linux");
      
      List<SaePagm> pagms = new ArrayList<SaePagm>();
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      
      SaePagm saePagm = new SaePagm();
      saePagm.setCode("PAGM_1");
      saePagm.setPagma(new SaePagma());
      saePagm.getPagma().setActionUnitaires(new ArrayList<String>());
      saePagm.getPagma().getActionUnitaires().add("archivage_masse");
      saePagm.setCompressionPdfActive(Boolean.TRUE);
      saePagm.setSeuilCompressionPdf(Integer.valueOf(-1));
      pagms.add(saePagm);
      
      // Cas 1 : le fichier est full image
      boolean compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(document, ecdeDirectory);
      Assert.assertFalse("La compression aurait du être inactive", compressionActive);
      EasyMock.reset(saeDroitService);
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
   
   @Test
   public void compresserDocument_success() throws CompressionException {
     
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      copieDocument(ecdeDirectory, "doc-5mo.pdf");
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc-5mo.pdf");
      document.setUMetadatas(new ArrayList<UntypedMetadata>());
      UntypedMetadata metadata = new UntypedMetadata();
      metadata.setLongCode(SAEArchivalMetadatas.TYPE_HASH.getLongCode());
      metadata.setValue("SHA-1");
      document.getUMetadatas().add(metadata);
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Mon-OS-Super-Bien");
      
      // calcul le chemin du document compressé
      String cheminDocCompresse = ecdeDirectory.getAbsolutePath() + File.separator 
            + "documents" + File.separator + "doc-5mo-compressed.pdf"; 
      
      // Cas 1 : compression d'un gros document comportant des images
      CompressedDocument compressedDoc = captureMasseCompressionSupport.compresserDocument(document, ecdeDirectory);
      Assert.assertNotNull("L'objet compressedDocument n'aurait pas du être null", compressedDoc);
      Assert.assertEquals("Le nom du fichier n'est pas celui attendu", "doc-5mo-compressed.pdf", compressedDoc.getFileName());
      Assert.assertEquals("Le chemin du fichier n'est pas celui attendu", cheminDocCompresse, compressedDoc.getFilePath());
      Assert.assertEquals("Le hash du fichier n'est pas celui attendu", "e96e16a9d15ceb481880b7808ab10aaeb5cb4e49", compressedDoc.getHash());
      Assert.assertEquals("Le nom du fichier n'est pas celui attendu", "doc-5mo.pdf", compressedDoc.getOriginalName());
      
      copieDocument(ecdeDirectory, "attestation_consultation.pdf");
      document.setFilePath("attestation_consultation.pdf");
      cheminDocCompresse = ecdeDirectory.getAbsolutePath() + File.separator 
            + "documents" + File.separator + "attestation_consultation-compressed.pdf"; 
      
      // Cas 2 : compression d'un plus petit document comportant des images
      compressedDoc = captureMasseCompressionSupport.compresserDocument(document, ecdeDirectory);
      Assert.assertNotNull("L'objet compressedDocument n'aurait pas du être null", compressedDoc);
      Assert.assertEquals("Le nom du fichier n'est pas celui attendu", "attestation_consultation-compressed.pdf", compressedDoc.getFileName());
      Assert.assertEquals("Le chemin du fichier n'est pas celui attendu", cheminDocCompresse, compressedDoc.getFilePath());
      Assert.assertEquals("Le hash du fichier n'est pas celui attendu", "4bf2ddbd82d5fd38e821e6aae434ac989972a043", compressedDoc.getHash());
      Assert.assertEquals("Le nom du fichier n'est pas celui attendu", "attestation_consultation.pdf", compressedDoc.getOriginalName());
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
   
   @Test
   public void compresserDocument_error() throws CompressionException {
     
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("fichier-inexistant.pdf");
      document.setUMetadatas(new ArrayList<UntypedMetadata>());
      UntypedMetadata metadata = new UntypedMetadata();
      metadata.setLongCode(SAEArchivalMetadatas.TYPE_HASH.getLongCode());
      metadata.setValue("SHA-1");
      document.getUMetadatas().add(metadata);
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Mon-OS-Super-Bien");
      
      // Cas 1 : compression d'un fichier inexistant
      CompressedDocument compressedDoc = captureMasseCompressionSupport.compresserDocument(document, ecdeDirectory);
      Assert.assertNull("L'objet compressedDocument aurait du être null", compressedDoc);
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
   
   @Test
   public void compresserDocument_algoHashInconnu() {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      copieDocument(ecdeDirectory, "attestation_consultation.pdf");
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("attestation_consultation.pdf");
      document.setUMetadatas(new ArrayList<UntypedMetadata>());
      UntypedMetadata metadata = new UntypedMetadata();
      metadata.setLongCode(SAEArchivalMetadatas.TYPE_HASH.getLongCode());
      metadata.setValue("ALGO-INCONNU");
      document.getUMetadatas().add(metadata);
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Mon-OS-Super-Bien");
      
      try {
         // Cas 2 : compression d'un plus petit document comportant des images
         captureMasseCompressionSupport.compresserDocument(document, ecdeDirectory);
         
         Assert.fail("Une erreur aurait du être levée");
      } catch (CompressionException e) {
         Assert.assertEquals("Une autre exception était attendue",  NoSuchAlgorithmException.class.getName(), e.getCause().getClass().getName());
      }
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
   
   @Test
   public void compresserDocument_execCommand() {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      copieDocument(ecdeDirectory, "attestation_consultation.pdf");
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("attestation_consultation.pdf");
      document.setUMetadatas(new ArrayList<UntypedMetadata>());
      UntypedMetadata metadata = new UntypedMetadata();
      metadata.setLongCode(SAEArchivalMetadatas.TYPE_HASH.getLongCode());
      metadata.setValue("SHA-1");
      document.getUMetadatas().add(metadata);
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Linux");
      
      try {
         // Cas 2 : compression d'un plus petit document comportant des images
         captureMasseCompressionSupport.compresserDocument(document, ecdeDirectory);
         
         Assert.fail("Une erreur aurait du être levée");
      } catch (CompressionException e) {
         // ce junit lance juste une commande echo, et ne produit pas le fichier compressé
         // d'ou l'erreur FileNotFoundException
         Assert.assertEquals("Une autre exception était attendue",  FileNotFoundException.class.getName(), e.getCause().getClass().getName());
      }
      
      // remet l'os original
      System.setProperty("os.name", os);
      
   }
}
