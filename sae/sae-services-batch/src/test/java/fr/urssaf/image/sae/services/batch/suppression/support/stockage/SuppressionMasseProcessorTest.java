package fr.urssaf.image.sae.services.batch.suppression.support.stockage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.docubase.toolkit.service.ServiceProvider;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.batch.common.Constantes;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-suppressionmasse-test.xml", 
      "/applicationContext-sae-services-suppressionmasse-test-mock.xml"})
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class SuppressionMasseProcessorTest {

   @Autowired
   @Qualifier("suppressionLauncher")
   private JobLauncherTestUtils launcher;
   
   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService mockService;
   
   private ExecutionContext context;
   
   @Before
   public void init() {
      context = new ExecutionContext();
      context.put(Constantes.SUPPRESSION_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
   }
   
   /**
    * Lancer un test avec une requete lucene invalide pour avoir une exception dans le reader
    * @throws QueryParseServiceEx 
    * @throws SearchingServiceEx 
    * 
    */
   @Test
   public void testRequeteLuceneInvalide() throws SearchingServiceEx, QueryParseServiceEx {

      String requete = "srt:[123456 TO ]";
      
      this.context.put(Constantes.REQ_FINALE_SUPPRESSION, requete);
      this.context.put(Constantes.ID_TRAITEMENT_SUPPRESSION, UUID.randomUUID());
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);
      
      EasyMock.expect(
            mockService.searchPaginatedStorageDocuments(EasyMock
                  .anyObject(PaginatedLuceneCriteria.class))).andThrow(new QueryParseServiceEx("La syntaxe de la requête est bizarroide")).once();
      
      mockService.setStorageDocumentServiceParameter(EasyMock
            .anyObject(ServiceProvider.class));
      
      EasyMock.expectLastCall().once();
      
      EasyMock.replay(mockService);

      JobExecution execution = launcher.launchStep(
            "miseALaCorbeilleStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      /*Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED,
            step.getExitStatus().getExitCode());*/
      
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.SUPPRESSION_EXCEPTION);
      Assert.assertFalse("Une exception aurait du être levée",
            listeErreurs.isEmpty());
      
      int nbDocsSupprimes = step.getJobExecution().getExecutionContext()
            .getInt(Constantes.NB_DOCS_SUPPRIMES);
      Assert.assertEquals("Aucun document n'aurait du être supprimé", 0,
            nbDocsSupprimes);
      
      EasyMock.reset(mockService);
   }
   
   /**
    * Lancer un test de supppression OK
    * 
    * @throws QueryParseServiceEx 
    * @throws SearchingServiceEx 
    */
   @Test
   //@DirtiesContext
   public void testSuppressionOK_aucunDoc() throws SearchingServiceEx, QueryParseServiceEx {

      String requete = "srt:123456";
      
      // configure le mock
      PaginatedStorageDocuments retour = new PaginatedStorageDocuments();
      retour.setAllStorageDocuments(new ArrayList<StorageDocument>());
      retour.setLastPage(Boolean.TRUE);
      
      EasyMock.expect(
            mockService.searchPaginatedStorageDocuments(EasyMock
                  .anyObject(PaginatedLuceneCriteria.class))).andReturn(retour).once();
      
      mockService.setStorageDocumentServiceParameter(EasyMock
            .anyObject(ServiceProvider.class));
      
      EasyMock.expectLastCall().once();
      
      EasyMock.replay(mockService);
      
      this.context.put(Constantes.REQ_FINALE_SUPPRESSION, requete);
      this.context.put(Constantes.ID_TRAITEMENT_SUPPRESSION, UUID.randomUUID());
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "miseALaCorbeilleStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status COMPLETED attendu", ExitStatus.COMPLETED,
            step.getExitStatus());
      
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.SUPPRESSION_EXCEPTION);
      Assert.assertTrue("Aucune exception n'aurait du être levée",
            listeErreurs.isEmpty());
      
      Assert.assertEquals("Aucun doc n'aurait du être trouvé",  0, step.getReadCount());
      
      int nbDocsSupprimes = step.getJobExecution().getExecutionContext()
            .getInt(Constantes.NB_DOCS_SUPPRIMES);
      Assert.assertEquals("Aucun document n'aurait du être supprimé", 0,
            nbDocsSupprimes);
      
      EasyMock.reset(mockService);
   }
   
   /**
    * Lancer un test de supppression OK
    * 
    */
   @Test
   @SuppressWarnings("unchecked")
   public void testSuppressionOK_avecDocs() throws SearchingServiceEx, QueryParseServiceEx, UpdateServiceEx, RecycleBinServiceEx {

      String requete = "srt:123456";
      int nbDocs = 10;
      
      // configure le mock
      PaginatedStorageDocuments retour = new PaginatedStorageDocuments();
      retour.setAllStorageDocuments(new ArrayList<StorageDocument>());
      retour.setLastPage(Boolean.TRUE);
      
      for (int index = 0; index < nbDocs; index++) {
         StorageDocument doc = new StorageDocument();
         StorageMetadata metaGel = new StorageMetadata(StorageTechnicalMetadatas.GEL.getShortCode(), Boolean.FALSE);
         doc.setUuid(UUID.randomUUID());
         doc.getMetadatas().add(metaGel);
         retour.getAllStorageDocuments().add(doc);
      }
      
      EasyMock.expect(
            mockService.searchPaginatedStorageDocuments(EasyMock
                  .anyObject(PaginatedLuceneCriteria.class))).andReturn(retour).once();
      
      mockService.setStorageDocumentServiceParameter(EasyMock
            .anyObject(ServiceProvider.class));
      
      EasyMock.expectLastCall().once();
      
      mockService.updateStorageDocument(EasyMock.anyObject(UUID.class), 
            (List<StorageMetadata>) EasyMock.anyObject(), (List<StorageMetadata>) EasyMock.anyObject());
      
      EasyMock.expectLastCall().times(10);
      
      mockService.moveStorageDocumentToRecycleBin(EasyMock.anyObject(UUID.class));
      
      EasyMock.expectLastCall().times(10);
      
      EasyMock.replay(mockService);
      
      this.context.put(Constantes.REQ_FINALE_SUPPRESSION, requete);
      this.context.put(Constantes.ID_TRAITEMENT_SUPPRESSION, UUID.randomUUID());
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "miseALaCorbeilleStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status COMPLETED attendu", ExitStatus.COMPLETED,
            step.getExitStatus());
      
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.SUPPRESSION_EXCEPTION);
      Assert.assertTrue("Aucune exception n'aurait du être levée",
            listeErreurs.isEmpty());
      
      Assert.assertEquals("Plusieurs documents auraient du être trouvés",  nbDocs, step.getReadCount());
      Assert.assertEquals("Plusieurs documents auraient du être mise à la corbeille",  nbDocs, step.getWriteCount());
      
      int nbDocsSupprimes = step.getJobExecution().getExecutionContext()
            .getInt(Constantes.NB_DOCS_SUPPRIMES);
      Assert.assertEquals("Plusieurs documents auraient du être supprimés", nbDocs,
            nbDocsSupprimes);
      
      EasyMock.reset(mockService);
   }
   
   /**
    * Lancer un test de supppression KO (erreur lors de la mise a jour du document)
    * 
    */
   @Test
   @SuppressWarnings("unchecked")
   public void testSuppressionKO_updateDocKO() throws SearchingServiceEx, QueryParseServiceEx, UpdateServiceEx, RecycleBinServiceEx {

      String requete = "srt:123456";
      
      this.context.put(Constantes.REQ_FINALE_SUPPRESSION, requete);
      this.context.put(Constantes.ID_TRAITEMENT_SUPPRESSION, UUID.randomUUID());
      
      // configure le mock
      PaginatedStorageDocuments retour = new PaginatedStorageDocuments();
      retour.setAllStorageDocuments(new ArrayList<StorageDocument>());
      retour.setLastPage(Boolean.TRUE);
      
      StorageDocument doc = new StorageDocument();
      StorageMetadata metaGel = new StorageMetadata(StorageTechnicalMetadatas.GEL.getShortCode(), Boolean.FALSE);
      doc.setUuid(UUID.randomUUID());
      doc.getMetadatas().add(metaGel);
      retour.getAllStorageDocuments().add(doc);
      
      EasyMock.expect(
            mockService.searchPaginatedStorageDocuments(EasyMock
                  .anyObject(PaginatedLuceneCriteria.class))).andReturn(retour).once();
      
      mockService.setStorageDocumentServiceParameter(EasyMock
            .anyObject(ServiceProvider.class));
      
      EasyMock.expectLastCall().once();
      
      mockService.updateStorageDocument(EasyMock.anyObject(UUID.class), 
            (List<StorageMetadata>) EasyMock.anyObject(), (List<StorageMetadata>) EasyMock.anyObject());
      
      EasyMock.expectLastCall().andThrow(new UpdateServiceEx(new Exception("Une erreur a été leveé lors de la modif du doc"))).once();
      
      mockService.moveStorageDocumentToRecycleBin(EasyMock.anyObject(UUID.class));
      
      EasyMock.expectLastCall().once();
      
      EasyMock.replay(mockService);
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "miseALaCorbeilleStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED,
            step.getExitStatus());
      
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.SUPPRESSION_EXCEPTION);
      Assert.assertFalse("Une exception aurait du être levée",
            listeErreurs.isEmpty());
      
      Assert.assertEquals("Plusieurs documents auraient du être trouvés",  1, step.getReadCount());
      Assert.assertEquals("Plusieurs documents auraient du être mise à la corbeille",  1, step.getWriteCount());
      
      int nbDocsSupprimes = step.getJobExecution().getExecutionContext()
            .getInt(Constantes.NB_DOCS_SUPPRIMES);
      Assert.assertEquals("Aucun document n'aurait du être supprimé", 0,
            nbDocsSupprimes);
      
      EasyMock.reset(mockService); 
   }
   
   /**
    * Lancer un test de supppression KO (erreur lors de la mise a la corbeille)
    * 
    */
   @Test
   @SuppressWarnings("unchecked")
   public void testSuppressionKO_moveToRecycleBin() throws SearchingServiceEx, QueryParseServiceEx, UpdateServiceEx, RecycleBinServiceEx {

      String requete = "srt:123456";
      
      this.context.put(Constantes.REQ_FINALE_SUPPRESSION, requete);
      this.context.put(Constantes.ID_TRAITEMENT_SUPPRESSION, UUID.randomUUID());
      
      // configure le mock
      PaginatedStorageDocuments retour = new PaginatedStorageDocuments();
      retour.setAllStorageDocuments(new ArrayList<StorageDocument>());
      retour.setLastPage(Boolean.TRUE);
      
      StorageDocument doc = new StorageDocument();
      StorageMetadata metaGel = new StorageMetadata(StorageTechnicalMetadatas.GEL.getShortCode(), Boolean.FALSE);
      doc.setUuid(UUID.randomUUID());
      doc.getMetadatas().add(metaGel);
      retour.getAllStorageDocuments().add(doc);
      
      EasyMock.expect(
            mockService.searchPaginatedStorageDocuments(EasyMock
                  .anyObject(PaginatedLuceneCriteria.class))).andReturn(retour).once();
      
      mockService.setStorageDocumentServiceParameter(EasyMock
            .anyObject(ServiceProvider.class));
      
      EasyMock.expectLastCall().once();
      
      mockService.updateStorageDocument(EasyMock.anyObject(UUID.class), 
            (List<StorageMetadata>) EasyMock.anyObject(), (List<StorageMetadata>) EasyMock.anyObject());
      
      EasyMock.expectLastCall().times(2);
      
      mockService.moveStorageDocumentToRecycleBin(EasyMock.anyObject(UUID.class));
      
      EasyMock.expectLastCall().andThrow(new RecycleBinServiceEx("Une erreur a été leveé lors de la mise a la corbeille")).once();
      
      EasyMock.replay(mockService);
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "miseALaCorbeilleStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED,
            step.getExitStatus());
      
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.SUPPRESSION_EXCEPTION);
      Assert.assertFalse("Une exception aurait du être levée",
            listeErreurs.isEmpty());
      
      Assert.assertEquals("Plusieurs documents auraient du être trouvés",  1, step.getReadCount());
      Assert.assertEquals("Plusieurs documents auraient du être mise à la corbeille",  1, step.getWriteCount());
      
      int nbDocsSupprimes = step.getJobExecution().getExecutionContext()
            .getInt(Constantes.NB_DOCS_SUPPRIMES);
      Assert.assertEquals("Aucun document n'aurait du être supprimé", 0,
            nbDocsSupprimes);
      
      EasyMock.reset(mockService); 
   }
}
