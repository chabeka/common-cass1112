package fr.urssaf.image.sae.storage.dfce.mapping;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;

import com.google.common.io.Files;

import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.DesiredMetaData;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeBase;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeCategories;
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
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
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
      storageDocument.setContent(Files.toByteArray(new File(saeDocument.getBase()
            .getFilePath())));
      return storageDocument;
   }

   /**
    * Permet de convertir les données StorageDocument en SaeDocument.<br/>
    * 
    * @param storageDocument
    *           : StorageDocuement.
    * @return SaeDocument {@link SaeDocument}
    * @throws IOException
    *            lorsque le fichier n'existe pas.
    * @throws ParseException
    *            Exception lorsque le parsing de la chaîne ne se déroule pas
    *            bien.
    */

   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public static SaeDocument storageDocumentToSaeDocument(
         StorageDocument storageDocument) throws IOException, ParseException {
      // Construction de l'objet SaeDocument
      SaeBase seaBase = new SaeBase();
      SaeCategories saeCategories = new SaeCategories();
      SaeDocument saeDocument = new SaeDocument();
      List<SaeCategory> listOfCategory = new ArrayList<SaeCategory>();

      List<StorageMetadata> listeMetaData = storageDocument.getMetadatas();
      for (StorageMetadata storageMetadata : listeMetaData) {
         final SaeCategory saeCategory = new SaeCategory();
         final Object value = storageMetadata.getValue();
         final String codeMetaData = storageMetadata.getShortCode();
         saeCategory.setName(codeMetaData);

         if (Constants.TEC_METADATAS[0].equals(codeMetaData)) {
            saeCategory.setValue(Utils.formatDateToString((Date) value));
         } else {
            saeCategory.setValue(value.toString());
         }
         listOfCategory.add(saeCategory);
      }
      // Set les éléments pour la construction de l'objet SaeDocument
      saeCategories.setCategories(listOfCategory);
      seaBase.setSaeCategories(saeCategories);
      seaBase.setFilePath(storageDocument.getFilePath());
      seaBase.setTypeDoc(storageDocument.getTypeDoc());
      saeDocument.setBase(seaBase);

      return saeDocument;
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

   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
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