package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.data.utils.CheckDataUtils;
import fr.urssaf.image.sae.storage.dfce.exception.DocumentTypeException;
import fr.urssaf.image.sae.storage.dfce.mapping.DocumentForTestMapper;
import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.dfce.utils.TraceAssertUtils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe de test du service
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl
 * InsertionService}
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class InsertionServiceTest {

   @Autowired
   private CommonsServices commonsServices;

   @Autowired
   private TraceAssertUtils traceAssertUtils;

   @Autowired
   private CassandraServerBean cassandraServerBean;

   private static final String PDF1 = "src/test/resources/PDF/doc1.PDF";

   @Before
   public void before() throws ConnectionServiceEx {

      // Initialisation des droits

      final VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");
      viExtrait.setPagms(Arrays.asList("TU_PAGM1", "TU_PAGM2"));

      final SaeDroits saeDroits = new SaeDroits();
      final List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      final SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      final Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      final String[] roles = new String[] { "ROLE_archivage_unitaire" };
      saePrmds.add(saePrmd);
      saeDroits.put("archivage_unitaire", saePrmds);
      viExtrait.setSaeDroits(saeDroits);

      final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                   viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

   }

   @After
   public void after() throws Exception {
      AuthenticationContext.setAuthenticationToken(null);

      cassandraServerBean.resetData(true, MODE_API.HECTOR);

   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#insertStorageDocument(StorageDocument)
    * insertStorageDocument} <br>
    * Insérer deux fois le même document et vérifier que les UUIDs sont
    * différents.
    *
    * @throws ConnectionServiceEx
    *            Exception lévée lorsque la connexion n'aboutie pas.
    * @throws InsertionIdGedExistantEx
    */
   @Test
   public void insertOneDocument() throws IOException, ParseException,
   InsertionServiceEx, ConnectionServiceEx, InsertionIdGedExistantEx {

      final SaeDocument saeDocument = commonsServices.getXmlDataService()
            .saeDocumentReader(
                               new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));

      final StorageDocument storageDocument = DocumentForTestMapper
            .saeDocumentXmlToStorageDocument(saeDocument);

      final StorageDocument firstDocument = commonsServices
            .getInsertionService().insertStorageDocument(storageDocument);

      Assert.assertNotNull(firstDocument);

      traceAssertUtils.verifieTraceDepotDfceDansJournalSae(firstDocument
                                                           .getUuid(), "a2f93f1f121ebba0faef2c0596f2f126eacae77b", "SHA-1");

      traceAssertUtils.verifieAucuneTraceDansRegistres();

   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#insertStorageDocument(StorageDocument)
    * insertStorageDocument} <br>
    * Insérer deux fois le même document et vérifier que les UUIDs sont
    * différents.
    *
    * @throws ConnectionServiceEx
    * @throws InsertionIdGedExistantEx
    */
   @Test
   public void insertTwiceSameDocument() throws IOException, ParseException,
   InsertionServiceEx, ConnectionServiceEx, InsertionIdGedExistantEx {
      final SaeDocument saeDocument = commonsServices.getXmlDataService()
            .saeDocumentReader(
                               new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
      final StorageDocument storageDocument = DocumentForTestMapper
            .saeDocumentXmlToStorageDocument(saeDocument);
      final StorageDocument firstDocument = commonsServices
            .getInsertionService().insertStorageDocument(storageDocument);
      final StorageDocument secondDocument = commonsServices
            .getInsertionService().insertStorageDocument(storageDocument);
      // si la valeur de la comparaison est égale à 1, c'est que les deux UUID
      // sont différent.
      Assert.assertEquals(
                          "Les deux UUID du même document doivent être différent :", true,
                          secondDocument.getUuid().getLeastSignificantBits() != firstDocument
                          .getUuid().getMostSignificantBits());
   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#insertStorageDocument(StorageDocument)
    * insertStorageDocument} <br>
    * <p>
    * Tests réaliser :
    * <ul>
    * <li>Insérer un document et vérifier son UUID.</li>
    * <li>Récupère le document par uuid.</li>
    * <li>Compare les métadonnée insérées dans DFCE et les métadonnées du
    * document xml en entrée.</li>
    * <li>Compare sha de Dfce et le sha1 calculé</li>
    * </ul>
    * </p>
    */
   @Test
   public void insertStorageDocument() throws IOException, ParseException,
   StorageException, NoSuchAlgorithmException {
      final SaeDocument saeDocument = commonsServices.getXmlDataService()
            .saeDocumentReader(
                               new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
      final StorageDocument storageDocument = DocumentForTestMapper
            .saeDocumentXmlToStorageDocument(saeDocument);
      final StorageDocument document = commonsServices.getInsertionService()
            .insertStorageDocument(storageDocument);
      Assert.assertNotNull("UUID après insertion ne doit pas être null ",
                           document.getUuid());
      final UUIDCriteria uuid = new UUIDCriteria(document.getUuid(), null);
      Assert.assertTrue("Les deux SHA1 doivent être identique", CheckDataUtils
                        .checkDocumentSha1(storageDocument.getContent().getInputStream(),
                                           new ByteArrayInputStream(commonsServices
                                                                    .getRetrievalService()
                                                                    .retrieveStorageDocumentContentByUUID(uuid))));
   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#insertStorageDocument(StorageDocument)
    * insertStorageDocument} <br>
    * <p>
    * Tests réaliser :
    * Insérer un document avec un RND inexistant
    */
   @Test
   public void insertStorageDocumentWrongRnd() throws IOException,
   ParseException, StorageException, NoSuchAlgorithmException {

      try {
         final SaeDocument saeDocument = commonsServices.getXmlDataService()
               .saeDocumentReader(
                                  new File(Constants.XML_PATH_DOC_WITH_ERROR[7]));
         final StorageDocument storageDocument = DocumentForTestMapper
               .saeDocumentXmlToStorageDocument(saeDocument);
         commonsServices.getInsertionService().insertStorageDocument(
                                                                     storageDocument);

         Assert.fail("une exception est attendue");

      } catch (final InsertionServiceEx ex) {
         Assert.assertEquals("le type de l'exception mere doit etre correcte",
                             DocumentTypeException.class, ex.getCause().getClass());

      } catch (final Exception ex) {
         Assert.fail("erreur DocumentTypeException attendue");
      }
   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#insertStorageDocument(StorageDocument)
    * insertStorageDocument} <br>
    * <p>
    * Tests réaliser :
    * <ul>
    * <li>Insérer un document et vérifier son UUID.</li>
    * <li>Récupère le document par uuid.</li>
    * <li>Compare les métadonnée insérées dans DFCE et les métadonnées du
    * document xml en entrée.</li>
    * <li>Compare sha de Dfce et le sha1 calculé</li>
    * </ul>
    * </p>
    */
   @Test
   public void insertVirtualReferenceFile() throws IOException, ParseException,
   StorageException, NoSuchAlgorithmException {

      final File file = new File(PDF1);
      final VirtualStorageReference reference = new VirtualStorageReference();
      reference.setFilePath(file.getAbsolutePath());

      final StorageReferenceFile storageReference = commonsServices
            .getInsertionService().insertStorageReference(reference);

      Assert.assertNotNull("UUID après insertion ne doit pas être null ",
                           storageReference.getUuid());

      final SaeDocument saeDocument = commonsServices.getXmlDataService()
            .saeDocumentReader(
                               new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
      final StorageDocument storageDocument = DocumentForTestMapper
            .saeDocumentXmlToStorageDocument(saeDocument);

      final VirtualStorageDocument document = new VirtualStorageDocument();
      document.setStartPage(1);
      document.setEndPage(2);
      document.setMetadatas(storageDocument.getMetadatas());
      document.setFileName("doc1_1_2.PDF");
      document.setReferenceFile(storageReference);

      final UUID docUuid = commonsServices.getInsertionService()
            .insertVirtualStorageDocument(document);

      final UUIDCriteria uuid = new UUIDCriteria(docUuid, null);
      Assert.assertTrue("Les deux SHA1 doivent être identique", CheckDataUtils
                        .checkDocumentSha1(FileUtils.openInputStream(file),
                                           new ByteArrayInputStream(commonsServices
                                                                    .getRetrievalService()
                                                                    .retrieveStorageDocumentContentByUUID(uuid))));
   }

}