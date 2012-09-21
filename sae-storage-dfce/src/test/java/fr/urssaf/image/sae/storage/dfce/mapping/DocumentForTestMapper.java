package fr.urssaf.image.sae.storage.dfce.mapping;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.google.common.io.Files;

import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.DesiredMetaData;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeCategory;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Fournit des méthodes statiques pour mapper les objets {@StorageDocument
 * 
 * } et des objets et les objets {@link SaeDocument}
 * 
 */
public final class DocumentForTestMapper {
   /**
    * Permet de convertir les données du document XML vers
    * {@link StorageDocument}.<br/>
    * 
    * @param saeDocument
    *           : Document à injecter pour les tests.
    * @return {@link StorageDocument}
    * @throws IOException
    *            lorsque le fichier n'existe pas.
    * @throws ParseException
    *            Exception lorsque le parsing de la chaîne ne se déroule pas
    *            bien.
    */
   public static StorageDocument saeDocumentXmlToStorageDocument(
         SaeDocument saeDocument) throws IOException, ParseException {

      // Initalisation de l'objet StotageDocument
      StorageDocument storageDocument = new StorageDocument();
      storageDocument.setTypeDoc(saeDocument.getBase().getTypeDoc());
      // Liste des métadonnées
      List<StorageMetadata> metadatas = new ArrayList<StorageMetadata>();
      // Liste des catégories à partir du XML
      List<SaeCategory> listOfCategoory = saeDocument.getDataBase()
            .getSaeCategories().getCategories();
      for (SaeCategory saeCategory : Utils.nullSafeIterable(listOfCategoory)) {
         final String codeMetaData = saeCategory.getName();
         Object value = saeCategory.getValue();
        	   if (Constants.TEC_METADATAS[0].equals(codeMetaData)) {
        	      		value =  Utils.formatStringToDate((String)value);
              } 
        	   if (Constants.TEC_METADATAS[1].equals(codeMetaData)) {
   	      		value =  Utils.formatStringToDate((String)value);
         } 
        	   if (Constants.TEC_METADATAS[3].equals(codeMetaData)) {
   	      		value =  Utils.formatStringToDate((String)value);
         } 
        	   if (Constants.TEC_METADATAS[2].equals(codeMetaData)) {
      	      		value =  Boolean.valueOf((String)value);
            } 
            metadatas.add(new StorageMetadata(codeMetaData, value));
               }
      storageDocument.setMetadatas(metadatas);
      storageDocument.setFilePath(new File(saeDocument.getBase()
            .getFilePath()).toString());
      storageDocument.setContent(Files.toByteArray(new File(saeDocument.getBase()
            .getFilePath())));
      return storageDocument;
   }

   /**
    * Permet de convertir les données du document métadonnées XML vers un
    * StorageDocument.<br/>
    * 
    * @param saeMetadata
    *           : document xml.
    * @return StorageDocument.
    * @throws IOException
    *            lorsque le fichier n'existe pas.
    * @throws ParseException
    *            Exception lorsque le parsing de la chaîne ne se déroule pas
    *            bien.
    */
   public static StorageDocument saeMetaDataXmlToStorageMetaData(
         DesiredMetaData saeMetadata) throws IOException, ParseException {
      Assert
            .assertNotNull("Objet DesiredMetaData ne doit pas être null pour faire le mapping"
                  + saeMetadata);
      // Initialisation de l'objet StotageDocument
      List<StorageMetadata> metadatas = new ArrayList<StorageMetadata>();
      for (String desiredMetaData : saeMetadata.getCodes()) {
         final StorageMetadata storageMetadata = new StorageMetadata(
               desiredMetaData);
         metadatas.add(storageMetadata);
      }
      return new StorageDocument(metadatas);
   }

   /** Cette classe n'est pas faite pour être instanciée. */
   private DocumentForTestMapper() {
      assert false;
   }
}