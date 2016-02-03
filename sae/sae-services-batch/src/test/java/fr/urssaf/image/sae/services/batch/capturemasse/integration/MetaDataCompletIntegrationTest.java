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
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

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
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-capturemasse-test.xml",
      "/applicationContext-sae-services-capturemasse-test-integration.xml",
      "/applicationContext-sae-services-capturemasse-test-writer.xml" })
public class MetaDataCompletIntegrationTest {

   private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
         "yyyy-MM-dd");

   @Autowired
   private SAECaptureMasseService service;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private EcdeTestSommaire ecdeTestSommaire;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(MetaDataCompletIntegrationTest.class);

   @Autowired
   private CassandraServerBean server;
   @Autowired
   private ParametersService parametersService;
   @Autowired
   private RndSupport rndSupport;
   @Autowired
   private JobClockSupport jobClockSupport;

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      LOGGER.debug("initialisation du répertoire de traitement :"
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
      typeDocCree.setLibelle("Libellé 2.3.1.1.12");
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
      server.resetData();
   }

   @Test
   @DirtiesContext
   public void testLancement() throws ConnectionServiceEx, DeletionServiceEx,
         InsertionServiceEx, IOException, JAXBException, SAXException {

      initDatas();

      ExitTraitement exitStatus = service.captureMasse(ecdeTestSommaire
            .getUrlEcde(), UUID.randomUUID());

      Assert.assertTrue("le traitement doit etre en succès", exitStatus
            .isSucces());

      checkFiles();

   }

   private void initDatas() throws IOException {
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "testhautniveau/metadatacomplet/sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

      File repEcde = new File(ecdeTestSommaire.getRepEcde(), "documents");
      ClassPathResource resAttestation1 = new ClassPathResource(
            "testhautniveau/metadatacomplet/documents/doc1.PDF");
      File fileAttestation1 = new File(repEcde, "doc1.PDF");
      FileUtils.copyURLToFile(resAttestation1.getURL(), fileAttestation1);

   }

   private void checkFiles() throws IOException {

      Properties result = new Properties();

      File datas = new File(ecdeTestSommaire.getRepEcde(), "datas.properties");

      if (!datas.exists()) {
         Assert.fail("le fichier contenant les données n'existe pas");
      }

      FileInputStream stream = new FileInputStream(datas);

      try {
         result.load(stream);
      } finally {
         stream.close();
      }

      Map<String, String> waited = waitedDatas();
      
      System.out.println(result.keySet());

      Assert
            .assertTrue(
                  "tous les éléments présents dans le résultat attendu doit etre dans l'obtenu",
                  waited.keySet().containsAll(result.keySet()));
      Assert
            .assertTrue(
                  "tous les éléments présents dans le résultat obtenu doit etre dans l'attendu",
                  result.keySet().containsAll(waited.keySet()));

      checkValues(waited, result);
   }

   /**
    * @param waited
    * @param result
    */
   private void checkValues(Map<String, String> waited, Properties result) {

      for (String code : waited.keySet()) {
         Assert.assertEquals("le code " + code
               + " attendu et obtenu doivent etre identiques", (String) waited
               .get(code), result.getProperty(code));
      }

   }

   private Map<String, String> waitedDatas() throws IOException {

      Properties debutTraitement = new Properties();
      File debut = new File(ecdeTestSommaire.getRepEcde(),
            "debut_traitement.flag");
      FileInputStream debutStream = new FileInputStream(debut);

      try {
         debutTraitement.load(debutStream);
      } finally {
         debutStream.close();
      }

      Map<String, String> datas = new HashMap<String, String>();
      Date startDate = new Date();
      datas.put("ApplicationProductrice", "ADELAIDE");
      datas.put("CodeActivite", "3");
      datas.put("CodeFonction", "2");
      datas.put("CodeOrganismeGestionnaire", "CER69");
      datas.put("CodeOrganismeProprietaire", "UR750");
      datas.put("CodeRND", "2.3.1.1.12");
      datas.put("ContratDeService", "TESTS_UNITAIRES");
      datas.put("DateArchivage", FORMAT.format(startDate));
      datas.put("DateCreation", "2011-09-08");
      datas.put("DateDebutConservation", FORMAT.format(startDate));
      Date endDate = DateUtils.addDays(startDate, 1825);
      datas.put("DateFinConservation", FORMAT.format(endDate));
      datas.put("FormatFichier", "fmt/354");
      datas.put("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      datas.put("IdTraitementMasseInterne", debutTraitement
            .getProperty("idTraitementMasse"));
      datas.put("NbPages", "2");
      datas.put("NomFichier", "doc1.PDF");
      datas.put("Titre", "Attestation de vigilance");
      datas.put("TypeHash", "SHA-1");
      datas.put("VersionRND", "11.2");
      datas.put("DocumentVirtuel", "false");
      datas.put("DomaineCotisant", "true");

      return datas;

   }
}
