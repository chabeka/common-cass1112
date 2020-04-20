package fr.urssaf.image.sae.services.batch.suppression.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.suppression.SAESuppressionMasseService;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.RecycleBinServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.PaginatedStorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.PaginatedLuceneCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-services-suppressionmasse-test.xml",
                                   "/applicationContext-sae-services-suppressionmasse-test-mock.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SAESuppressionMasseServiceTest {

   @Autowired
   private SAESuppressionMasseService service;

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService mockService;

   @Autowired
   @Qualifier("jobQueueService")
   private JobQueueService mockJobService;

   @Before
   public void init() {
      // initialisation du contexte de sécurité
      final VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      final SaeDroits saeDroits = new SaeDroits();
      final List<SaePrmd> saePrmds = new ArrayList<>();
      final SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      final Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      final String[] roles = new String[] {"ROLE_suppression_masse", "ROLE_recherche_iterateur"};
      saePrmds.add(saePrmd);

      saeDroits.put("suppression_masse", saePrmds);
      saeDroits.put("recherche_iterateur", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                   viExtrait.getIdUtilisateur(),
                                                                                   viExtrait,
                                                                                   roles);
      AuthenticationContext.setAuthenticationToken(token);

      try {
         mockJobService.renseignerDocCountJob(EasyMock.anyObject(UUID.class), EasyMock.anyObject(Integer.class));
         EasyMock.expectLastCall().once();
         EasyMock.replay(mockJobService);
      }
      catch (final JobInexistantException e) {
         e.printStackTrace();
      }
   }

   @After
   public void end() {
      AuthenticationContext.setAuthenticationToken(null);

      EasyMock.reset(mockJobService);
   }

   @Test
   public void testRequeteObligatoire() {
      final ExitTraitement exit = service.suppressionMasse(UUID.randomUUID(), null);
      Assert.assertFalse("Le job de suppression aurait du terminé en failure", exit.isSucces());
      Assert.assertEquals("Le message n'est pas celui attendu", "Traitement en erreur", exit.getExitMessage());
   }

   @Test
   public void testRequeteLuceneInvalide() {
      final ExitTraitement exit = service.suppressionMasse(UUID.randomUUID(), "Siret:123456 AND IdTraitementMasse:41882:050200023");
      Assert.assertFalse("Le job de suppression aurait du terminé en failure", exit.isSucces());
      Assert.assertEquals("Le message n'est pas celui attendu", "Traitement en erreur", exit.getExitMessage());
   }

   @Test
   public void testRequeteLuceneNonSearcheableMetadata() {
      final ExitTraitement exit = service.suppressionMasse(UUID.randomUUID(), "NomFichier:123456");
      Assert.assertFalse("Le job de suppression aurait du terminé en failure", exit.isSucces());
      Assert.assertEquals("Le message n'est pas celui attendu", "Traitement en erreur", exit.getExitMessage());
   }

   @Test
   public void testRequeteLuceneUnknownMetadata() {
      final ExitTraitement exit = service.suppressionMasse(UUID.randomUUID(), "Metadata:123456");
      Assert.assertFalse("Le job de suppression aurait du terminé en failure", exit.isSucces());
      Assert.assertEquals("Le message n'est pas celui attendu", "Traitement en erreur", exit.getExitMessage());
   }

   @Test
   @SuppressWarnings("unchecked")
   public void testSuppressionKO_updateDocKO() throws SearchingServiceEx, QueryParseServiceEx, UpdateServiceEx, RecycleBinServiceEx {

      // configure le mock
      final PaginatedStorageDocuments retour = new PaginatedStorageDocuments();
      retour.setAllStorageDocuments(new ArrayList<StorageDocument>());
      retour.setLastPage(Boolean.TRUE);

      final StorageDocument doc = new StorageDocument();
      final StorageMetadata metaGel = new StorageMetadata(StorageTechnicalMetadatas.GEL.getShortCode(), Boolean.FALSE);
      doc.setUuid(UUID.randomUUID());
      doc.getMetadatas().add(metaGel);
      retour.getAllStorageDocuments().add(doc);

      EasyMock.expect(
                      mockService.searchPaginatedStorageDocuments(EasyMock
                                                                          .anyObject(PaginatedLuceneCriteria.class)))
              .andReturn(retour)
              .once();

      mockService.updateStorageDocument(EasyMock.anyObject(UUID.class),EasyMock.anyObject(UUID.class),
                                        (List<StorageMetadata>) EasyMock.anyObject(),
                                        (List<StorageMetadata>) EasyMock.anyObject());

      EasyMock.expectLastCall().andThrow(new UpdateServiceEx(new Exception("Une erreur a été leveé lors de la modif du doc"))).once();

      mockService.moveStorageDocumentToRecycleBin(EasyMock.anyObject(UUID.class));

      EasyMock.expectLastCall().once();
      
      mockService.moveStorageDocumentToRecycleBin(EasyMock.anyObject(UUID.class));
      EasyMock.expectLastCall().once();

      EasyMock.replay(mockService);

      final ExitTraitement exit = service.suppressionMasse(UUID.randomUUID(), "NumeroRecours:445");

      Assert.assertFalse("Le job de suppression aurait du terminé en failure", exit.isSucces());
      Assert.assertEquals("Le message n'est pas celui attendu", "Traitement en erreur", exit.getExitMessage());

      EasyMock.reset(mockService);
   }

   @Test
   @SuppressWarnings("unchecked")
   public void testSuppressionOK() throws SearchingServiceEx, QueryParseServiceEx, UpdateServiceEx, RecycleBinServiceEx {

      // configure le mock
      final PaginatedStorageDocuments retour = new PaginatedStorageDocuments();
      retour.setAllStorageDocuments(new ArrayList<StorageDocument>());
      retour.setLastPage(Boolean.TRUE);

      final StorageDocument doc = new StorageDocument();
      final StorageMetadata metaGel = new StorageMetadata(StorageTechnicalMetadatas.GEL.getShortCode(), Boolean.FALSE);
      doc.setUuid(UUID.randomUUID());
      doc.getMetadatas().add(metaGel);
      retour.getAllStorageDocuments().add(doc);

      EasyMock.expect(
                      mockService.searchPaginatedStorageDocuments(EasyMock
                                                                          .anyObject(PaginatedLuceneCriteria.class)))
              .andReturn(retour)
              .times(2);

      mockService.updateStorageDocument(EasyMock.anyObject(UUID.class),
                                        EasyMock.anyObject(UUID.class),
                                        (List<StorageMetadata>) EasyMock.anyObject(),
                                        (List<StorageMetadata>) EasyMock.anyObject());
      EasyMock.expectLastCall().once();

      mockService.moveStorageDocumentToRecycleBin(EasyMock.anyObject(UUID.class));
      EasyMock.expectLastCall().once();

      EasyMock.replay(mockService);

      final ExitTraitement exit = service.suppressionMasse(UUID.randomUUID(), "NumeroRecours:445");

      Assert.assertTrue("Le job de suppression aurait du terminé en succès", exit.isSucces());
      Assert.assertEquals("Le message n'est pas celui attendu", "Traitement réalisé avec succès", exit.getExitMessage());

      EasyMock.reset(mockService);
   }
}
