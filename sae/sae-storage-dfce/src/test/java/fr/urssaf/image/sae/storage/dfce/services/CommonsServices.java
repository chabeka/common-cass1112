package fr.urssaf.image.sae.storage.dfce.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.dfce.mapping.DocumentForTestMapper;
import fr.urssaf.image.sae.storage.dfce.services.xml.XmlDataService;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;

/**
 * Classe de base pour les tests unitaires.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
@SuppressWarnings( { "PMD.ExcessiveImports", "PMD.LongVariable",
      "AbstractClassWithoutAbstractMethod" })
public abstract class CommonsServices {
   @Autowired
   @Qualifier("xmlDataService")
   private XmlDataService xmlDataService;

   @Autowired
   @Qualifier("dfceServicesManager")
   private DFCEServicesManager dfceServicesManager;

   /**
    * 
    * @param dfceServices
    *           : Les services DFCE
    */
   public final void setDfceServicesManager(
         final DFCEServicesManager dfceServices) {
      this.dfceServicesManager = dfceServices;
   }

   /**
    * 
    * @return Les services DFCE
    */
   public final DFCEServicesManager getDfceServicesManager() {
      return dfceServicesManager;
   }

   /**
    * @return Le service de gestion du fichier xml.
    */
   public final XmlDataService getXmlDataService() {
      return xmlDataService;
   }

   /**
    * @param xmlDataService
    *           : Le service de gestion du fichier xml.
    */
   public final void setXmlDataService(final XmlDataService xmlDataService) {
      this.xmlDataService = xmlDataService;
   }

   /**
    * Initialisation des tests. <br>{@inheritDoc}
    */
   public final StorageDocument getMockData(
         final InsertionService insertionService) throws IOException,
         ParseException, InsertionServiceEx {
      // Injection de jeu de donnée.
      final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
            new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
      final StorageDocument storageDocument = DocumentForTestMapper
            .saeDocumentXmlToStorageDocument(saeDocument);
      return insertionService.insertStorageDocument(storageDocument);
   }

   /**
    * Suppression du jeu de donnée.<br>{@inheritDoc}
    */
   public final void destroyMockTest(final UUID uuid,
         final DeletionService deletionService) throws DeletionServiceEx {
      final List<StorageMetadata> desiredStorageMetadatas = new ArrayList<StorageMetadata>();
      final UUIDCriteria uuidCriteria = new UUIDCriteria(uuid,
            desiredStorageMetadatas);
      deletionService.deleteStorageDocument(uuidCriteria.getUuid());
   }
}
