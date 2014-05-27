/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.integration;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
import fr.urssaf.image.sae.services.batch.model.ExitTraitement;
import fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.NonIntegratedDocumentType;
import fr.urssaf.image.sae.services.capturemasse.modele.resultats.ObjectFactory;
import fr.urssaf.image.sae.services.capturemasse.modele.resultats.ResultatsType;
import fr.urssaf.image.sae.services.capturemasse.utils.TraceAssertUtils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-capturemasse-test.xml",
      "/applicationContext-sae-services-capturemasse-test-integration.xml" })
public class IntegrationDeleteOutOfMemoryTest {

   private static final String ERREUR_ATTENDUE = "La capture de masse en mode \"Tout ou rien\" a été interrompue. "
         + "Une procédure d'exploitation a été initialisée pour supprimer les données qui auraient pu être stockées.";

   @Autowired
   private ApplicationContext applicationContext;

   @Autowired
   private SAECaptureMasseService service;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService storageDocumentService;

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider provider;

   private EcdeTestSommaire ecdeTestSommaire;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(IntegrationDeleteOutOfMemoryTest.class);

   @Autowired
   private TraceAssertUtils traceAssertUtils;

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

      LOGGER.debug("initialisation du répertoire de traitetement :"
            + ecdeTestSommaire.getRepEcde());

      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");
      viExtrait.setPagms(Arrays.asList("TU_PAGM1", "TU_PAGM2"));

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "archivage_masse", "recherche" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_masse", saePrmds);
      saeDroits.put("recherche", saePrmds);
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

      EasyMock.reset(provider, storageDocumentService);

      server.resetData();
   }

   @Test
   @DirtiesContext
   public void testLancement() throws ConnectionServiceEx, DeletionServiceEx,
         InsertionServiceEx, IOException, JAXBException, SAXException {
      initComposants();
      initDatas();

      UUID idTdm = UUID.randomUUID();
      URI urlSommaire = ecdeTestSommaire.getUrlEcde();

      ExitTraitement exitStatus = service.captureMasse(urlSommaire, idTdm);

      EasyMock.verify(provider, storageDocumentService);

      Assert.assertFalse("le traitement doit etre en erreur", exitStatus
            .isSucces());

      checkFiles();

      checkTracabilite(idTdm, urlSommaire);

   }

   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   private void initComposants() throws ConnectionServiceEx, DeletionServiceEx,
         InsertionServiceEx {

      // règlage provider
      provider.openConnexion();
      EasyMock.expectLastCall().anyTimes();
      provider.closeConnexion();
      EasyMock.expectLastCall().anyTimes();
      EasyMock.expect(provider.getStorageDocumentService()).andReturn(
            storageDocumentService).anyTimes();

      // règlage storageDocumentService
      storageDocumentService.deleteStorageDocument(EasyMock
            .anyObject(UUID.class));
      EasyMock.expectLastCall().andThrow(new Error("exception de suppression"))
            .once();

      StorageDocument storageDocument = new StorageDocument();
      storageDocument.setUuid(UUID.randomUUID());

      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class)))
            .andReturn(storageDocument).times(9);

      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class))).andThrow(
            new Error("erreur mémoire")).once();

      EasyMock.replay(provider, storageDocumentService);
   }

   private void initDatas() throws IOException {
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "testhautniveau/deleteOutofmemory/sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

      File repEcde = new File(ecdeTestSommaire.getRepEcde(), "documents");
      ClassPathResource resAttestation1 = new ClassPathResource(
            "testhautniveau/deleteOutofmemory/documents/doc1.PDF");
      File fileAttestation1 = new File(repEcde, "doc1.PDF");
      FileUtils.copyURLToFile(resAttestation1.getURL(), fileAttestation1);

   }

   private void checkFiles() throws IOException, JAXBException, SAXException {

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

      ResultatsType res = getResultats(resultats);

      Assert.assertEquals("10 documents doivent être initialement présents",
            Integer.valueOf(10), res.getInitialDocumentsCount());
      Assert.assertEquals("10 documents doivent être rejetés", Integer
            .valueOf(10), res.getNonIntegratedDocumentsCount());
      Assert.assertEquals("0 documents doivent être intégrés", Integer
            .valueOf(0), res.getIntegratedDocumentsCount());
      Assert.assertEquals(
            "0 documents virtuels doivent être initialement présents", Integer
                  .valueOf(0), res.getInitialVirtualDocumentsCount());
      Assert.assertEquals("0 documents virtuels doivent être rejetés", Integer
            .valueOf(0), res.getNonIntegratedVirtualDocumentsCount());
      Assert.assertEquals("0 documents virtuels doivent être intégrés", Integer
            .valueOf(0), res.getIntegratedVirtualDocumentsCount());

      boolean erreurFound = false;
      int index = 0;
      int indexErreur = 0;
      List<ErreurType> listeErreurs;
      List<NonIntegratedDocumentType> docs = res.getNonIntegratedDocuments()
            .getNonIntegratedDocument();
      ErreurType erreurType;
      while (!erreurFound && index < docs.size()) {

         if (CollectionUtils.isNotEmpty(docs.get(index).getErreurs()
               .getErreur())) {

            indexErreur = 0;
            listeErreurs = docs.get(index).getErreurs().getErreur();
            while (!erreurFound && indexErreur < listeErreurs.size()) {
               erreurType = listeErreurs.get(indexErreur);

               if (Constantes.ERR_BUL003.equals(erreurType.getCode())
                     && ERREUR_ATTENDUE.equalsIgnoreCase(erreurType
                           .getLibelle())) {
                  erreurFound = true;
               }
               indexErreur++;
            }

         }

         index++;

      }

      Assert.assertTrue("le message d'erreur doit être trouvé", erreurFound);
   }

   /**
    * @param resultats
    * @throws JAXBException
    * @throws IOException
    * @throws SAXException
    */
   private ResultatsType getResultats(File resultats) throws JAXBException,
         IOException, SAXException {
      JAXBContext context = JAXBContext
            .newInstance(new Class[] { ObjectFactory.class });
      Unmarshaller unmarshaller = context.createUnmarshaller();

      final Resource classPath = applicationContext
            .getResource("classpath:xsd_som_res/resultats.xsd");
      URL xsdSchema;

      xsdSchema = classPath.getURL();

      // Affectation du schéma XSD si spécifié
      if (xsdSchema != null) {
         SchemaFactory schemaFactory = SchemaFactory
               .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
         Schema schema = schemaFactory.newSchema(xsdSchema);
         unmarshaller.setSchema(schema);
      }

      // Déclenche le unmarshalling
      @SuppressWarnings("unchecked")
      JAXBElement<ResultatsType> doc = (JAXBElement<ResultatsType>) unmarshaller
            .unmarshal(resultats);

      return doc.getValue();

   }

   private void checkTracabilite(UUID idTdm, URI urlSommaire) {

      traceAssertUtils
            .verifieTraceCaptureMasseDansRegTechnique(
                  idTdm,
                  urlSommaire,
                  Arrays
                        .asList(
                              "fr.urssaf.image.sae.storage.exception.InsertionServiceEx: erreur mémoire",
                              "fr.urssaf.image.sae.storage.exception.DeletionServiceEx: exception de suppression"));

   }

}
