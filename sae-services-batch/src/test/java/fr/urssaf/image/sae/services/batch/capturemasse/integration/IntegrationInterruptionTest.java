/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-batch-test.xml" })
public class IntegrationInterruptionTest {

   @Autowired
   private SAECaptureMasseService service;
   @Autowired
   private EcdeTestTools ecdeTestTools;
   @Autowired
   private CassandraServerBean server;
   @Autowired
   private ParametersService parametersService;
   @Autowired
   private RndSupport rndSupport;
   @Autowired
   private JobClockSupport jobClockSupport;

   private EcdeTestSommaire ecdeTestSommaire;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(IntegrationInterruptionTest.class);

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      LOGGER.debug("initialisation du répertoire de traitetement :"
            + ecdeTestSommaire.getRepEcde());

      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

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

      // Paramétrage du RND
      parametersService.setVersionRndDateMaj(new Date());
      parametersService.setVersionRndNumero("11.2");

      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("2.3.1.1.12");
      typeDocCree.setCodeActivite("3");
      typeDocCree.setCodeFonction("2");
      typeDocCree.setDureeConservation(1825);
      typeDocCree.setLibelle("ATTESTATION DE VIGILANCE");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());
   }

   @After
   public void end() throws Exception {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }

      AuthenticationContext.setAuthenticationToken(null);

      // EasyMock.reset(provider, storageDocumentService);

      server.resetData();
   }


   @Test
   @DirtiesContext
   public void testLancement() throws ConnectionServiceEx, DeletionServiceEx,
   InsertionServiceEx, IOException {
      initComposants();
      initDatas();

      Date dateNow = new Date();
      Date dateStart = DateUtils.addSeconds(dateNow, 10);
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
      String value = sdf.format(dateStart);
      LOGGER.debug("Debut de l'arret prévu à " + value);

      ExitTraitement exitStatus = service.captureMasse(ecdeTestSommaire
            .getUrlEcde(), UUID.randomUUID());

      // EasyMock.verify(provider, storageDocumentService);

      Assert.assertTrue("le traitement doit etre un succes", exitStatus
            .isSucces());

      checkFiles();

   }

   private void initComposants() throws ConnectionServiceEx, DeletionServiceEx,
   InsertionServiceEx {

      // règlage provider
      // provider.openConnexion();
      // EasyMock.expectLastCall().once();
      // EasyMock.expectLastCall().andThrow(new ConnectionServiceEx());
      // provider.closeConnexion();
      // EasyMock.expectLastCall().anyTimes();
      // EasyMock.expect(provider.getStorageDocumentService()).andReturn(
      // storageDocumentService).anyTimes();

      // règlage storageDocumentService
      // storageDocumentService.deleteStorageDocument(EasyMock
      // .anyObject(UUID.class));
      // EasyMock.expectLastCall().anyTimes();

      StorageDocument storageDocument = new StorageDocument();
      storageDocument.setUuid(UUID.randomUUID());

      // EasyMock.expect(
      // storageDocumentService.insertStorageDocument(EasyMock
      // .anyObject(StorageDocument.class)))
      // .andReturn(storageDocument).anyTimes();
      //
      // EasyMock.replay(provider, storageDocumentService);
   }

   private void initDatas() throws IOException {
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "testhautniveau/204/sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

      File origine = new File(ecdeTestSommaire.getRepEcde(), "documents");
      int i = 1;
      File dest, attestation;
      String resourceString = "testhautniveau/204/documents/";
      ClassPathResource resource;
      while (i < 11) {

         dest = new File(origine, String.valueOf(i));
         resourceString = resourceString + i + File.separator;
         resource = new ClassPathResource(resourceString + "doc" + i + ".PDF");
         attestation = new File(dest, "doc" + i + ".PDF");
         FileUtils.copyURLToFile(resource.getURL(), attestation);

         origine = dest;
         i++;
      }
   }

   private void checkFiles() throws IOException {

      File repTraitement = ecdeTestSommaire.getRepEcde();
      File debut = new File(repTraitement, "debut_traitement.flag");
      File fin = new File(repTraitement, "fin_traitement.flag");
      File resultats = new File(repTraitement, "resultats.xml");

      Assert.assertTrue("le fichier debut_traitement.flag doit exister", debut
            .exists());
      Assert.assertTrue("le fichier fin_traitement.flag doit exister", fin
            .exists());
      Assert.assertTrue("le fichier resultats.xml doit exister", resultats
            .exists());

      String sha1Resultat = calculeSha1(resultats);
      String sha1Attendu = "de6273873baab4d45cb78c5dcc4dce79f917ed4e";

      Assert.assertEquals(
            "le sha1 attendu et de résultat doivent etre identiques",
            sha1Attendu, sha1Resultat);

   }

   private String calculeSha1(File file) throws IOException {

      FileInputStream fis = new FileInputStream(file);
      StringBuilder sb = new StringBuilder();
      int content;
      while ((content = fis.read()) != -1) {
         // convert to char and display it
         sb.append((char) content);
      }

      LOGGER.debug(sb.toString());
      try {

         return DigestUtils.shaHex(fis);

      } finally {
         if (fis != null) {
            fis.close();
         }
      }

   }

}
