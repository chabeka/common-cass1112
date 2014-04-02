/**
 * 
 */
package fr.urssaf.image.sae.utils;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
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
    * création d'un mock {@link SAEDocumentService}
    * 
    * @return un mock SAEDocumentService
    */
   public final SAEDocumentService createSaeDocumentService() {
      return EasyMock.createMock(SAEDocumentService.class);
   }
   
   
   public static final UntypedDocument getUntypedDocumentMockData() {
      
      UntypedDocument doc = new UntypedDocument();
      
      doc.setFilePath("src/test/resources/doc/doc1.PDF");
      
      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      doc.setUMetadatas(metadatas);
      metadatas.add(new UntypedMetadata("SiteAcquisition", "CER69"));
      metadatas.add(new UntypedMetadata("Titre", "NOTIFICATIONS DE REMBOURSEMENT du 41882050200023"));
      metadatas.add(new UntypedMetadata("DateCreation", "2012-01-01"));
      metadatas.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(new UntypedMetadata("Hash", "A2f93f1f121ebba0faef2c0596f2f126eacae77b"));
      metadatas.add(new UntypedMetadata("TypeHash", "SHA-1"));
      metadatas.add(new UntypedMetadata("TracabilitePreArchivage", "P"));
      metadatas.add(new UntypedMetadata("ApplicationProductrice", "GED"));
      metadatas.add(new UntypedMetadata("FormatFichier", "fmt/354"));
      metadatas.add(new UntypedMetadata("NbPages", "2"));
      metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "UR030"));
      metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR030"));
      
      return doc;
      
   }
   
   public static final SAEDocument getSAEDocumentMockData() {
      
      SAEDocument doc = new SAEDocument();
      
      doc.setFilePath("src/test/resources/doc/doc1.PDF");
      
      List<SAEMetadata> metadatas = new ArrayList<SAEMetadata>(); 
      doc.setMetadatas(metadatas);
      metadatas.add(new SAEMetadata("SiteAcquisition", "CER69"));
      metadatas.add(new SAEMetadata("Titre", "NOTIFICATIONS DE REMBOURSEMENT du 41882050200023"));
      metadatas.add(new SAEMetadata("DateCreation", "2012-01-01")); // TODO
      metadatas.add(new SAEMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(new SAEMetadata("Hash", "A2f93f1f121ebba0faef2c0596f2f126eacae77b"));
      metadatas.add(new SAEMetadata("TypeHash", "SHA-1"));
      metadatas.add(new SAEMetadata("TracabilitePreArchivage", "P"));
      metadatas.add(new SAEMetadata("ApplicationProductrice", "GED"));
      metadatas.add(new SAEMetadata("FormatFichier", "fmt/354"));
      metadatas.add(new SAEMetadata("NbPages", new Integer(2)));
      metadatas.add(new SAEMetadata("CodeOrganismeProprietaire", "UR030"));
      metadatas.add(new SAEMetadata("CodeOrganismeGestionnaire", "UR030"));
      
      return doc;
      
   }

}
