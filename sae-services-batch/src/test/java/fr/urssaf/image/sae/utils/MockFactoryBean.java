/**
 * 
 */
package fr.urssaf.image.sae.utils;

import javax.xml.bind.JAXBElement;

import org.easymock.EasyMock;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.droit.service.SaeDroitService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.services.batch.capturemasse.controles.SAEControleSupportService;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.DocumentType;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.CaptureMasseControleSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

/**
 * Classe de factory pour créer les mocks
 * 
 */
public class MockFactoryBean {

   /**
    * Création d'un mock de storageDocumentService
    * 
    * @return un mock StorageDocumentService
    */
   public final StorageDocumentService createStorageDocumentService() {
      return EasyMock.createMock(StorageDocumentService.class);
   }

   /**
    * création d'un mock de CaptureMasseControleSupport
    * 
    * @return un mock CaptureMasseControleSupport
    */
   public final CaptureMasseControleSupport createCaptureMasseControleSupport() {
      return EasyMock.createMock(CaptureMasseControleSupport.class);
   }

   /**
    * 
    * @return instance de {@link DeletionService}
    */
   public final DeletionService createDeletionService() {

      DeletionService service = EasyMock.createMock(DeletionService.class);

      return service;
   }

   /**
    * création d'un mock de {@link SAEEnrichmentMetadataService}
    * 
    * @return un mock SAEEnrichmentMetadataService
    */
   public final SAEEnrichmentMetadataService createEnrichmentMetaDataService() {

      return EasyMock.createMock(SAEEnrichmentMetadataService.class);
   }

   /**
    * création d'un mock de {@link StorageServiceProvider}
    * 
    * @return un mock StorageServiceProvider
    */
   public final StorageServiceProvider createStorageServiceProvider() {

      return EasyMock.createMock(StorageServiceProvider.class);
   }

   /**
    * création d'un mock de {@link DFCEServicesManager}
    * 
    * @return un mock DFCEServicesManager
    */
   public final DFCEServicesManager createServicesManager() {
      return EasyMock.createMock(DFCEServicesManager.class);
   }

   /**
    * création d'un mock {@link ItemProcessor}
    * 
    * @return un mock ItemProcessor
    */
   @SuppressWarnings("unchecked")
   public final ItemProcessor<JAXBElement<DocumentType>, UntypedDocument> createConvertSommaireProcessor() {
      return EasyMock.createMock(ItemProcessor.class);
   }

   /**
    * création d'un mock {@link SAEDocumentService}
    * 
    * @return un mock SAEDocumentService
    */
   public final SAEDocumentService createSaeDocumentService() {
      return EasyMock.createMock(SAEDocumentService.class);
   }

   /**
    * 
    * @return instance de {@link InterruptionTraitementMasseSupport}
    */
   public final InterruptionTraitementMasseSupport createInterruptionTraitementMasseSupport() {
      return EasyMock.createMock(InterruptionTraitementMasseSupport.class);
   }

   /**
    * 
    * @return instance de {@link SAEControleSupportService}
    */
   public final SAEControleSupportService createControleSupportService() {
      return EasyMock.createMock(SAEControleSupportService.class);
   }

   /**
    * 
    * @return instance de {@link JobQueueService}
    */
   public final JobQueueService createJobQueueService() {
      return EasyMock.createMock(JobQueueService.class);
   }
   
   /**
    * 
    * @return instance de {@link SaeDroitService}
    */
   public final SaeDroitService createDroitService() {
      return EasyMock.createMock(SaeDroitService.class);
   }

   /**
    * @return un mock de type {@link JobLauncher}
    */
   public final JobLauncher createJobLauncher() {
      return EasyMock.createMock(JobLauncher.class);
   }

}
