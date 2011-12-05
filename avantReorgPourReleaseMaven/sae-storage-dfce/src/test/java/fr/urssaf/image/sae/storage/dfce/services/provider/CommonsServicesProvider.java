package fr.urssaf.image.sae.storage.dfce.services.provider;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.mapping.DocumentForTestMapper;
import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Classe abstract pour les tests.
 */
@SuppressWarnings("PMD.LongVariable")
public class CommonsServicesProvider extends CommonsServices {
   private StorageDocuments storageDocuments;
   private StorageDocument storageDocument;
   private List<StorageMetadata> storageMetadatas;
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   /**
    * @param storageServiceProvider
    *           : La façade de services
    * 
    */
   public final void setServiceProvider(
         final StorageServiceProvider storageServiceProvider) {
      this.serviceProvider = storageServiceProvider;
   }

   /**
    * @return La façade de services
    */
   public final StorageServiceProvider getServiceProvider() {
      return serviceProvider;
   }

   /**
    * Initialise les paramètres de connexion.
    * 
    * @throws ConnectionServiceEx
    *            Exception levée lorsque la connexion se passe mal
    * @throws ParseException
    *            Exception levée lorsque le parsing n'abouti pas.
    * @throws IOException
    *            Exception levée lorsque le fichier xml n'existe pas.
    */
   @Before
   public final void initParameter() throws ConnectionServiceEx, IOException,
         ParseException {
      setStorageDocuments(getStorageDocumentsFromXml());
      setStorageDocument(getStorageDocumentFromXml());
   }

   /**
    * @param storageDocuments
    *           : Un document de test.
    */
   public final void setStorageDocuments(final StorageDocuments storageDocuments) {
      this.storageDocuments = storageDocuments;
   }

   /**
    * @return Un document de test.
    */
   public final StorageDocuments getStorageDocuments() {
      return storageDocuments;
   }

   /**
    * 
    * @return La liste des storageDocuments à partir des fichier de xml.
    * @throws IOException
    *            Exception levée lorsque le fichier xml n'existe pas.
    * @throws ParseException
    *            Exception levée lorsque le parsing n'abouti pas.
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   private StorageDocuments getStorageDocumentsFromXml() throws IOException,
         ParseException {
      List<StorageDocument> storageDoc = new ArrayList<StorageDocument>();
      File files[] = new File[Constants.XML_PATH_DOC_WITHOUT_ERROR.length];
      int numFile = 0;
      for (String pathFile : Constants.XML_PATH_DOC_WITHOUT_ERROR) {
         files[numFile] = new File(pathFile);
         numFile++;
      }
      // Récupération des fichiers de tests désérialisé.
      final List<SaeDocument> saeDocuments = getXmlDataService()
            .saeDocumentsReader(files);
      // Mapping entre les fichiers de tests et les StorageDocument
      for (SaeDocument saeDocument : Utils.nullSafeIterable(saeDocuments)) {
         storageDoc.add(DocumentForTestMapper
               .saeDocumentXmlToStorageDocument(saeDocument));
      }
      StorageDocuments storDocuments = new StorageDocuments(storageDoc);

      return storDocuments;
   }

   /**
    * @param storageDocument
    *           : Le storageDocument à partir du fichier xml.
    */
   public final void setStorageDocument(final StorageDocument storageDocument) {
      this.storageDocument = storageDocument;
   }

   /**
    * @return Le storageDocument à partir du fichier xml.
    */
   public final StorageDocument getStorageDocument() {
      return storageDocument;
   }

   /**
    * 
    * @return
    * @throws IOException
    *            Exception levée lorsque le fichier xml n'existe pas.
    * @throws ParseException
    *            Exception levée lorsque le parsing n'abouti pas.
    */
   private StorageDocument getStorageDocumentFromXml() throws IOException,
         ParseException {
      final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
            new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
      return DocumentForTestMapper.saeDocumentXmlToStorageDocument(saeDocument);
   }

   /**
    * @param storageMetadatas
    *           the storageMetadatas to set
    */
   public final void setStorageMetadatas(
         final List<StorageMetadata> storageMetadatas) {
      this.storageMetadatas = storageMetadatas;
   }

   /**
    * @return the storageMetadatas
    */
   public final List<StorageMetadata> getStorageMetadatas() {
      return storageMetadatas;
   }

}
