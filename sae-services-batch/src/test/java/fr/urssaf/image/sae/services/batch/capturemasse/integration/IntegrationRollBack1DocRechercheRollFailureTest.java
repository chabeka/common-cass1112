/**
 *
 */
package fr.urssaf.image.sae.services.batch.capturemasse.integration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import org.springframework.aop.framework.Advised;
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
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
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
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.NonIntegratedDocumentType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.resultats.ObjectFactory;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.resultats.ResultatsType;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
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
                                   "/applicationContext-sae-services-batch-test.xml",
"/applicationContext-sae-services-capturemasse-test-integration.xml" })
public class IntegrationRollBack1DocRechercheRollFailureTest {

   /**
    *
    */
   private static final String ERREUR_ATTENDUE = "La capture de masse en mode "
         + "\"Tout ou rien\" a été interrompue. Une procédure d'exploitation a été "
         + "initialisée pour supprimer les données qui auraient pu être stockées.";

   @Autowired
   private ApplicationContext applicationContext;

   @Autowired
   private SAECaptureMasseService service;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private SAEDocumentService saeDocumentService;

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService storageDocumentService;

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider provider;

   private EcdeTestSommaire ecdeTestSommaire;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(IntegrationRollBack1DocRechercheRollFailureTest.class);

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
      final VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      final SaeDroits saeDroits = new SaeDroits();
      final List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      final SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      final Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      final String[] roles = new String[] { "ROLE_archivage_masse", "ROLE_recherche" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_masse", saePrmds);
      saeDroits.put("recherche", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                   viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

      // Paramétrage du RND
      parametersService.setVersionRndDateMaj(new Date());
      parametersService.setVersionRndNumero("11.2");

      final TypeDocument typeDocCree = new TypeDocument();
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
      } catch (final IOException e) {
         // rien a faire
      }

      AuthenticationContext.setAuthenticationToken(null);

      final Advised advised = (Advised) saeDocumentService;
      final SAEDocumentService impl = (SAEDocumentService) advised.getTargetSource()
            .getTarget();

      EasyMock.reset(provider, storageDocumentService, impl);

      server.resetData(true, MODE_API.HECTOR);
   }

   @Test
   @DirtiesContext
   public void testLancement() throws Exception {
      initComposants();
      initDatas();

      final ExitTraitement exitStatus = service.captureMasse(ecdeTestSommaire
                                                             .getUrlEcde(), UUID.randomUUID());

      final Advised advised = (Advised) saeDocumentService;
      final SAEDocumentService impl = (SAEDocumentService) advised.getTargetSource()
            .getTarget();

      EasyMock.verify(provider, storageDocumentService, impl);

      Assert.assertFalse("le traitement doit etre en erreur", exitStatus
                         .isSucces());

      checkFiles();

   }

   @SuppressWarnings("unchecked")
   private void initComposants() throws Exception {

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
      EasyMock.expectLastCall().times(9);
      EasyMock.expectLastCall().andThrow(
                                         new DeletionServiceEx("suppression impossible"));

      final StorageDocument storageDocument = new StorageDocument();
      storageDocument.setUuid(UUID.randomUUID());

      // simulation de la non intégration d'un seul document
      EasyMock.expect(
                      storageDocumentService.insertStorageDocument(EasyMock
                                                                   .anyObject(StorageDocument.class)))
      .andReturn(storageDocument).times(9);

      EasyMock.expect(
                      storageDocumentService.insertStorageDocument(EasyMock
                                                                   .anyObject(StorageDocument.class))).andThrow(
                                                                                                                new InsertionServiceEx(ERREUR_ATTENDUE)).anyTimes();

      // la recherche va retourner 1 éléments
      final UntypedDocument untypedDocument = new UntypedDocument();
      untypedDocument.setUuid(UUID.randomUUID());
      final List<UntypedDocument> list = new ArrayList<UntypedDocument>();
      list.add(untypedDocument);

      EasyMock.expect(
                      saeDocumentService.search(EasyMock.anyObject(String.class),
                                                EasyMock.anyObject(List.class), EasyMock.anyInt()))
      .andReturn(list);

      final Advised advised = (Advised) saeDocumentService;
      final SAEDocumentService impl = (SAEDocumentService) advised.getTargetSource()
            .getTarget();

      EasyMock.replay(provider, storageDocumentService, impl);
   }

   private void initDatas() throws IOException {
      final File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      final ClassPathResource resSommaire = new ClassPathResource(
            "testhautniveau/rollBack1DocRechercheSucces/sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

      final File origine = new File(ecdeTestSommaire.getRepEcde(), "documents");
      final String resourceString = "testhautniveau/rollBack1DocRechercheSucces/documents/doc1.PDF";
      final ClassPathResource resource = new ClassPathResource(resourceString);
      final File attestation = new File(origine, "doc1.PDF");
      FileUtils.copyURLToFile(resource.getURL(), attestation);

   }

   private void checkFiles() throws IOException, JAXBException, SAXException {

      final File repTraitement = ecdeTestSommaire.getRepEcde();
      final File debut = new File(repTraitement, "debut_traitement.flag");
      final File fin = new File(repTraitement, "fin_traitement.flag");
      final File resultats = new File(repTraitement, "resultats.xml");

      Assert.assertTrue("le fichier debut_traitement.flag doit exister", debut
                        .exists());
      Assert.assertTrue("le fichier fin_traitement.flag doit exister", fin
                        .exists());
      Assert.assertTrue("le fichier resultats.xml doit exister", resultats
                        .exists());

      final ResultatsType res = getResultats(resultats);

      Assert.assertEquals("11 documents doivent être initialement présents",
                          Integer.valueOf(11), res.getInitialDocumentsCount());
      Assert.assertEquals("11 documents doivent être rejetés", Integer
                          .valueOf(11), res.getNonIntegratedDocumentsCount());
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
      final List<NonIntegratedDocumentType> docs = res.getNonIntegratedDocuments()
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
                     && ERREUR_ATTENDUE.equals(erreurType.getLibelle())) {
                  erreurFound = true;
               }
               indexErreur++;
            }

         }

         index++;

      }

   }

   /**
    * @param resultats
    * @throws JAXBException
    * @throws IOException
    * @throws SAXException
    */
   private ResultatsType getResultats(final File resultats) throws JAXBException,
   IOException, SAXException {
      final JAXBContext context = JAXBContext
            .newInstance(new Class[] { ObjectFactory.class });
      final Unmarshaller unmarshaller = context.createUnmarshaller();

      final Resource classPath = applicationContext
            .getResource("classpath:xsd_som_res/resultats.xsd");
      URL xsdSchema;

      xsdSchema = classPath.getURL();

      // Affectation du schéma XSD si spécifié
      if (xsdSchema != null) {
         final SchemaFactory schemaFactory = SchemaFactory
               .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
         final Schema schema = schemaFactory.newSchema(xsdSchema);
         unmarshaller.setSchema(schema);
      }

      // Déclenche le unmarshalling
      @SuppressWarnings("unchecked")
      final
      JAXBElement<ResultatsType> doc = (JAXBElement<ResultatsType>) unmarshaller
      .unmarshal(resultats);

      return doc.getValue();

   }

}
